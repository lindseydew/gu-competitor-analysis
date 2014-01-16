package models

case class Position(width: Int, height: Int, xOffset: Int, yOffset: Int) {
  lazy val score = width*height/(yOffset + 1)

}

case class Sources(name: String, items: List[NewsItem])
case class NewsItem(headline: String, url: String, position: Position)

object NewsItems {
  var info: Map[String, Sources] = Map[String, Sources]()

  def all = List(Sources("the guardian",List(
                NewsItem("NSA spying on us again", "http://www.gu.com/2134",
                         Position(380, 480, 0, 0)),
                NewsItem("NSA spying on us again", "http://www.gu.com/2134",
                        Position(120, 60, 70, 100)),
                NewsItem("NSA spying on us again", "http://www.gu.com/2134",
                        Position(30, 30, 500, 300))
                ).sortBy(_.position.score)),
                Sources("daily mail", List(
                  NewsItem("BOOBS", "http://dailymail.CO.UK/boobs", Position(10000, 10000, 1, 3)))
                  )
                )
}