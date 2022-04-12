package FlinkSqlApi

import com.jsoniter.JsonIterator
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import pojo.CommercePojo
import utils.KafkaTemp.consumerProps

object CommerceFlinkSql {
  case class Commerce(InvoiceNo: String, StockCode: String, Description: String, Quantity: String, InvoiceDate: String, UnitPrice: String, CustomerID: String, Country: String)

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)

    val source: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("commerce_info", new SimpleStringSchema(), consumerProps))
    val stream: DataStream[CommercePojo] = source.map(x => {
      JsonIterator.deserialize(x, new CommercePojo().getClass)
    })
    val inputTable: Table = tableEnv.fromDataStream(stream)
    tableEnv.createTemporaryView("commerce", inputTable)
    tableEnv.toDataStream(tableEnv.sqlQuery("select upper(Description) from commerce"))
    tableEnv.toChangelogStream(tableEnv.sqlQuery("select Country, max(Description) from commerce group by Country")).print("change")
    env.execute()
  }
}
