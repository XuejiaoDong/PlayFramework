package controllers

import javax.inject._
import play.api._
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{JsPath, Json, Writes}
import play.api.mvc._
import play.libs.Json.{mapper, toJson}
import play.mvc.Results.ok


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def explore() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.explore())
  }
  
  def tutorial() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.tutorial())
  }

  def hello = Action {
    Ok(views.html.hello())
  }

  case class User(id:Long, name: String)
  def returnJson = Action {
    val tt = User(122.toLong,"shahs")
    val ss = mapper.writeValueAsString(toJson(tt))
    Ok(views.html.test(ss))

  }

  case class Location(lat: Double, long: Double)

  case class Place(name: String, location: Location)

  object Place {
    var list: List[Place] = {
      List(
        Place(
          "Sandleford",
          Location(51.377797, -1.318965)
        ),
        Place(
          "Watership Down",
          Location(51.235685, -1.309197)
        )
      )
    }

    def save(place: Place) = {
      list = list ::: List(place)
    }
  }

  import play.api.mvc._

  class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {}

  implicit val locationWrites: Writes[Location] =
    (JsPath \ "lat").write[Double].and((JsPath \ "long").write[Double])(unlift(Location.unapply))

  implicit val placeWrites: Writes[Place] =
    (JsPath \ "name").write[String].and((JsPath \ "location").write[Location])(unlift(Place.unapply))

  def listPlaces = Action {
    val json = mapper.writeValueAsString(Json.toJson(Place.list))
    Ok(json)
  }

}
