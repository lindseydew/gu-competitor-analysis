package models

case class Position(width: Int, height: Int, xOffset: Int, yOffset: Int) {
  lazy val score = width*height

}

case class Source(name: String, items: List[NewsItem])

case class NewsItem(headline: String, url: String, entities: List[String], position: Position, linkTo: List[NewsItem]=Nil)

case class Entities(headline: String, entities: List[String])

object Story {
  val excludedWords = List("A", "The", "On", "And")
  def processStory(headline: String): List[String] = {
    val words = headline.split(" ")
    (for(word<-words; if(word.head.isUpper && !excludedWords.contains(word))) yield word).toList
  }
}

object NewsItems {
  var info: Map[String, Source] = Map[String, Source]()
}