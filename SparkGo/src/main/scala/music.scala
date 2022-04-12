import org.apache.spark.sql.SparkSession

object music {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").appName("music").getOrCreate()

    var rawUserArtistData = spark.read.textFile("hdfs://localhost:9000/data/music/user_artist_data.txt")

    case class PlayInfo(uid: Int, mid: Int, times: Int)

    import spark.implicits._
    import org.apache.spark.sql.functions._
    rawUserArtistData.printSchema()

    val playInfoDf = rawUserArtistData.map(line => {
      val splitLine = line.split(" ")
      (splitLine(0).toInt, splitLine(1).toInt, splitLine(2).toInt)
    }).toDF("user", "artist", "count")
    playInfoDf.printSchema()
    playInfoDf.agg(min("user"), max("user"), min("artist"), max("artist"), min("count"), max("count")).show

    val rawArtistData = spark.read.textFile("hdfs://localhost:9000/data/music/artist_data.txt")

    // 艺术家(id, name)
    val artistByID = rawArtistData.flatMap{line =>
      val (id, name) = line.span(_ != '\t')
      if(name.isEmpty) {
        None
      } else {
        try {
          Some((id.toInt, name.trim))
        } catch {
          case _: NumberFormatException => None
        }
      }
    }.toDF("id", "name")

    // 艺术家的别名(id, alias_id)
    val rawArtistAlias = spark.read.textFile("hdfs://localhost:9000/data/music/artist_alias.txt")
    val artistAlias = rawArtistAlias.flatMap{line =>
      val Array(artist, alias) = line.split('\t')
      if(artist.isEmpty) {
        None
      } else {
        Some((artist.toInt, alias.toInt))
      }
    }.collect().toMap

    artistByID.filter($"id" isin (1208690,1003926)).show

    import org.apache.spark.sql._
    import org.apache.spark.broadcast._
    // 通过广播变量，修正user artist data中错误的artistID
    def buildCounts(rawUserArtistData: Dataset[String], bArtistAlias: Broadcast[Map[Int, Int]]): DataFrame = {
      rawUserArtistData.map{line =>
        val Array(userId, artistId, count) = line.split(' ').map(_.toInt)
        val finalArtistID = bArtistAlias.value.getOrElse(artistId, artistId)
        (userId, finalArtistID, count)
      }.toDF("user", "artist", "count")
    }
    val bArtistAlias = spark.sparkContext.broadcast(artistAlias)
    val trainData = buildCounts(rawUserArtistData, bArtistAlias)
    trainData.cache()
    trainData.count()
    // 构建ALS模型
    import org.apache.spark.ml.recommendation._
    import scala.util.Random
    val model = new ALS()
      .setSeed(Random.nextLong())
      .setImplicitPrefs(true)
      .setRank(10)
      .setRegParam(0.01)
      .setAlpha(1.0)
      .setMaxIter(5)
      .setUserCol("user")
      .setItemCol("artist")
      .setRatingCol("count")
      .setPredictionCol("prediction")
      .fit(trainData)

    model.userFactors.show(1, truncate = false)

    val userId = 2093760
    val existingArtistIds = trainData.filter($"user" === userId)
      .select("artist")
      .as[Int]
      .collect()
    artistByID.filter($"id" isin (existingArtistIds:_*)).show()

    def makeRecommendations(model: ALSModel, userID: Int, howMany: Int): DataFrame = {
      val toRecommend = model
        .itemFactors
        .select($"id".as("artist"))
        .withColumn("user", lit(userID)) // 选取所有艺术家ID与对应的目标用户ID

      model.transform(toRecommend)
        .select("artist", "prediction")
        .orderBy($"prediction".desc)
        .limit(howMany) // 对所有艺术家评分，选取其中分值最高的
    }

    val topRecommendations = makeRecommendations(model, userId, 20)
    topRecommendations.show()

    val recommendedArtistIDs = topRecommendations.select("artist").as[Int].collect()
    artistByID.filter($"id" isin (recommendedArtistIDs:_*)).show()
  }
}
