package KafkaUse.sink

import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import utils.Utils.WSL_IP

import java.util.Properties

class Output extends OutputFormat[(Int, Int, Int, String, Int)] {
  private var producer:Producer[String, String] = null
  private val topic = "user_behaviour_info"

  override def configure(configuration: Configuration): Unit = {

  }

  override def open(i: Int, i1: Int): Unit = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", WSL_IP)
    properties.setProperty("acks", "all")
    properties.setProperty("topic", topic)
    properties.setProperty("retries", "0")
    properties.setProperty("batch.size", "100")
    properties.setProperty("linger.ms", "1")
    properties.setProperty("buffer.memory", "10240")
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    producer = new KafkaProducer[String, String](properties)
  }

  override def writeRecord(it: (Int, Int, Int, String, Int)): Unit = {
    producer.send(new ProducerRecord[String, String](topic, System.currentTimeMillis().toString,
      String.join(",", it._1.toString, it._2.toString, it._3.toString, it._4, it._5.toString)))
  }

  override def close(): Unit = {
    producer.close()
  }
}
