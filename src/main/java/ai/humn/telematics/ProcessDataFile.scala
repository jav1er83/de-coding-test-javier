package ai.humn.telematics

import ai.humn.telematics.model.Journey
import ai.humn.telematics.parsing.JourneyFileParser

import scala.io.Source

/**
 * Version of ProcessDataFile optimized for processing Big files:
 * We do only one pass, line by line, on the contents of the file, avoiding to load it completely into memory.
 * We only keep in memory strictly necessary info for the aggregations: journeys longer than 90 minutes, and driver distances.
 *
 * This performance improvement is done at the cost of a somewhat less clean/tidy code
 */
object ProcessDataFile {

  val LONG_JOURNEY_DURATION = 90 * 60 * 1000 // 90 minutes

  def main(args: Array[String]): Unit = {

    val inputPath = parseCmdParams(args)

    val source = Source.fromFile(inputPath)
    try{
      val journeys = JourneyFileParser.parseJourneySet(source)
      printStats(journeys)
    }
    finally {
      source.close()
    }
  }

  def parseCmdParams(args: Array[String]): String = {
    if (args.length == 0) {
      println("Please, specify an input path to process. Example: /path/to/2021-10-05_journeys.csv")
      System.exit(0)
    }
    args(0)
  }

  def printStats(journeys: Iterator[Journey]) {
    var seenJourneyIds = Set[String]()
    var driverDistances = Map[String, Double]()
    var longJourneys = Set[Journey]()

    println("Average speeds in Kph")
    for (journey <- journeys) {
      if (!seenJourneyIds.contains(journey.journeyId)) { // This avoids processing duplicate journeys
        seenJourneyIds += journey.journeyId
        println(journey.summary)

        driverDistances = updateDriverDistance(driverDistances, journey)

        if (journey.duration > LONG_JOURNEY_DURATION) longJourneys += journey
      }
    }
    println

    println("Journeys of 90 minutes or more.")
    for (journey <- longJourneys) {
      println(journey.summary)
    }
    println

    println("Mileage By Driver")
    for ((driverId, distance) <- driverDistances) {
      val distanceInt = distance.toInt
      println(driverId+" drove "+ f"$distanceInt%4s"+" kilometers")
    }
    println

    val mostActiveDriver = computeMostActiveDriver(driverDistances)
    mostActiveDriver match {
      case Some(x) => println("Most active driver is "+x._1)
      case None => print("No active driver could be extracted from the dataset")
    }
    println
  }

  def updateDriverDistance(driverDistances: Map[String, Double], journey: Journey): Map[String, Double] = {
    if (driverDistances.contains(journey.driverId)) {
      val newDistance = driverDistances(journey.driverId) + journey.distance
      driverDistances + (journey.driverId -> newDistance)
    }
    else {
      driverDistances + (journey.driverId -> journey.distance)
    }
  }

  def computeMostActiveDriver(driverDistances: Map[String, Double]): Option[(String, Double)] = {
    // This is a way of obtaining the (key, value) of a Map that has the maximum value: (https://stackoverflow.com/a/39713197/437012)
    if (driverDistances.isEmpty) None else Some(driverDistances.maxBy(_._2))
  }
}
