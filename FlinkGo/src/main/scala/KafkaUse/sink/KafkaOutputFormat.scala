package KafkaUse.sink

import org.apache.flink.api.common.io.RichOutputFormat
import org.apache.flink.configuration.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import utils.Utils.WSL_IP

import java.util.Properties

class KafkaOutputFormat extends RichOutputFormat[String]{
  private var producer:Producer[String, String] = null
  private val topic = "user_behaviour"
  override def configure(configuration: Configuration): Unit = {
  }

  override def open(taskNumber: Int, numTasks: Int): Unit = {
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

  override def writeRecord(record: String): Unit = {
    producer.send(new ProducerRecord[String, String](this.topic, System.currentTimeMillis().toString, record))
  }

  override def close(): Unit = {
    producer.close()
  }
}
