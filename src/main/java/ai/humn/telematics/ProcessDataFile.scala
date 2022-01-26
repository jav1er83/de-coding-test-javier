package ai.humn.telematics

import scala.io.Source

object ProcessDataFile {

  def main(args: Array[String]): Unit = {

    // Parse input CSV file into a list of Journeys
    val inputPath = args(0)
    val source = Source.fromFile(inputPath)
    val journeys = JourneyFileParser.parseJourneys(source).toList
    source.close()

    // 1. Find journeys that are 90 minutes or more.
    println("Journeys longer than 90 minutes:")
    for (journey <- journeys) {
      val ninetyMins = 90 * 60 * 1000
      if (journey.duration > ninetyMins) println(journey)
    }

    // 2. Find the average speed per journey in kph.
    println("Average speed per (valid) journey:")
    for (journey <- journeys) println(journey)

    // 3. Find the total mileage by driver for the whole day.
    val journeySet = JourneySet(journeys)
    println("Mileage By Driver")
    for ((driverId, distance) <- journeySet.mileageByDriver) {
      println(driverId+" drove "+ distance+" kilometers")
    }

    // This part is the last part of the puzzle
    // This jira was a little bit unclear.
    // I assume that most active driver means the driver who drove the most mileage
    // for all of the journeys.
    // we somehow need to
    // 4. Find the most active driver - the driver who has driven the most kilometers.

  }
}
