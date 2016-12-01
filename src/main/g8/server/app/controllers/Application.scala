package controllers

import javax.inject._

import play.api.{Environment, Mode}
import play.api.mvc._

@Singleton
class Application @Inject()(env: Environment) extends Controller {

  private val isProd: Boolean = if(env.mode == Mode.Prod) true else false

  def index = Action {
    Ok(views.html.index(isProd))
  }

}
