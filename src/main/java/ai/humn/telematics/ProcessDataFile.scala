package ai.humn.telematics

import scala.io.Source
import scala.collection.Iterator


case class Journey(journeyId: String,
                   driverId: String,
                   startTime: Long,
                   endTime: Long,
                   startLat: Float,
                   startLon: Float,
                   endLat: Float,
                   endLon: Float,
                   startOdometer: Long,
                   endOdometer: Long)
{
  def duration: Long = endTime - startTime
}

object ProcessDataFile {

  def main(args: Array[String]): Unit = {

    // Parse input CSV file into a list of Journeys
    val inputPath = args(0)
    val source = Source.fromFile(inputPath)
    val journeys = parseJourneys(source)

    // 1. Find journeys that are 90 minutes or more.
    println("Journeys longer than 90 minutes:")
    for (journey <- journeys) {
      val ninetyMins = 90 * 60 * 1000
      if (journey.duration > ninetyMins) println(journey.journeyId)
    }

    source.close()

    // need to do the
    // 2. Find the average speed per journey in kph.

    // her eis where I will
    // 3. Find the total mileage by driver for the whole day.

    // This part is the last part of the puzzle
    // This jira was a little bit unclear.
    // I assume that most active driver means the driver who drove the most mileage
    // for all of the journeys.
    // we somehow need to
    // 4. Find the most active driver - the driver who has driven the most kilometers.

  }


  def parseJourneys(source: Source): Iterator[Journey] = {
    val rawLines = source.getLines()
    rawLines.next() // skip header
    // Parse CSV Lines
    val parsedLines = for (rawLine <- rawLines) yield parseCsvLine(rawLine)
    val validLines = parsedLines.filter(isValid)
    buildJourneys(validLines)
  }

  def parseCsvLine(line: String): Array[String] = {
    line.split(",")
  }

  def isValid(parsedLine: Array[String]): Boolean = {
    true
  }

  def buildJourneys(parsedLines: Iterator[Array[String]]): Iterator[Journey] = {
    for (line <- parsedLines) yield buildJourney(line)
  }

  def buildJourney(csvFields: Array[String]): Journey = {
    Journey(journeyId = csvFields(0),
      driverId = csvFields(1),
      startTime = csvFields(2).toLong,
      endTime = csvFields(3).toLong,
      startLat = csvFields(4).toFloat,
      endLat = csvFields(5).toFloat,
      startLon = csvFields(6).toFloat,
      endLon = csvFields(7).toFloat,
      startOdometer = csvFields(8).toLong,
      endOdometer = csvFields(9).toLong
    )
  }

}
