package $package$

import scala.language.implicitConversions

import java.net.{InetSocketAddress, SocketAddress}

/**
  * read assigned port number from production scheduler with fallback
  * {{{
  *   object app extends QuadasServer {
  *     val myServicePort = assignedPort("my-service", 8899)
  *   }
  * }}}
  *
  * how to assign ports
  *   - assigned by nomad (env `NOMAD_PORT_my-service`)
  *   - `java -Dport.my-service=9999`
  */
trait PortAssignment {
  self: com.twitter.app.App =>
  def assignedPort: AssignedPort = AssignedPort
}

trait IntToSocketAddress {
  self: com.twitter.app.App =>
  implicit def intToSocketAddress(i: Int): IntToInetSocketAddress = new IntToInetSocketAddress(i)
}

class IntToInetSocketAddress(private val i: Int) extends AnyVal {
  def anyLocalAddress: SocketAddress = new InetSocketAddress(i)
}

trait AssignedPort {
  def apply(name: String): Option[Int]
  def apply(name: String, default: => Int): Int
}

object AssignedPort extends AssignedPort {

  def apply(name: String): Option[Int] =
    sys.env
      .get(s"NOMAD_PORT_$"$"$name")
      .orElse(sys.props.get(s"port.$"$"$name"))
      .map(_.toInt)

  def apply(name: String, default: => Int): Int =
    apply(name).getOrElse(default)
}
