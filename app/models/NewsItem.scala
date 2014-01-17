package models

case class Position(width: Int, height: Int, xOffset: Int, yOffset: Int, maxOffset: Int) {
  lazy val score = {
    // percentage of the vertical offset, 1 at the top and 0 at the bottom
    val verticalPenalty = (1 - (yOffset.toDouble / maxOffset.toDouble))
    (width + height) * verticalPenalty * verticalPenalty * verticalPenalty
    //math.sqrt(width * height) * verticalPenalty *
 }

}

case class Source(name: String, items: List[NewsItem])

case class NewsItem(headline: String, url: String, entities: List[String], position: Position, movement: String = "new",  linkTo: List[NewsItem]=Nil) {

  def indicator = {
    if (movement == "new") { "new" }
    else if (movement.toInt < 0) { "down" }
    else if (movement.toInt > 0) { "up" }
    else { "nomovement" }
  }

  lazy val shortenedHeadline = {
    val maxHeadlineLen = 100
    if (headline.length > maxHeadlineLen) {
      headline.substring(0, maxHeadlineLen - 3) + "..."
    } else {
      headline
    }
  }
}


case class Entities(headline: String, entities: List[String])

object Story {
  val excludedWords = List("A", "The", "On", "And", "Why")
  def processStory(headline: String): List[String] = {
    val words = headline.split(" ")
    (for(word<-words; c<-word; if(c.isUpper && !excludedWords.contains(word))) yield word).toList
  }
}

object NewsItems {
  var info: Map[String, Source] = Map[String, Source]()

}