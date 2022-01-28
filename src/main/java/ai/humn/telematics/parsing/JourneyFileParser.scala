package ai.humn.telematics.parsing

import ai.humn.telematics.model.{Journey, JourneySet}

import scala.collection.Iterator
import scala.io.Source
import scala.util.{Success, Try}

object JourneyFileParser {

  /**
   * Parses a CSV source into a JourneySet
   *
   * @param source scala.io.Source pointing to the input CSV file
   * @return JourneySet containing all (valid) journeys on the original CSV source file
   */
  def parseJourneys(source: Source): JourneySet = {
    val rawLines = source.getLines()

    // Skip Header
    if (rawLines.hasNext) rawLines.next()
    else return JourneySet(Seq()) // return empty JourneySet if input has no content

    // Parse and validate CSV Lines
    val parsedLines = for (rawLine <- rawLines) yield parseCsvLine(rawLine)
    val validatedLines = parsedLines.filter(isValid) // drop invalid lines

    // Build the journeys from the input and discard those Journeys that failed to build:
    val journeys = buildJourneys(validatedLines).collect { case Success(journey) => journey }
    //val journeys = buildJourneys(validatedLines).toStream.filter(_.isSuccess).map(_.get)

    // Drop invalid journeys (that built correctly but have incorrect field values):
    val validatedJourneys = journeys.filter(_.isValid)

    // Drop duplicates:  // TODO drop duplicates taking into account only journeyId?
    val dedupJourneys = validatedJourneys.toStream.distinct

    // Build a JourneySet with the deduplicated & validated journeys:
    JourneySet(dedupJourneys)
  }

  /**
   * Parses a CSV line into multiple String fields
   *
   * @param line
   * @return Array of String fields
   */
  def parseCsvLine(line: String): Array[String] = line.split(",")

  /**
   * Validates if a CSV line is correct
   *
   * @param parsedLine
   * @return true if line is valid else otherwise
   */
  def isValid(parsedLine: Array[String]): Boolean = {
    parsedLine.length == 10
  }

  /**
   * Builds an iterable of Journey objects from the parsed lines of a Journey CSV file
   *
   * @param parsedLines
   * @return
   */
  def buildJourneys(parsedLines: Iterator[Array[String]]): Iterator[Try[Journey]] = {
    for (line <- parsedLines) yield buildJourney(line)
  }

  /**
   * Tries to build a Journey object from a parsed CSV line (an array of CSV fields)
   *
   * @param csvFields
   * @return Try[Journey] object built from the CSV fields. It can be a Success(Journey)
   *         if everything goes fine or a Failure(Exception) if some exception occurs when trying to build the Journey
   */
  def buildJourney(csvFields: Array[String]): Try[Journey] = {
    Try(
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
    )
  }


}
