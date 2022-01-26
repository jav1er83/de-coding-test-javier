package ai.humn.telematics

case class JourneySet(journeys: Iterable[Journey]) {

  /**
   * Computes mileage per driver
   * @return a Map of driverId -> mileage
   */
  def mileageByDriver: scala.collection.mutable.Map[String, Double] = {
    val driverDistances = scala.collection.mutable.Map[String, Double]()
    for (journey <- journeys) {
      if (driverDistances.contains(journey.driverId)) {
        driverDistances(journey.driverId) = driverDistances(journey.driverId) + journey.distance
      }
      else {
        driverDistances(journey.driverId) = journey.distance
      }
    }
    driverDistances
  }
}
