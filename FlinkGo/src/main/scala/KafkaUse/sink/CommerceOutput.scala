package KafkaUse.sink

import KafkaUse.CommerceToKafka.Commerce
import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import utils.Utils.{WSL_IP, gson}

import java.util.Properties

object CommerceOutput extends OutputFormat[Commerce]{

  private var producer: KafkaProducer[String, String] = null
  private val topic = "commerce_info"

  override def configure(conf: Configuration): Unit = {
  }

  override def open(i: Int, i1: Int): Unit = {
    val props = new Properties()
    props.setProperty("bootstrap.servers", WSL_IP)
    props.setProperty("acks", "all")
    props.setProperty("topic", topic)
    props.setProperty("retries", "0")
    props.setProperty("batch.size", "100")
    props.setProperty("linger.ms", "1")
    props.setProperty("buffer.memory", "10240")
    props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    producer = new KafkaProducer[String, String](props)
  }

  override def writeRecord(commerce: Commerce): Unit = {
    producer.send(new ProducerRecord[String, String](topic, commerce.InvoiceDate, gson.toJson(commerce)))
  }

  override def close(): Unit = {
    producer.close()
  }
}
