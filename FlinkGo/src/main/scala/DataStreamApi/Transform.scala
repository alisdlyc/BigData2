package DataStreamApi

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{AllWindowedStream, DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream, createTypeInformation}
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

import scala.tools.nsc.io.Socket

object Transform {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val source: DataStream[String] = env.socketTextStream("localhost", 9999)
    // 1. map
    // 将DataStream中的每一个元素转换为另一个元素，为一对一的关系
    source.map(x => x * 2).print("map")
    // 2. flatMap
    // 采用一个数据元并生成零个、一个或者多个数据元。如将句子分割为单词
    source.flatMap(line => line.split(" ")).print("flatMap")
    // 3. filter
    // 计算每个数据元的布尔函数，并保存函数返回为true的数据元
    source.filter(line => line.contains("spark")).print("filter")
    // 4. keyBy
    // 逻辑上将流分区为不相交的分区，具有相同keys的所有记录都分配给同一分区。在内部，keyBy通过散列分区实现，指定键有不同的方法。
    // 该方法返回keyedStream，其中包括使用被keys化状态所需要的KeyedStream
    val keyByStream: KeyedStream[(String, Int), String] = source.flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(_._1)
    // 5. reduce
    // 被Keys化数据流上的滚动Reduce，将当前数据元与最后一个Reduce的值组合并发出新值
    keyByStream.reduce((x, y) => (x._1, x._2+y._2)).print("reduce")
    // 6. fold
    // 具有初始值的被keys化的数据流上的滚动折叠。将当前数据元与最后折叠的值组合发出新值
      //???好像删除了, 源码中也没有
    // 7. aggregations: sum|min|max|minBy|maxBy
    keyByStream.sum(1).print("sum")
    keyByStream.min(1).print("min")
    keyByStream.max(1).print("max")
    keyByStream.minBy(0).print("minBy")
    keyByStream.maxBy(0).print("maxBy")
    // 8. window
    // 可以对已分区的KeyedStream上定义Windows，Windows根据某些特征对每个Keys中的数据进行分组
    val windowKeyByStream: WindowedStream[(String, Int), String, TimeWindow] = keyByStream.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    // 9. windowAll
    // 可以在常规的DataStream上定义，Windows根据某些特征对所有流事件进行分组
    val windowStream: AllWindowedStream[String, TimeWindow] = source.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
    // 10. window Apply
    // 将一般函数应用于整个窗口
        //    windowKeyByStream.apply()
        //    windowStream.apply()
    // 11. window reduce
    // 将reduce应用于窗口，并返回结果
        //        windowKeyByStream.reduce()
    // 12. window fold ???
    // 13. union
    // 两个或多个数据流的联合
    keyByStream.union(keyByStream)
    // 14. window join
    // 在给定keys和公共窗口上连接两个数据流
    // 15. interval join
    // 在给定的时间间隔内使用公共的Keys关联两个被Key化的数据流的两个数据元e1和e2
    // 以便：e1.timestamp + lowerBound <= e2.timestamp <= e1.timestamp + upperBound
    // 16. window CoGroup
    // 在给定Keys和公共窗口上对两个数据流进行Cogroup
    // 17. connect
    // 连接两个保存其类型的数据流，连接允许两个流之间共享状态
    // 18. CoMap\CoFlatMap
    // 用于连接数据流上的map和flatMap
    // 19. split
    // 根据某些标准将流拆分为两个或多个流
    // 20. select
    // 从拆分流中选择一个或多个流
    env.execute()
  }
}
