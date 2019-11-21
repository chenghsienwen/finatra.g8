package is.kow.finatratypesafe.modules

import java.io.File

import com.github.racc.tscg.TypesafeConfigModule
import com.twitter.inject.{Logging, TwitterModule}
import com.typesafe.config.{Config, ConfigFactory}

object FinatraTypesafeConfigModule extends TwitterModule with Logging {
  val configurationFile = flag("config.file", "", "Optional config file to override settings")

  override def configure() = {
    val specified = configurationFile()

    val config = if (specified.nonEmpty) {
      logger.info(s"LOADING SPECIFIED CONFIG FROM: $specified")
      ConfigFactory.parseFile(new File(specified)).withFallback(ConfigFactory.load())
    } else {
      logger.warn("LOADING DEFAULT CONFIG!")
      ConfigFactory.load()
    }

    //Lets validate that some config exists
    logger.info(s"Thingy: ${config.getString("example.thingy")}")
    logger.info(s"Thingy2: ${config.getString("example.thingy2")}")

    install(TypesafeConfigModule.fromConfig(config))
  }
}
