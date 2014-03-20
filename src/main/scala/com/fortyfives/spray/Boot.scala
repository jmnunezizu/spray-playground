package com.fortyfives.spray

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import com.fortyfives.spray.metrics.service.MetricServiceActor

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // the application wide metrics registry
  val metricRegistry = new com.codahale.metrics.MetricRegistry()

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "demo-service")
  val metricService = system.actorOf(Props[MetricServiceActor], "metric-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8090)
  IO(Http) ! Http.Bind(metricService, interface = "localhost", port = 8091)

}