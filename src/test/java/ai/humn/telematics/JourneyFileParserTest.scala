package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

import java.io.InputStream
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class JourneyFileParserTest extends FlatSpec with Matchers {

  val stream: InputStream = getClass.getResourceAsStream("/2021-10-05_journeys.csv")
  val testSource: Source = scala.io.Source.fromInputStream( stream )

  it should "generate only valid Journeys from test file " in {
    // There is 1 duplicate and 2 invalid journeys in the test file, so it should generate only 6
    val journeys = JourneyFileParser.parseJourneys(testSource)
    assert(journeys.length == 6)
  }

  it should "correctly parse one correct CSV line" in {
    val line = "000005,driver_b,1633430362000,1633430422000,0.125,0.458,0.125,0.458,123460,123461"
    val parsedLine = JourneyFileParser.parseCsvLine(line)
    val expected = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    assert(parsedLine === expected)
  }

  it should "correctly build a journey from a valid parsed line" in {
    val parsedLine = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    val expected = Journey("000005","driver_b",1633430362000.0d,1633430422000.0d,0.125d,0.458d,0.125d,0.458d,123460.0d,123461.0d)
    assert(JourneyFileParser.buildJourney(parsedLine) === expected)
  }


}
