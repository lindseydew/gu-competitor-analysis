package controllers

import play.api._
import play.api.mvc._
import models.{NewsItems, NewsItem}
import play.api.libs.json._

case class ParsedItem(height: Int, width: Int, top: Int, left: Int, linkText: String, url: String)

object ParsedItem  {
  import _root_.play.api.libs.functional.syntax._
  implicit val reads: Reads[ParsedItem]  = (
      (__ \"height").read[Int] ~
        (__ \"width").read[Int] ~
        (__ \"top").read[Int] ~
        (__ \"left").read[Int] ~
        (__ \"linkText").read[String] ~
        (__ \"url").read[String]
      )(ParsedItem.apply _)
}


object Application extends Controller {

  def index = Action {
    val items = NewsItems.all
    Ok(views.html.index(items))
  }
  def insert(source: String) = Action { implicit request =>
    val json = request.body.asJson
    json match {
      case Some(JsArray(items)) =>{
        items.map(_.as[ParsedItem])
      }
      case _ => println("couldn't parse json")
    }
    Ok
  }
  
}