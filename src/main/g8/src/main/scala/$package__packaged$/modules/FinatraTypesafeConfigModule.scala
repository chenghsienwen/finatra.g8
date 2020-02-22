package $package$.modules

import java.io.File
import com.google.inject.Provides
import $package$.util.PipeOperator._
import com.twitter.inject.{Logging, TwitterModule}
import com.typesafe.config.{Config, ConfigFactory}
import javax.inject.Singleton

object FinatraTypesafeConfigModule extends TwitterModule with Logging {
  val configurationFile = flag("config.file", "", "Optional config file to override settings")

  @Provides
  @Singleton
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
