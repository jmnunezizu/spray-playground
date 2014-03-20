package com.fortyfives.spray.metrics

import spray.json._
import com.codahale.metrics.{Gauge, Counter, Meter, Histogram, Timer}

trait MetricsJsonProtocol {

  implicit object GaugeJsonFormat extends RootJsonFormat[Gauge[_]] {

    def write(g: Gauge[_]) = {
      val gaugeValue = try {
        new JsString(g.getValue().toString)
      } catch {
        case throwable: Throwable => JsString("error reading gauge: " + throwable.getMessage())
      }

      JsObject(
        "type" -> JsString("gauge"),
        "value" -> gaugeValue
      )
    }

    def read(value : JsValue) = value match {
      case _ => deserializationError("not implemented yet")
    }

  }

  implicit object CounterJsonFormat extends RootJsonFormat[Counter] {

    def write(c: Counter) =
      JsObject(
        "type" -> JsString("counter"),
        "count" -> JsNumber(c.getCount().toString)
      )

    def read(value : JsValue) = value match {
      case _ => deserializationError("not implemented yet")
    }

  }

  implicit object HistogramJsonFormat extends RootJsonFormat[Histogram] {

    def write(h: Histogram) =
      JsObject(
        "min" -> JsNumber(h.getSnapshot.getMin),
        "max" -> JsNumber(h.getSnapshot.getMax),
        "mean" -> JsNumber(h.getSnapshot.getMean),
        "stdDev" -> JsNumber(h.getSnapshot.getStdDev),
        "count" -> JsNumber(h.getCount),
        "median" -> JsNumber(h.getSnapshot.getMedian),
        "p75" -> JsNumber(h.getSnapshot.get75thPercentile()),
        "p95" -> JsNumber(h.getSnapshot.get95thPercentile()),
        "p99" -> JsNumber(h.getSnapshot.get99thPercentile()),
        "p999" -> JsNumber(h.getSnapshot.get999thPercentile())
      )

    def read(value : JsValue) = value match {
      case _ => deserializationError("not implemented yet")
    }

  }

  implicit object MeterJsonFormat extends RootJsonFormat[Meter] {

    def write(m: Meter) =
      JsObject(
        "mean" -> JsNumber(m.getMeanRate),
        "count" -> JsNumber(m.getCount),
        "1MinuteRate" -> JsNumber(m.getOneMinuteRate),
        "5MinuteRate" -> JsNumber(m.getFiveMinuteRate),
        "15MinuteRate" -> JsNumber(m.getFifteenMinuteRate)
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("not implemented yet")
    }

  }

  implicit object TimerJsonFormat extends RootJsonFormat[Timer] {

    def write(t: Timer) =
      JsObject(
        "meter" -> JsObject(
          "mean" -> JsNumber(t.getMeanRate),
          "count" -> JsNumber(t.getCount),
          "1MinuteRate" -> JsNumber(t.getOneMinuteRate),
          "5MinuteRate" -> JsNumber(t.getFiveMinuteRate),
          "15MinuteRate" -> JsNumber(t.getFifteenMinuteRate)
        ),
        "histogram" -> JsObject(
          "min" -> JsNumber(t.getSnapshot.getMin),
          "max" -> JsNumber(t.getSnapshot.getMax),
          "mean" -> JsNumber(t.getSnapshot.getMean),
          "stdDev" -> JsNumber(t.getSnapshot.getStdDev),
          "count" -> JsNumber(t.getCount),
          "median" -> JsNumber(t.getSnapshot.getMedian),
          "p75" -> JsNumber(t.getSnapshot.get75thPercentile()),
          "p95" -> JsNumber(t.getSnapshot.get95thPercentile()),
          "p99" -> JsNumber(t.getSnapshot.get99thPercentile()),
          "p999" -> JsNumber(t.getSnapshot.get999thPercentile())
        )
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("not implemented yet")
    }

  }

}

object MetricsJsonProtocol extends DefaultJsonProtocol
