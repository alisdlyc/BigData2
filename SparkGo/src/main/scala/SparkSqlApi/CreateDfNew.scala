package SparkSqlApi

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, SparkSession}

object CreateDfNew {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local")
    val spark: SparkSession = SparkSession.builder()
      .appName("create df by spark session")
      .config(conf)
      .getOrCreate()
    val inputDs: Dataset[String] = spark.read.textFile("C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\SparkGo\\src\\main\\resources\\people.txt")
    inputDs.printSchema()
    inputDs.show()
  }
}
