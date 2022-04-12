package DataSetApi

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}
import org.apache.flink.configuration.Configuration


object fromCollection extends App {
  val env = ExecutionEnvironment.getExecutionEnvironment
  // 1. 从集合创建DataSet
  val textDataSet = env.fromCollection(
    List("1,a", "2,b", "3,c")
  )
  // 2. 从文件创建DataSet
  val textDataSet_file = env.readTextFile("C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources\\wc.txt")
  // 3. 遍历目录，读取多个文件
  val parameters = new Configuration
  parameters.setBoolean("recursive.file.enumeration", true)
  val textDataSet_files = env.readTextFile("C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources").withParameters(parameters)
  // 4. flink还可以通过readTextFile读取压缩文件，可以自动识别并解压，但可能不会并行读取
}
