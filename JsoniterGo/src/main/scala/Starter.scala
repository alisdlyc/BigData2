import com.jsoniter.JsonIterator
import com.jsoniter.output.JsonStream
import pojo.{Student, StudentPojo}

object Starter {
  case class student(name: String, age: Int)

  def main(args: Array[String]): Unit = {
    val s1: student = student("qwq", 18)
    val str: String = JsonStream.serialize(s1)
    println(str)

    val student1: Student = JsonIterator.deserialize(str, new Student().getClass)
    val student2: student = JsonIterator.deserialize(str, student("", 1).getClass)
    println(student1.toString)
    println(student2.toString)
  }
}
