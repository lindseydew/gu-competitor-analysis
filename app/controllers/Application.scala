package controllers

import play.api._
import play.api.mvc._
import models.{Position, Sources, NewsItems, NewsItem}
import play.api.libs.json._

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

  var info: Map[String, Sources] = Map[String, Sources]()
  def index = Action {
    Ok(views.html.index(info.values.toList))
  }
  def insert(source: String) = Action { implicit request =>
    println(s"receive $source")
    val json = request.body.asJson
    json match {
      case Some(JsArray(items)) =>{
        val data = items.map(_.as[ParsedItem])

        val newList = data.map(d =>
          NewsItem(d.linkText, d.url, Position(d.height, d.width, d.top, d.left))).toList.sortBy(-_.position.score)

        def generateDelta(old: List[NewsItem], replacement: List[NewsItem]) = {
          replacement.zipWithIndex.map{case(key, i) =>
            val oldIndex = old.indexOf(key)

            if (oldIndex == -1) {
              NewsItem(key.headline, key.url, key.position, "new!")
            } else if (i < oldIndex ){
              NewsItem(key.headline, key.url, key.position, "+"+(oldIndex - i))
            } else if (oldIndex < i) {
              NewsItem(key.headline, key.url, key.position, (oldIndex - i).toString)
            } else {
              NewsItem(key.headline, key.url, key.position, "<->")
            }
          }
        }


        // This is horrible
        val oldList = info.getOrElse(source, Sources(source, List())).items
        val sourcesData = Sources(source, generateDelta(oldList, newList))



        info = info.updated(source, sourcesData)
        println(s"$source saved")
      }
      case _ => println("couldn't parse json")
    }
    Ok
  }


  
}