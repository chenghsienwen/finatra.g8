package $package$.controllers

import $package$.services.ConfigService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import javax.inject.{Inject, Singleton}

@Singleton
class ConfigController @Inject()(exampleService: ConfigService) extends Controller {
  get("/examples") { request: Request =>
    response.ok(exampleService.listExamples())
  }
}
