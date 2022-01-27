package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

import java.io.InputStream
import scala.io.Source
import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class JourneyFileParserTest extends FlatSpec with Matchers {

  val stream: InputStream = getClass.getResourceAsStream("/2021-10-05_journeys.csv")
  val testSource: Source = scala.io.Source.fromInputStream( stream )

  it should "generate only valid Journeys from test file " in {
    // There is 1 duplicate and 2 invalid journeys in the test file, so it should generate only 6
    val journeySet = JourneyFileParser.parseJourneys(testSource)
    assert(journeySet.length == 6)
  }

  it should "generate an empty JourneySet from an empty file" in {
    val stream: InputStream = getClass.getResourceAsStream("/empty_file.csv")
    val testSource: Source = scala.io.Source.fromInputStream( stream )
    val journeySet = JourneyFileParser.parseJourneys(testSource)
    assert(journeySet.length == 0)
  }

  it should "generate an empty JourneySet from a file containing only header" in {
    val stream: InputStream = getClass.getResourceAsStream("/only_header.csv")
    val testSource: Source = scala.io.Source.fromInputStream( stream )
    val journeySet = JourneyFileParser.parseJourneys(testSource)
    assert(journeySet.length == 0)
  }

  it should "skip lines with missing fields when parsing a file" in {
    // This file has 3 invalid lines and 2 invalid Journeys. Should generate 5 journeys
    val stream: InputStream = getClass.getResourceAsStream("/file_with_missing_fields.csv")
    val testSource: Source = scala.io.Source.fromInputStream( stream )
    val journeySet = JourneyFileParser.parseJourneys(testSource)
    assert(journeySet.length == 5)
  }

  it should "skip lines with invalid fields" in {
    // This file has 2 lines with invalid fields. Should generate only 1 journey
    val stream: InputStream = getClass.getResourceAsStream("/file_with_invalid_fields.csv")
    val testSource: Source = scala.io.Source.fromInputStream( stream )
    val journeySet = JourneyFileParser.parseJourneys(testSource)
    assert(journeySet.length == 1)
  }

  it should "correctly parse one correct CSV line" in {
    val line = "000005,driver_b,1633430362000,1633430422000,0.125,0.458,0.125,0.458,123460,123461"
    val parsedLine = JourneyFileParser.parseCsvLine(line)
    val expected = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    assert(parsedLine === expected)
  }

  it should "correctly build a journey from a valid parsed line" in {
    val parsedLine = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    val expected = Success(Journey("000005","driver_b",1633430362000.0d,1633430422000.0d,0.125d,0.458d,0.125d,0.458d,123460.0d,123461.0d))
    assert(JourneyFileParser.buildJourney(parsedLine) === expected)
  }

  it should "detect a line with missing fields as invalid" in {
    val line = "000005,driver_b,1633430362000,1633430422000,0.125,0.458,0.125,3460,123461"
    val parsedLine = JourneyFileParser.parseCsvLine(line)
    assert(!JourneyFileParser.isValid(parsedLine))
  }

  it should "return a failure when building a journey from a line with incorrect fields" in {
    val parsedLine = Array("000005","driver_b","1633430362000","1633430422000","0.125","abc","0.125","0.458","123460","hello")
    val journey = JourneyFileParser.buildJourney(parsedLine)
    assert(journey isFailure)
  }


}
