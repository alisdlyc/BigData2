package utils

import com.google.gson.Gson

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

object Utils {
  val WSL_IP = "172.26.154.168:9092"
  val gson = new Gson()
  val format = new SimpleDateFormat("dd/MM/yyyy HH:mm")

  def tranTimeToLong(time: String): Long = {
    val fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dt: Date = fm.parse(time)
    dt.getTime
  }

  def tranCommerceDatetime(rowTime: String): String = {
    var re = ""
    try {
      re = format.parse(rowTime.trim).getTime.toString
    } catch {
      case e: Exception => re = System.currentTimeMillis().toString
    }
    re
  }
}
