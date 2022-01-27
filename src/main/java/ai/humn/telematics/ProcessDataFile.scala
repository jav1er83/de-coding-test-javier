package ai.humn.telematics

import scala.io.Source

object ProcessDataFile {

  def main(args: Array[String]): Unit = {

    val inputPath = parseCmdParams(args)

    // Parse input CSV file into a JourneySet:
    val source = Source.fromFile(inputPath)
    val journeySet = JourneyFileParser.parseJourneys(source)

    // 1. Find journeys that are 90 minutes or more.
    printLongJourneys(journeySet)

    // 2. Find the average speed per journey in kph.
    printAvgSpeeds(journeySet)

    // 3. Find the total mileage by driver for the whole day.
    printDriverMileages(journeySet)

    // 4. Find the most active driver - the driver who has driven the most kilometers.
    printMostActiveDriver(journeySet)

    // Close the input file
    source.close()
  }

  def parseCmdParams(args: Array[String]): String = {
    if (args.length == 0) {
      println("Please, specify an input path to process. Example: /path/to/2021-10-05_journeys.csv")
      System.exit(0)
    }
    args(0)
  }

  def printLongJourneys(journeySet: JourneySet)  {
    println("Journeys longer than 90 minutes:")
    for (journey <- journeySet.journeys) {
      val ninetyMins = 90 * 60 * 1000
      if (journey.duration > ninetyMins) println(journey)
    }
    println
  }

  def printAvgSpeeds(journeySet: JourneySet) {
    println("Average speed per (valid) journey:")
    for (j <- journeySet.journeys) {
      val journeySummary = "journeyId: "+j.journeyId+" "+j.driverId+" distance "+j.distance+" durationMS "+j.duration+" avgSpeed in kph was "+j.avgSpeed
      println(journeySummary)
    }
    println
  }

  def printDriverMileages(journeySet: JourneySet) {

    println("Mileage By Driver")
    for ((driverId, distance) <- journeySet.mileageByDriver) {
      println(driverId+" drove "+ distance+" kilometers")
    }
    println
  }

  def printMostActiveDriver(journeySet: JourneySet) {
    val (mostActiveDriverId, _) = journeySet.mostActiveDriver
    println("Most active driver is "+mostActiveDriverId)
    println
  }
}
