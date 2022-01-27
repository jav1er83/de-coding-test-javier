package ai.humn.telematics

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JourneyTest extends FlatSpec with Matchers {


  it should "correctly compute duration of a journey" in {
    val journey = Journey("000005","driver_b",1633430362000.0d,1633430422000.0d,0.125d,0.458d,0.125d,0.458d,123460.0d,123461.0d)
    assert (journey.duration === 60000.0)
  }

  it should "correctly compute duration of a journey even with bad data" in {
    val journey = Journey("000001","driver_a",1633430422000.0,1633430362000.0,0,0,0,0,0,0)
    assert (journey.duration === -60000.0)
  }

  it should "correctly compute distance of a journey" in {
    val journey = Journey("000005","driver_b",1633430362000.0d,1633430422000.0d,0.125d,0.458d,0.125d,0.458d,123460.0d,123461.0d)
    assert (journey.distance === 1.0)
  }

  it should "correctly compute distance of a journey even with bad data" in {
    val journey = Journey("000001","driver_a",0,0,0,0,0,0,123465.0,123460.0)
    assert (journey.distance === -5.0)
  }

  it should "correctly compute average speed of a journey" in {
    val journey = Journey("000005","driver_b",1633430362000.0d,1633430422000.0d,0.125d,0.458d,0.125d,0.458d,123460.0d,123461.0d)
    assert (journey.avgSpeed === 60.0)
  }

  it should "compute an avg speed of 0 if no positive distance" in {
    val journey = Journey("000001","driver_a",0,0,0,0,0,0,123465.0,123460.0)
    assert (journey.avgSpeed === 0.0)
  }

  it should "compute an avg speed of 0 if no positive duration" in {
    val journey = Journey("000001","driver_a",1633430422000.0,1633430362000.0,0,0,0,0,0,0)
    assert (journey.avgSpeed === 0.0)
  }

}
