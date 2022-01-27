package ai.humn.telematics

import scala.collection.Iterator
import scala.io.Source

object JourneyFileParser {

  /**
   * Parses a CSV source into a JourneySet
   * @param source scala.io.Source pointing to the input CSV file
   * @return JourneySet containing all (valid) journeys on the original CSV source file
   */
  def parseJourneys(source: Source): JourneySet = {
    val rawLines = source.getLines()

    // Skip Header
    if (rawLines.hasNext) {
      rawLines.next()
    }
    else return JourneySet(Seq()) // return empty JourneySet if input has no content

    // Parse CSV Lines
    val parsedLines = for (rawLine <- rawLines) yield parseCsvLine(rawLine)
    val validatedLines = parsedLines.filter(isValid) // drop invalid lines

    val journeys = buildJourneys(validatedLines)

    // Drop invalid journeys:
    val validatedJourneys = journeys.filter(_.isValid)

    // Drop duplicates:  // TODO drop duplicates of just journey id?
    val dedupJourneys = validatedJourneys.toStream.distinct

    // Build a JourneySet with the deduplicated & validated journeys:
    JourneySet(dedupJourneys)
  }

  /**
   * Parses a CSV line into multiple String fields
   * @param line
   * @return Array of String fields
   */
  def parseCsvLine(line: String): Array[String] = line.split(",")

  /**
   * Validates if a CSV line is correct
   * @param parsedLine
   * @return true if line is valid else otherwise
   */
  def isValid(parsedLine: Array[String]): Boolean = {
    parsedLine.length == 10
  }

  /**
   * Builds an iterable of Journey objects from the parsed lines of a Journey CSV file
   * @param parsedLines
   * @return
   */
  def buildJourneys(parsedLines: Iterator[Array[String]]): Iterator[Journey] = {
    for (line <- parsedLines) yield buildJourney(line)
  }

  /**
   * Builds a Journey object from a parsed CSV line (an array of CSV fields)
   * @param csvFields
   * @return Journey object built from the CSV fields
   */
  def buildJourney(csvFields: Array[String]): Journey = {
    Journey(journeyId = csvFields(0),
      driverId = csvFields(1),
      startTime = csvFields(2).toDouble,
      endTime = csvFields(3).toDouble,
      startLat = csvFields(4).toDouble,
      startLon = csvFields(5).toDouble,
      endLat = csvFields(6).toDouble,
      endLon = csvFields(7).toDouble,
      startOdometer = csvFields(8).toDouble,
      endOdometer = csvFields(9).toDouble
    )
  }


}
