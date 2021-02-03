package $package$.modules

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.twitter.finatra.jackson.modules.ScalaObjectMapperModule
object CustomJacksonModule extends ScalaObjectMapperModule {

  override val propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
}
