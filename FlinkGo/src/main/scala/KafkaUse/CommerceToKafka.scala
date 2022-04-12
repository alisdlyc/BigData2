package KafkaUse

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, createTypeInformation}
import utils.Utils.tranCommerceDatetime
import sink.CommerceOutput

object CommerceToKafka {
  case class Commerce(InvoiceNo: String, StockCode: String, Description: String, Quantity: String, InvoiceDate: String, UnitPrice: String, CustomerID: String, Country: String)

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val path = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources\\data.csv"

    val inputDs: DataSet[Commerce] = env.readCsvFile[Commerce](path)
    inputDs.map(x => {
      val timeStamp: String = tranCommerceDatetime(x.InvoiceDate)
      Commerce(x.InvoiceNo, x.StockCode, x.Description, x.Quantity, timeStamp, x.UnitPrice, x.CustomerID, x.Country)
    })
      .output(CommerceOutput)
      .setParallelism(1)
    env.execute()
  }
}
