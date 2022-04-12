package demo

import scala.io.{BufferedSource, Source}

object ReadFile extends App {
  lazy val path = "C:\\Users\\alisdlyc\\IdeaProjects\\BigData\\JsoniterGo\\src\\main\\resources\\data.txt"
  private val source: BufferedSource = Source.fromFile(path, "UTF-8")
  private val strings: Iterator[String] = source.getLines()
  for(line <- strings) {
    println(line)
  }

  private val inputSource: BufferedSource = Source.fromFile(path)
  println(inputSource.getLines().toArray.mkString)
}
