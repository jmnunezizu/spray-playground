package com.fortyfives.spray.metrics.service

import spray.routing.HttpService
import akka.actor.Actor
import com.fortyfives.spray.metrics.{MetricsJsonProtocol, Instrumented}
import scala.collection.JavaConversions._
import com.codahale.metrics._
import spray.json._
import spray.httpx.SprayJsonSupport

class MetricServiceActor extends Actor with MetricService {

  def actorRefFactory = context

  def receive = runRoute(routes)

}

trait MetricService extends HttpService with SprayJsonSupport with MetricsJsonProtocol with Instrumented {

  metrics.timer("test")

  val routes = {
    path("metrics") {
      get {
        val metrics: Map[String, JsValue] = for {
          (metricName, metric) <- metricRegistry.getMetrics().toMap
        } yield (metricName, writeMetric(metric))

        complete(metrics)
      }
    }
  }

  def writeMetric(metric: Metric) = {

    metric match {
      //case gaugeMetric: Gauge[_] => gaugeMetric.toJson
      case counterMetric: Counter => counterMetric.toJson
      case histogramMetric: Histogram => histogramMetric.toJson
      case meterMetric: Meter => meterMetric.toJson
      case timerMetric: Timer => timerMetric.toJson
    }
  }

}
