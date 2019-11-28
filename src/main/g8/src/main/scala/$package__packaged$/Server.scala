package $package$

import $package$.modules._
import $package$.controllers._
import $package$.filters._
import $package$.AssignedPort._
import $package$.util.AppConfigLib._
import $package$.util.PipeOperator._
import com.jakehschwartz.finatra.swagger.DocsController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.util.Var
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService
import perfolation._

object ServerMain extends Server

class Server extends HttpServer with PortAssignment {
  val health = Var("good")
  val dtabFile = flag("dtab.file", "", "dtab file to override settings")

  implicit lazy val scheduler: SchedulerService = Scheduler.io("$package$")

  override protected def modules = Seq(ServiceSwaggerModule, FinatraTypesafeConfigModule)
  
  override val name             = "$name;format="Camel"$"
  override def defaultHttpPort  = assignedPort(name).fold(":9999")(x => p":$"$"$x")
  override def defaultAdminPort = assignedPort("admin").getOrElse(9990)


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
