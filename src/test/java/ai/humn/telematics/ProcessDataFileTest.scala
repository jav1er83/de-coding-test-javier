package ai.humn.telematics

import ai.humn.telematics.model.{Gps, Journey}
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ProcessDataFileTest extends FlatSpec with Matchers {


  it should "correctly update driver distance in a bunch of journeys" in {
    val journey1 = Journey("000001","driver_a",1633430362000.0d,1633430422000.0d,Gps(0.125d,0.458d),Gps(0.125d,0.458d),123460.0d,123461.0d)

    var driverDistances = Map[String, Double]("driver_a" -> 100.0)

    //update distance
    driverDistances = ProcessDataFile.updateDriverDistance(driverDistances, journey1)

    assert(driverDistances("driver_a") === 101.0)
  }

  it should "correctly compute most active driver" in {
    val driverDistances = scala.collection.mutable.Map[String, Double]("driver_a" -> 100.0, "driver_b" -> 300.5, "driver_c" -> 5.0)
    assert(ProcessDataFile.computeMostActiveDriver(driverDistances.toMap) === Some("driver_b", 300.5))
  }

}
