package DataStreamApi

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object IntervalJoin {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    env.execute("interval join")
  }
}
