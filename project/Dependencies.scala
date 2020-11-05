import sbt._

object Settings {
  val organization = "com.example"
  val name         = "http4s-tagless-example"
}

object Versions {
  val scala               = "2.13.3"
  val http4s              = "0.21.8"
  val circe               = "0.13.0"
  val circeExtras         = "0.13.0"
  val circeConfig         = "0.8.0"
  val cats                = "2.2.0"
  val catsEffect          = "2.2.0"
  val catsMTL             = "1.0.0"
  val catsRetry           = "2.0.0"
  val monix               = "3.2.2"
  val sup                 = "0.8.0"
  val doobie              = "0.9.2"
  val flyway              = "7.1.1"
  val newtype             = "0.4.4"
  val refined             = "0.9.17"
  val magnolia            = "0.17.0"
  val odin                = "0.9.1"
  val logback             = "1.2.3"
  val scalatest           = "3.2.2"
  val scalatestScalacheck = "3.2.2.0"
  val catsScalatest       = "3.0.8"
  val catsEffectScalaTest = "0.4.1"
  val scalacheck          = "1.15.0"
  val scalazDeriving      = "2.0.0-M6"
  val kindProjector       = "0.11.0"
  val betterMonadicFor    = "0.3.1"
}

object Dependencies {

  val CatsEffectScalaTest = "cats-effect-testing-scalatest-scalacheck"

  val root: Seq[ModuleID] = Seq(
    "org.http4s"           %% "http4s-dsl"           % Versions.http4s,
    "org.http4s"           %% "http4s-blaze-server"  % Versions.http4s,
    "org.http4s"           %% "http4s-circe"         % Versions.http4s,
    "io.monix"             %% "monix"                % Versions.monix,
    "org.typelevel"        %% "cats-core"            % Versions.cats,
    "org.typelevel"        %% "cats-effect"          % Versions.catsEffect,
    "org.typelevel"        %% "cats-mtl"             % Versions.catsMTL,
    "com.github.cb372"     %% "cats-retry-mtl"       % Versions.catsRetry,
    "org.scalaz"           %% "deriving-macro"       % Versions.scalazDeriving,
    "io.circe"             %% "circe-generic"        % Versions.circe,
    "io.circe"             %% "circe-refined"        % Versions.circe,
    "io.circe"             %% "circe-generic-extras" % Versions.circeExtras,
    "io.circe"             %% "circe-config"         % Versions.circeConfig,
    "io.estatico"          %% "newtype"              % Versions.newtype,
    "eu.timepit"           %% "refined"              % Versions.refined,
    "eu.timepit"           %% "refined-cats"         % Versions.refined,
    "com.kubukoz"          %% "sup-doobie"           % Versions.sup,
    "com.kubukoz"          %% "sup-http4s"           % Versions.sup,
    "com.kubukoz"          %% "sup-circe"            % Versions.sup,
    "org.tpolecat"         %% "doobie-hikari"        % Versions.doobie,
    "org.tpolecat"         %% "doobie-refined"       % Versions.doobie,
    "org.tpolecat"         %% "doobie-postgres"      % Versions.doobie,
    "org.flywaydb"          % "flyway-core"          % Versions.flyway,
    "com.propensive"       %% "magnolia"             % Versions.magnolia,
    "com.github.valskalla" %% "odin-extras"          % Versions.odin,
    "ch.qos.logback"        % "logback-classic"      % Versions.logback,
    "org.tpolecat"         %% "doobie-scalatest"     % Versions.doobie              % "it",
    "org.scalatest"        %% "scalatest"            % Versions.scalatest           % "it,test",
    "org.scalatestplus"    %% "scalacheck-1-14"      % Versions.scalatestScalacheck % "it,test",
    "com.ironcorelabs"     %% "cats-scalatest"       % Versions.catsScalatest       % "it,test",
    "com.codecommit"       %% CatsEffectScalaTest    % Versions.catsEffectScalaTest % "it,test",
    "org.scalacheck"       %% "scalacheck"           % Versions.scalacheck          % "it,test",
    "eu.timepit"           %% "refined-scalacheck"   % Versions.refined             % "it,test"
  )

}
