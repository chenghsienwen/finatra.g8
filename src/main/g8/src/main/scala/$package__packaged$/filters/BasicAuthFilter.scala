package $package$.filters

import java.nio.charset.StandardCharsets
import java.util.Base64

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.inject.Logging
import com.twitter.util.Future
import javax.inject.Inject

class BasicAuthFilter @Inject()(
                                 responseBuilder: ResponseBuilder,
                                 config: Config
                               ) extends SimpleFilter[Request, Response]
  with Logging {

  def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    val authPass = config.getString("auth.pass")
    val authUser = config.getString("auth.user")
    logger.info(s">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> User: $"$"$authUser Pass: $"$"$authPass")
    if (authUser.nonEmpty) {
      val authHeader: Option[String] = request.headerMap.get("Authorization")
      authHeader.map { value =>
        val base64Creds: String = value.substring("Basic".length()).trim()
        val creds               = new String(Base64.getDecoder.decode(base64Creds), StandardCharsets.UTF_8)
        val split               = creds.split(":", 2)

        val user = split(0)
        val pass = split(1)

        if (user == authUser && pass == authPass) {
          service(request)
        } else {
          responseBuilder.unauthorized(s"Invalid Credentials!").toFuture
        }
      } getOrElse {
        responseBuilder
          .unauthorized("Authentication required!")
          .header("WWW-Authenticate", "Basic realm=\"jenkins\"")
          .toFuture
      }
    } else {
      //The authUser is empty, and so we're going to just do the thing
      service(request)
    }
  }
}
