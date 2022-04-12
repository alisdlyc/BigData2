package DataStreamApi

import org.apache.flink.api.common.functions.CoGroupFunction
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import utils.Utils

import java.lang

object IntervalJoinDemo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val view_info: KeyedStream[View, Int] = env.fromCollection(List(
      View(1, 1, Utils.tranTimeToLong("2020-04-29 13:01:00")),
      View(1, 2, Utils.tranTimeToLong("2020-04-29 13:01:01")),
      View(1, 3, Utils.tranTimeToLong("2020-04-29 13:01:02")),
      View(1, 4, Utils.tranTimeToLong("2020-04-29 13:01:03")),
      View(2, 5, Utils.tranTimeToLong("2020-04-29 13:01:20")),
      View(2, 6, Utils.tranTimeToLong("2020-04-29 13:01:28")),
      View(3, 7, Utils.tranTimeToLong("2020-04-29 13:01:29")),
      View(4, 8, Utils.tranTimeToLong("2020-04-29 13:01:30"))
    ))
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[View](Time.seconds(1)) {
        override def extractTimestamp(t: View) = t.timeStamp
      })
      .keyBy(_.cid)

    val stay_time: KeyedStream[TimeInfo, Int] = env.fromCollection(List(
      TimeInfo(1, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:02")),
      TimeInfo(2, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:04")),
      TimeInfo(3, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:06")),
      TimeInfo(4, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:09")),
      TimeInfo(5, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:50")),
      TimeInfo(6, 1.1, Utils.tranTimeToLong("2020-04-29 13:01:55"))
    )).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[TimeInfo](Time.seconds(1)) {
      override def extractTimestamp(t: TimeInfo) = t.timeStamp
    })
      .keyBy(_.cid)

    val reStream: DataStream[ViewInfo] = stay_time.intervalJoin(view_info)
      .between(Time.seconds(-5), Time.seconds(0))
      .process(new ProcessJoinFunction[TimeInfo, View, ViewInfo] {
        override def processElement(in1: TimeInfo, in2: View, context: ProcessJoinFunction[TimeInfo, View, ViewInfo]#Context, out: Collector[ViewInfo]) = {
          out.collect(ViewInfo(in2.uid, in2.cid, in1.time))
        }
      })


    // inner join
    view_info.join(reStream)
      .where(_.cid).equalTo(_.cid)
      .window(TumblingEventTimeWindows.of(Time.seconds(6)))
      .apply((e1, e2) => {
          ViewInfo(e1.uid, e1.cid, e2.time)
      })

    // left outer join
    view_info.coGroup(reStream)
      .where(_.cid).equalTo(_.cid)
      .window(TumblingEventTimeWindows.of(Time.seconds(6)))
      .apply(new CoGroupFunction[View, ViewInfo, ViewInfo] {
        override def coGroup(viewInfoRecords: lang.Iterable[View], timeInfoRecords: lang.Iterable[ViewInfo], out: Collector[ViewInfo]) = {
          import scala.collection.JavaConverters._
          val leftList: List[View] = viewInfoRecords.asScala.toList
          val rightList: List[ViewInfo] = timeInfoRecords.asScala.toList
          for (view <- leftList) {
            var isMatch = false
            for (timeInfo <- rightList) {
              // 右流中有匹配的记录
              out.collect(timeInfo)
              isMatch = true
            }
            if(!isMatch) {
              out.collect(ViewInfo(view.uid, view.cid, 5))
            }
          }
        }
      })
      .print("re")
      .setParallelism(1)
    env.execute("双流join")
  }
}
case class View(uid: Int, cid: Int, timeStamp: Long)
case class TimeInfo(cid: Int, time: Double, timeStamp: Long)
case class ViewInfo(uid: Int, cid: Int, time: Double)
