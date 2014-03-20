package com.fortyfives.spray.metrics

import com.fortyfives.spray.Boot

trait Instrumented extends nl.grons.metrics.scala.InstrumentedBuilder {

  val metricRegistry = Boot.metricRegistry

}
