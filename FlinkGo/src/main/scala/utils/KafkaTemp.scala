package utils

import utils.Utils.WSL_IP

import java.util.Properties

object KafkaTemp {
  val consumerProps = new Properties()
  consumerProps.setProperty("bootstrap.servers", WSL_IP)
  consumerProps.setProperty("group.id", "consumer-group-Source_1")
  consumerProps.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProps.setProperty("auto.offset.reset", "earliest")
  consumerProps.setProperty("ProducerConfig.BOOTSTRAP_SERVERS_CONFIG", "0.0.0.0:9092")
}
