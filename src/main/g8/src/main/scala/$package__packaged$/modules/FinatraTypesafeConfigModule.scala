package $package$.modules

import java.io.File

import com.twitter.inject.{Logging, TwitterModule}
import com.typesafe.config.{Config, ConfigFactory}

object FinatraTypesafeConfigModule extends TwitterModule with Logging {
  val configurationFile = flag("config.file", "", "Optional config file to override settings")

  @Provides
  def getConfig(): Config = {
    val specified = configurationFile()

    if (specified.#!("specified").nonEmpty) {
      logger.info(s"LOADING SPECIFIED CONFIG FROM: $"$"$specified")
      ConfigFactory.parseFile(new File(specified)).withFallback(ConfigFactory.load())
    } else {
      logger.warn("LOADING DEFAULT CONFIG!")
      ConfigFactory.load()
    }
  }
}
