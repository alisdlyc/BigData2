import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

case class SensorReading(id: String, timestamp: Long, temperature: Double)

object Sensor {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val stream1 = env.fromCollection(List(
      SensorReading("sensor_1", 1, 13.7),
      SensorReading("sensor_3", 2, 11.7),
      SensorReading("sensor_5", 3, 12.7),
      SensorReading("sensor_7", 5, 16.7)
    ))
    stream1.print("stream1: ").setParallelism(1)

    val stream2 = env.readTextFile("C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\FlinkGo\\src\\main\\resources\\sensor.txt")
      .map(line => {
        val items = line.split(" ")
        SensorReading(id = items(0), timestamp = items(1).toInt, temperature = items(2).toDouble)
      })
    stream2.print("stream2: ").setParallelism(1)


    env.execute()
  }
}
