package models

case class Position(width: Int, height: Int, xOffset: Int, yOffset: Int) {
  lazy val score = width*height - xOffset - yOffset

}

case class Sources(name: String, items: NewsItem)
case class NewsItem(headline: String, url: String, position: Position)

object NewsItems {
//  def all = List(
//            NewsItem("theguardian.com", "NSA spying on us again", "http://www.gu.com/2134",
//            "here we go again", Position(380, 480, 0, 0)),
//            NewsItem("theguardian.com", "NSA spying on us again", "http://www.gu.com/2134",
//            "here we go again", Position(120, 60, 70, 100)),
//            NewsItem("theguardian.com", "NSA spying on us again", "http://www.gu.com/2134",
//             "here we go again", Position(30, 30, 500, 300))
//          ).sortBy(_.position.score)


}