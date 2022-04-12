package SparkSqlApi

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}

object CreateDf {
  case class Person(id: Int, name: String, age: Int)
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("create df").setMaster("local")
    val sc = new SparkContext(conf)
    val inputRdd: RDD[String] = sc.textFile("C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\SparkGo\\src\\main\\resources\\people.txt")
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val personDf: DataFrame = inputRdd.map(line => {
      val strings: Array[String] = line.split(" ")
      Person(strings(0).toInt, strings(1), strings(2).toInt)
    }).toDF()

    personDf.show()
    personDf.printSchema()
    sc.stop()
  }
}
