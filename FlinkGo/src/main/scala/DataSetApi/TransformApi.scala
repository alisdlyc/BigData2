package DataSetApi

import org.apache.flink.api.common.functions.GroupReduceFunction
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.createTypeInformation
import org.apache.flink.util.Collector

import scala.collection.mutable

object TransformApi extends App {
  val env = ExecutionEnvironment.getExecutionEnvironment
  val path = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources\\name_id"
  val textDataSet: DataSet[String] = env.readTextFile(path)

  // 1.map: 将DataSet中的每一个元素（一行String）转换为另外一个元素（User对象）
  case class User(name: String, id: String)
  val userDs: DataSet[User] = textDataSet.map(line => {
    val items = line.split(" ")
    User(items(0), items(1))
  })
  userDs.print()

  // 2.flatMap: 将DataSet中的每一个元素转换为0到多个元素
  // 对每个子数据流的每个元素进行映射操作，然后再进行扁平化处理。汇聚结果为一个新的列表
  val result: AggregateDataSet[(String, Int)] = textDataSet.flatMap(_.split(" "))
    .map((_, 1))
    .groupBy(0)
    .sum(1)
  result.print()

  // 3.mapPartition: 将一个分区中的元素转换为另一个元素
  // 该函数将分区作为迭代器，用于产生任意数量的结果
  val text: DataSet[String] = env.fromCollection(
    List("张三,1", "李四,2", "王五,3", "张三,4")
  )
  text.mapPartition(list => {
    list.map(line => {
      val items = line.split(",")
      User(items(0), items(1))
    })
  }).print()

  // 4.filter
  text.filter(_.contains("三")).print()

  // 5.reduce
  var source = env.fromElements(("spark", 1), ("flink", 1), ("spark", 2))
  source.groupBy(_._1)
    .reduce((x, y) => (x._1, x._2+y._2))
    .print()

  // 6.reduceGroup
  // 将一个dataset或者一个group聚合成一个或多个元素
  // 它会先分组reduce，然后再做整体的reduce，这样做可以减少网络IO
  source.groupBy(_._1)
    .reduceGroup{
      (in: Iterator[(String, Int)], out: Collector[(String, Int)]) =>
        val tuple = in.reduce((x, y) => (x._1, x._2+y._2))
        out.collect(tuple)
    }
    .print()

  // 7.minBy和maxBy
  // 选择具有最小值或最大值的元素
  source = env.fromCollection(
    List(("张三", 1), ("张三", 2), ("李四", -1), ("李四", 0))
  )
  val userText = source.mapPartition(item => {
    item.map(tuple => User(tuple._1, tuple._2.toString))
  })
  println("minBy: ")
  userText.groupBy(0)
    .minBy(1)
    .print()
  println("maxBy: ")
  userText.groupBy(0)
    .maxBy(1)
    .print()

  // 8.Aggregate
  // 在数据集上进行聚合求最值, Aggregate只能作用在元组上
  var data = new mutable.MutableList[(Int, String, Double)]
  data+=((1, "语文", 82))
  data+=((2, "数学", 92))
  data+=((3, "语文", 102))
  env.fromCollection(data)
    .groupBy(1)
    .aggregate(Aggregations.MAX, 2)
    .print()
  env.fromCollection(data)
    .groupBy(1)
    .maxBy(2)
    .print()

  // 9.distinct
  // 根据对应的索引名称去除重复的记录
  println("distinct: ")
  env.fromCollection(data)
    .distinct(1)
    .setParallelism(4)
    .print()

  // 10.first
  // 取前几个数
  println("first: ")
  env.fromCollection(data)
    .first(2)
    .print()

  // 11.join(inner join)
  // 将两个DataSet按照一定的条件连接到一起，形成新的DataSet
  println("inner join: ")
  var student = new mutable.MutableList[(Int, String, Int)]
  student+=((1, "qwq", 2), (22, "ovo", 9), (2, "alisdlyc", 9), (3, "qwq", 2))
  var classInfo = new mutable.MutableList[(Int, String)]
  classInfo+=((9, "9班"))
  var s1 = env.fromCollection(student)
  var s2 = env.fromCollection(classInfo)
  var joinData = s1.join(s2)
    .where(2).equalTo(0) {
    (s1, s2) => (s1._1, s1._2, s2._2)
  }
    .print()

  // 12.leftOuterJoin/RightOuterJoin/FullOuterJoin
  // 如何实现DataSet Join时候的多个条件判断捏?
  println("Left Outer Join: ")
  s1.leftOuterJoin(s2)
    .where(2).equalTo(0).apply((s1, s2) => {
    if(s2 == null) {
      (s1._1, s1._2, null)
    } else {
      (s1._1, s1._2, s2._2)
    }
  })
    .print()

  // 13.cross
  // 形成数据集与其他数据集的笛卡尔积
  println("Cross 笛卡尔积: ")
  s1.cross(s2).apply((s1, s2) => {
    (s1._1, s1._2, s2._2)
  })
    .print()

  // 14.union
  println("Union 操作不会去重: ")
  s1.union(s1)
    .print()

  // 15.rebalance
  // 负载均衡，用于处理数据倾斜，将数据均匀打散
  s1.rebalance()

  // 16.partitionByHash
  // 按照指定key进行hash分区
  s1.setParallelism(1)
    .partitionByHash(1)
    .setParallelism(2)
    .mapPartition(_.map(x => (x._1, x._2, x._3)))
    .writeAsText("out/hashPartition", WriteMode.OVERWRITE)

  // 17.partitionByRange
  // 根据指定的key按照范围对数据集进行区分, 等值划分还是范围划分?
  var datas = new mutable.MutableList[(Int, Long, String)]
  datas+=((1, 1L, "Hi"))
  datas+=((2, 2L, "Hello"))
  datas+=((3, 2L, "Hello world"))
  datas+=((4, 3L, "Hello world, how are you?"))
  env.fromCollection(datas)
    .partitionByRange(_._2)
    .setParallelism(2)
    .mapPartition(_.map(x => (x._1, x._2, x._3)))
    .writeAsText("out/rangePartition", WriteMode.OVERWRITE)

  // 18.sortParation
  // 根据指定的字段值进行分区的排序
  env.fromCollection(datas)
    .map(x=>x)
    .setParallelism(2)
    .sortPartition(1, Order.DESCENDING)
    .mapPartition(line=>line)
    .writeAsText("out/sortPartition", WriteMode.OVERWRITE)
  env.execute()
}

