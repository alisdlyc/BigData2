package scala

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

object WordCount {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val inputPath = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources\\wc.txt"

    val inputDs: DataSet[String] = env.readTextFile(inputPath)

    import org.apache.flink.api.scala._
    val wordCountDs = inputDs.flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

      wordCountDs.print()
  }
}
