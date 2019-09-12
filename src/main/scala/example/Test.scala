package example

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.directives.RouteDirectives
import akka.stream._

import scala.concurrent.Future

object TestRSTApp extends App {
  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val mat = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case _: RuntimeException =>
      complete(StatusCodes.Conflict)
  }

  val route =
    path("data") {
      handleExceptions(exceptionHandler) {
        put {
          extractRequest { request =>
            discardEntityBytes(request.entity)
            val response: Future[Done] = Future.failed(new RuntimeException("It is not allowed to overwrite an existing object"))
            complete(response)
          }
        }
      }
    }

  final def discardEntityBytes(entity: HttpEntity)
                              (implicit materializer: Materializer): Future[Done] =
    entity match {
      case _: HttpEntity.Strict  =>
        Future.successful(Done)

      case e if e.isKnownEmpty() =>
        Future.successful(Done)

      case e                     =>
        e.discardBytes().future()
    }
}