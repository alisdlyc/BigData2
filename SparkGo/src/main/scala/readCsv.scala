import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object readCsv extends App {
  val spark = SparkSession.builder().master("local").appName("hdfs").getOrCreate()
  val rowRdd = spark.read
    .option("header", "true")
    .option("nullValue", "?")
    .option("inferSchema", "true")
    .csv("hdfs://localhost:9000/data/linkage")

  rowRdd.printSchema()

  spark.stop()
}