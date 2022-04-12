package DataStreamApi

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.util.Properties

/**
 * 1. 基于本地集合的source
 * 2. 基于文件的source
 * 3. 基于网络套接字的source
 * 4. 自定义source
 * */
object Source {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = env.socketTextStream("localhost", 9999)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "172.26.154.168:9092")
    properties.setProperty("group.id", "consumer-group-Source")
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "earliest")
    properties.setProperty("ProducerConfig.BOOTSTRAP_SERVERS_CONFIG", "0.0.0.0:9092");

    val kafkaSource: DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("user_behaviour_info", new SimpleStringSchema(), properties))

    kafkaSource.print("kafka")
    env.execute()
  }
}
