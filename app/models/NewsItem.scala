package models

case class Position(width: Int, height: Int, xOffset: Int, yOffset: Int, maxOffset: Int) {
  lazy val score = {
    // percentage of the vertical offset, 1 at the top and 0 at the bottom
    val verticalPenalty = (1 - (yOffset.toDouble / maxOffset.toDouble))
    width * height * verticalPenalty * verticalPenalty
  }

}

case class Sources(name: String, items: List[NewsItem])

case class NewsItem(headline: String, url: String, position: Position, movement: String = "new") {

  def indicator = {
    if (movement == "new") { "new" }
    else if (movement.toInt < 0) { "down" }
    else if (movement.toInt > 0) { "up" }
    else { "nomovement" }
  }
}

object NewsItems {
  var info: Map[String, Sources] = Map[String, Sources]()

  def all = List(Sources("the guardian", List(
    NewsItem("NSA spying on us again", "http://www.gu.com/2134",
      Position(380, 480, 0, 0, 300)),
    NewsItem("NSA spying on us again", "http://www.gu.com/2134",
      Position(120, 60, 70, 100, 300)),
    NewsItem("NSA spying on us again", "http://www.gu.com/2134",
      Position(30, 30, 500, 300, 300))
  ).sortBy(_.position.score)),
    Sources("daily mail", List(
      NewsItem("BOOBS", "http://dailymail.CO.UK/boobs", Position(10000, 10000, 1, 3, 3)))
    )
  )
}