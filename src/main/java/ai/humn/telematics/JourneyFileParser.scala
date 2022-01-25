package ai.humn.telematics

import scala.collection.Iterator
import scala.io.Source

object JourneyFileParser {

  /**
   * Parses a CSV source into an iterable of Journeys
   * @param source
   * @return iterator on parsed Journeys from the source
   */
  def parseJourneys(source: Source): Iterator[Journey] = {
    val rawLines = source.getLines()
    rawLines.next() // skip header
    // Parse CSV Lines
    val parsedLines = for (rawLine <- rawLines) yield parseCsvLine(rawLine)
    val validLines = parsedLines.filter(isValid)
    buildJourneys(validLines)
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
    true  //TODO: implement validation
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
