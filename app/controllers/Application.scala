package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.libs.json._
import models.Position
import play.api.libs.json.JsArray
import models.NewsItem
import models.Source
import scala.Some
import scala.Predef._
import models.Position
import play.api.libs.json.JsArray
import models.NewsItem
import models.Source
import scala.Some

case class ParsedItem(height: Int, width: Int, top: Int, left: Int, linkText: String, url: String)

object ParsedItem  {
  import _root_.play.api.libs.functional.syntax._
  implicit val reads: Reads[ParsedItem]  = (
      (__ \"height").read[Int] ~
        (__ \"width").read[Int] ~
        (__ \"top").read[Int] ~
        (__ \"left").read[Int] ~
        (__ \"headline").read[String] ~
        (__ \"url").read[String]
      )(ParsedItem.apply _)
}

object Application extends Controller {
  var info: Map[String, Source] = Map[String, Source]()
  def index = Action {
    for(things<-info.values.toList;
                       item<-things.items) {
        item.copy(linkTo= matchingNewsItem(item.entities))
    }
    Ok(views.html.index(info.values.toList))
  }

  def matchingNewsItem(entities: List[String]): List[NewsItem] = {
    val matching = for(things<-info.values.toList;
        item<-things.items;
        if(!entities.intersect(item.entities).isEmpty && !entities.diff(item.entities).isEmpty)
    ) yield item
    matching
  }

  def insert(source: String) = Action { implicit request =>
    val json = request.body.asJson
    json match {
      case Some(JsArray(items)) =>{
        val data = items.map(_.as[ParsedItem])
        val sourcesData = Source(source,
          data.map(d =>
            NewsItem(d.linkText, d.url, Story.processStory(d.linkText),
            Position(d.height, d.width, d.top, d.left))
          ).toList.sortBy(-_.position.score)
        )
        info = info.updated(source, sourcesData)
      }
      case _ => println("couldn't parse json")
    }
    Ok
  }

}