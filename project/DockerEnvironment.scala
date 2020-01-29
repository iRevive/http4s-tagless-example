import sbt._
import scala.sys.process._

object DockerEnvironment {

  val DefaultNetwork: String = "http4s-tagless-example-network"
  val ComposeProject: String = "http4s-tagless-example".replace("-", "_")

  def createEnv(network: Option[String]): DockerEnv = new DockerEnv(network.getOrElse(DefaultNetwork))

  class DockerEnv(network: String) {

    def start(sourceDirectory: File): Unit = {
      val path = dockerComposePath(sourceDirectory)

      createNetwork()
      Process(s"docker-compose -f $path -p $ComposeProject up -d", None, dockerEnvVars: _*).!
    }

    def destroy(sourceDirectory: File): Unit = {
      val path = dockerComposePath(sourceDirectory)
      Process(s"docker-compose -f $path -p $ComposeProject down", None, dockerEnvVars: _*).!
    }

    def javaOpts: Seq[String] = {
      val postgreUri = foldNetwork(
        "jdbc:postgresql://localhost:55432/http4s-tagless-example",
        "jdbc:postgresql://postgres:5432/http4s-tagless-example"
      )

      val (postgreUser, postgrePassword) = ("postgres", "admin")

      Seq(
        s"-DPOSTGRESQL_URI=$postgreUri",
        s"-DPOSTGRESQL_USER=$postgreUser",
        s"-DPOSTGRESQL_PASSWORD=$postgrePassword"
      )
    }

    private def createNetwork(): Unit =
      s"docker network create $network".!

    private def dockerComposePath(sourceDirectory: File): File =
      sourceDirectory / "it" / "docker" / "docker-compose.yml"

    private def dockerEnvVars: Seq[(String, String)] = Seq(("NETWORK", network))

    private def foldNetwork[A](onDefault: => A, onExternal: => A): A =
      if (network == DefaultNetwork) onDefault else onExternal

  }

}