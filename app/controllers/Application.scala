package controllers

import play.api._
import play.api.mvc._
import models.{NewsItems, NewsItem}

object Application extends Controller {
  
  def index = Action {

    Ok("hello world")
  }

  def insert = Action { implicit request =>
    println(request.body.asJson)
    Ok
  }
  
}