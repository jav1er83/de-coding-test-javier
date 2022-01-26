package ai.humn.telematics

import scala.io.Source

object ProcessDataFile {

  def main(args: Array[String]): Unit = {

    // Parse input CSV file into a list of Journeys
    val inputPath = args(0)
    val journeySet = parseJourneys(inputPath)

    // 1. Find journeys that are 90 minutes or more.
    printLongJourneys(journeySet)

    // 2. Find the average speed per journey in kph.
    printAvgSpeeds(journeySet)

    // 3. Find the total mileage by driver for the whole day.
    printDriverMileages(journeySet)

    // This part is the last part of the puzzle
    // This jira was a little bit unclear.
    // I assume that most active driver means the driver who drove the most mileage
    // for all of the journeys.
    // we somehow need to
    // 4. Find the most active driver - the driver who has driven the most kilometers.
    printMostActiveDriver(journeySet)

  }

  /**
   * Parses a source CSV file into a JourneySet object
   * @param path CSV file path
   * @return JourneySet object with info of all parsed journeys
   */
  def parseJourneys(path: String): JourneySet = {
    val source = Source.fromFile(path)
    val journeys = JourneyFileParser.parseJourneys(source).toList
    source.close()
    JourneySet(journeys)
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
    for (journey <- journeySet.journeys) println(journey)
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
