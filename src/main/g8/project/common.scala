package com.quadas.sbt

import sbt._
import Keys._

object Settings {
  val nameOfBranch = settingKey[String]("Define name concated with branch abbreviation")
  val buildNumber = settingKey[String]("Build number")

  val dockerPublishRepo = settingKey[Option[String]]("Docker registry to publish")
  val dockerPublishUser = settingKey[Option[String]]("User name of Docker registry")
}