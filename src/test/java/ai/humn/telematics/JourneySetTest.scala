package ai.humn.telematics

import ai.humn.telematics.model.{Driver, Gps, Journey, JourneySet}
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JourneySetTest extends FlatSpec with Matchers {


  it should "correctly compute mileage by driver in a journey set" in {
    val journey1 = Journey("000001","driver_a",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123461.0d)
    val journey2 = Journey("000002","driver_b",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123471.0d)
    val journey3 = Journey("000003","driver_a",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123499.0d)
    val computedMileages = JourneySet(Seq(journey1, journey2, journey3)).mileageByDriver
    val expectedMileages = scala.collection.mutable.Map("driver_a" -> 40.0d, "driver_b" -> 11.0d)
    assert (computedMileages === expectedMileages)
  }

  it should "correctly compute most active driver" in {
    val journey1 = Journey("000001","driver_a",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123461.0d)
    val journey2 = Journey("000002","driver_b",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123471.0d)
    val journey3 = Journey("000003","driver_a",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123499.0d)
    val journeySet = JourneySet(Seq(journey1, journey2, journey3))
    assert(journeySet.mostActiveDriver === Some(Driver("driver_a", 40.0d)))
  }

}
