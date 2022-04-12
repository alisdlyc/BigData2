package FlinkSqlApi

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.calcite.shaded.com.fasterxml.jackson.databind.JsonNode
import org.apache.flink.calcite.shaded.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.util.Collector
import utils.Utils.WSL_IP

object BilibiliFlinkSql {
  case class DanMu(timestamp: String, uuid: String, content: String)

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)
    val source: KafkaSource[String] = KafkaSource.builder[String]()
      .setBootstrapServers(WSL_IP)
      .setTopics("bilibili_cat")
      .setGroupId("bilibili_cat_02")
      .setStartingOffsets(OffsetsInitializer.earliest())
      .setValueOnlyDeserializer(new SimpleStringSchema())
      .build()
    val inputStream: DataStream[String] = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source")


    env.execute()
  }

  def createKafkaTable(): String = {
    """
      |CREATE TABLE demo1 (
      |    uid VARCHAR COMMENT 'uid',
      |    rid VARCHAR COMMENT 'rid'
      |)
      |WITH (
      |    'connector.type' = 'kafka', -- 使用 kafka connector
      |    'connector.version' = 'universal',  -- kafka 版本
      |    'connector.topic' = 'test',  -- kafka topic
      |    'connector.properties.0.key' = 'zookeeper.connect',  -- zk连接信息
      |    'connector.properties.0.value' = 'hosts:2181',  -- zk连接信息
      |    'connector.properties.1.key' = 'bootstrap.servers',  -- broker连接信息
      |    'connector.properties.1.value' = 'hosts:9092',  -- broker连接信息
      |    'connector.sink-partitioner' = 'fixed',
      |    'update-mode' = 'append',
      |    'format.type' = 'custom',  -- 数据源格式为解析换为自定义
      |    'format.derive-schema' = 'true' -- 从 DDL schema 确定 json 解析规则
      |)
    """.stripMargin
  }
}
