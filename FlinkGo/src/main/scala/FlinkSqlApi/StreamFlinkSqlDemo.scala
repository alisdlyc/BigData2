package FlinkSqlApi

import java.util.UUID
import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{Table, TableEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.bridge.scala.{StreamTableEnvironment, tableConversions}
import org.apache.flink.types.Row

import scala.util.Random

object StreamFlinkSqlDemo {

  case class Order(orderId:String, userId:Int, money:Long, createTime:Long)

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)

    val orderDataStream: DataStream[Order] = env.addSource(new RichSourceFunction[Order] {
      var isRunning = true
      override def run(ctx: SourceFunction.SourceContext[Order]): Unit = {
        // - 随机生成订单ID（UUID）
        // - 随机生成用户ID（0-2）
        // - 随机生成订单金额（0-100）
        // - 时间戳为当前系统时间
        // - 每隔1秒生成一个订单
        for (i <- 0 until 1000 if isRunning) {
          val order: Order = Order(UUID.randomUUID().toString, Random.nextInt(3), Random.nextInt(101),
            System.currentTimeMillis())
          TimeUnit.SECONDS.sleep(1)
          ctx.collect(order)
        }
      }
      override def cancel(): Unit = { isRunning = false }
    })

    val watermarkDataStream: DataStream[Order] = orderDataStream.assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[Order](Time.seconds(2)) {
        override def extractTimestamp(element: Order): Long = {
          val eventTime: Long = element.createTime
          eventTime
        }
      }
    )
    import org.apache.flink.table.api._
    tableEnv.registerDataStream("t_order", watermarkDataStream, 'orderId, 'userId, 'money,'createTime.rowtime)
    val sql =
      """
        |select
        | userId,
        | count(1) as totalCount,
        | max(money) as maxMoney,
        | min(money) as minMoney
        | from
        | t_order
        | group by
        | tumble(createTime, interval '5' second),
        | userId
      """.stripMargin
    // 9. 使用`tableEnv.sqlQuery`执行sql语句
    val table: Table = tableEnv.sqlQuery(sql)

    // 10. 将SQL的执行结果转换成DataStream再打印出来
    table.toRetractStream[Row].print()
    env.execute("StreamSQLApp")
  }
}
