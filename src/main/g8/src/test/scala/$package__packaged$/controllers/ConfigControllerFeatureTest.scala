package $package$.controllers

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Base64
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.http.Status._
import $package$.Server

/*sbt 'testOnly $package$.controllers.ConfigControllerFeatureTest' */
class ConfigControllerFeatureTest extends FeatureTest {

  override def afterAll() =
    server.close()

  //Create a new configuration file, and use the data from here, and then barf it out to a file that I can pass in
  def testConfig: String =
    s"""
       |example {
       |thingy: test1
       |thingy2: "test2"
       |magicNumber: 4321
       |}
       |""".stripMargin

  //Write the test config out to a test file
  val tempFilePath = Files.createTempFile("exampleConfig", ".config")
  Files.write(tempFilePath, testConfig.getBytes(StandardCharsets.UTF_8))

  //Cat out the temp file?

  val server = new EmbeddedHttpServer(
    new Server,
    flags = Map(
      "config.file" -> tempFilePath.toString
    )
  )

  val authData = s"Basic ${new String(
    Base64.getEncoder
      .encode("requiredUser:requiredPass".getBytes(StandardCharsets.UTF_8))
  )}"

  test("It should be requiring authentication by default") {
    server.httpGet(
      path      = "/examples",
      andExpect = Unauthorized
    )
  }

  test("Gets the example strings as configured") {
    server.httpGet(
      path         = "/examples",
      andExpect    = Ok,
      headers      = Map("Authorization" -> authData),
      withJsonBody = """
                       |[ "test1", "test2", "4321"]
        """.stripMargin
    )
  }
}
