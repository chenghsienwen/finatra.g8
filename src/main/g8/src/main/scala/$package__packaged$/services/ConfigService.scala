package $package$.services

import com.twitter.inject.Logging
import javax.inject.{Inject, Singleton}
import com.typesafe.config.Config

@Singleton
class ConfigService @Inject()(
                               config: Config
                             ) extends Logging {
  val thingy      = config.getString("example.thingy")
  val thingy2     = config.getString("example.thingy2")
  val magicNumber = config.getInt("example.magicNumber")

  def listExamples(): Seq[String] =
    Seq(
      thingy,
      thingy2,
      magicNumber.toString
    )
}
