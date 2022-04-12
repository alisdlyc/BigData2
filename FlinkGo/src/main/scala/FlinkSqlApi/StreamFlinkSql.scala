package FlinkSqlApi

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object StreamFlinkSql {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)

    val stream: DataStream[String] = env.fromCollection(List("spark", "flink", "kafka"))
    val inputTable: Table = tableEnv.fromDataStream(stream)

    tableEnv.createTemporaryView("InputTable", inputTable)

    val re: Table = tableEnv.sqlQuery("select upper(f0) from InputTable")
    tableEnv.toDataStream(re)
      .print()

    env.execute("create table on stream")
  }
}
