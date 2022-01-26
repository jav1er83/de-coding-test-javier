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

  /**
   * Obtains the driver with most kilometers in the JourneySet
   * @return
   */
  def mostActiveDriver: (String, Double) = {
    // This is a way of obtaining the (key, value) of a Map that has the maximum value: (https://stackoverflow.com/a/39713197/437012)
    mileageByDriver.maxBy(_._2)
    // TODO: we could memoize the mileageByDriver for this journey set and avoid computing it again here if it was already computed
  }
}
