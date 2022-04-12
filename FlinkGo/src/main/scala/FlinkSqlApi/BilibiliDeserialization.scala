package FlinkSqlApi

import com.jsoniter.JsonIterator
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema
import org.apache.kafka.clients.consumer.ConsumerRecord
import pojo.DanmuBean

case class DanMu(timestamp: String, uuid: String, content: String)

class BilibiliDeserialization extends KafkaDeserializationSchema[DanmuBean]{
  override def isEndOfStream(t: DanmuBean): Boolean = ???

  override def deserialize(consumerRecord: ConsumerRecord[Array[Byte], Array[Byte]]): DanmuBean = ???

  override def getProducedType: TypeInformation[DanmuBean] = ???
}
