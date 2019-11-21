package $package$

import $package$.modules._
import $package$.controllers._
import $package$.filters._
import $package$.util.AppConfigLib._
import $package$.util.PipeOperator._
import com.jakehschwartz.finatra.swagger.DocsController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.util.Var

object ServerMain extends Server

class Server extends HttpServer {
  val health = Var("good")

  implicit lazy val scheduler: SchedulerService = Scheduler.io("$package$")

  override protected def modules = Seq(ServiceSwaggerModule, FinatraTypesafeConfigModule)

  override def defaultHttpPort = getConfig[String]("FINATRA_HTTP_PORT").fold(":9999")(x => p":$"$"$x")
  override val name            = "$package$-$name;format="Camel"$"

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[DocsController]
      .add[AdminController]
      .add[MainController]
      .add[BasicAuthFilter, ConfigController]
      .|>(_ => ())
  }
}
