package utils

import KafkaUse.CommerceToKafka
import KafkaUse.CommerceToKafka.Commerce
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import pojo.CommercePojo

import java.text.SimpleDateFormat
import java.util.Date

object UtilsDemo {
  @Test
  def Obj2Json(): Unit = {
    val commerce: Commerce = Commerce("536365","85123A","WHITE HANGING HEART T-LIGHT HOLDER","6","12/1/2010 8:26","2.55","17850","United Kingdom")
    val gson = new Gson()
    val str = gson.toJson(commerce)
    println("data row: " + commerce.InvoiceDate)
    val pattern = "dd/MM/yyyy HH:mm"
    val format = new SimpleDateFormat(pattern)
    val dateTime: Date = format.parse(commerce.InvoiceDate)
    println(dateTime)
    println(dateTime.getTime)
//    gson.fromJson(str, CommercePojo)
    println()
  }
}
