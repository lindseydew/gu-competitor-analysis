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

        val maxTop = data.map(_.top).max
        val newList = data.map(d =>
          NewsItem(d.linkText, d.url,  Story.processStory(d.linkText),Position(d.height, d.width, d.left, d.top, maxTop))).toList.sortBy(- _.position.score)

        def generateDelta(old: List[NewsItem], replacement: List[NewsItem]) = {
          replacement.zipWithIndex.map{case(key, i) =>
            val oldIndex = old.map(_.url).indexOf(key.url)

            if (oldIndex == -1) {
              NewsItem(key.headline, key.url, key.entities, key.position, "new")
            } else if (i < oldIndex ){
              NewsItem(key.headline, key.url, key.entities, key.position, (oldIndex - i).toString)
            } else if (oldIndex < i) {
              NewsItem(key.headline, key.url, key.entities, key.position, (oldIndex - i).toString)
            } else {
              NewsItem(key.headline, key.url, key.entities, key.position, "0")
            }
          }
        }


        // This is horrible
        val oldList = info.getOrElse(source, Source(source, List())).items
        val sourcesData = Source(source, generateDelta(oldList, newList))

        info = info.updated(source, sourcesData)
      }
      case _ => println("couldn't parse json")
    }
    Ok
  }

}