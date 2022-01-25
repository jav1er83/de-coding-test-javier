package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

import java.io.InputStream
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class JourneyFileParserTest extends FlatSpec with Matchers {

  //val testFilePath = this.getClass.getClassLoader.getResource("2021-10-05_journeys.csv")
  //val testSource = Source.fromFile(testFilePath.toString)
  val stream: InputStream = getClass.getResourceAsStream("/2021-10-05_journeys.csv")
  val testSource: Source = scala.io.Source.fromInputStream( stream )

  it should "generate 7 Journeys from test file " in {
    // There is 1 duplicate and 1 invalid journey in the test file, so it should generate only 7
    val journeys = JourneyFileParser.parseJourneys(testSource)
    assert(journeys.length == 7)
  }

  it should "correctly parse one correct CSV line" in {
    val line = "000005,driver_b,1633430362000,1633430422000,0.125,0.458,0.125,0.458,123460,123461"
    val parsedLine = JourneyFileParser.parseCsvLine(line)
    val expected = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    assert(parsedLine === expected)
  }

  it should "correctly build a journey from a valid parsed line" in {
    val parsedLine = Array("000005","driver_b","1633430362000","1633430422000","0.125","0.458","0.125","0.458","123460","123461")
    val expected = Journey("000005","driver_b",1633430362000L,1633430422000L,0.125f,0.458f,0.125f,0.458f,123460L,123461L)
    assert(JourneyFileParser.buildJourney(parsedLine) === expected)
  }


}
