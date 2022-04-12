package DataSetApi

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}
import org.apache.flink.core.fs.FileSystem.WriteMode

object sinkApi extends App {
  val env = ExecutionEnvironment.getExecutionEnvironment
  var dataSet = env.fromCollection(List(1, 2, 3, 4, 6))
  // 1. 将数据输出到本地集合
  dataSet.collect()
  // 2. 将数据输出到文件，本地/HDFS/text/CSV
  dataSet.writeAsText("out/Sink", WriteMode.OVERWRITE)
}
