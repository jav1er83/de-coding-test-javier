package ai.humn.telematics

import ai.humn.telematics.model.JourneySet
import ai.humn.telematics.parsing.JourneyFileParser

import scala.io.Source

object ProcessDataFile {

  val LONG_JOURNEY_DURATION = 90 * 60 * 1000 // 90 minutes

  def main(args: Array[String]): Unit = {

    val inputPath = parseCmdParams(args)

    val source = Source.fromFile(inputPath)
    try{
      val journeySet = JourneyFileParser.parseJourneys(source)

      printLongJourneys(journeySet)

      printAvgSpeeds(journeySet)

      printDriverMileages(journeySet)

      printMostActiveDriver(journeySet)
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

  def printLongJourneys(journeySet: JourneySet)  {
    println("Journeys of 90 minutes or more.")
    for (journey <- journeySet.journeys) {
      if (journey.durationMs > LONG_JOURNEY_DURATION) println(journey.summary)
    }
    println
  }

  def printAvgSpeeds(journeySet: JourneySet) {
    println("Average speeds in Kph")
    for (journey <- journeySet.journeys) {
      println(journey.summary)
    }
    println
  }

  def printDriverMileages(journeySet: JourneySet) {

    println("Mileage By Driver")
    for ((driverId, distance) <- journeySet.mileageByDriver) {
      val distanceInt = distance.toInt
      println(driverId+" drove "+ f"$distanceInt%4s"+" kilometers")
    }
    println
  }

  def printMostActiveDriver(journeySet: JourneySet) {
    val mostActiveDriverInfo: Option[(String, Double)] = journeySet.mostActiveDriver
    mostActiveDriverInfo match {
      case Some(x) => println("Most active driver is "+x._1)
      case None => print("No active driver could be extracted from the dataset")
    }
    println
  }
}
