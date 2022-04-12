package KafkaUse

import KafkaUse.sink.Output
import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}
import org.apache.flink.core.fs.FileSystem.WriteMode

object FlinkSinkToKafka {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val path = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\KafkaGo\\src\\main\\resources\\UserBehavior.csv"
    val lines: DataSink[(Int, Int, Int, String, Int)] = env.readCsvFile[(Int, Int, Int, String, Int)](path)
      .output(new Output())
      .setParallelism(1)
    env.execute("DataSet Sink to topic")
  }
}
