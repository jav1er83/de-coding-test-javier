package ai.humn.telematics

/**
 * A JourneySet contains all the journeys for a concrete day and provides some helper methods to compute aggregations
 * on all the journeys (e.g total mileage by driver)
 * @param journeys Sequence of journeys for a day
 */
case class JourneySet(journeys: Seq[Journey]) {

  /**
   * Computes mileage per driver
   * @return a Map of driverId -> total mileage
   */
  def mileageByDriver: Map[String, Double] = {
    val driverDistances = scala.collection.mutable.Map[String, Double]()
    for (journey <- journeys) {
      if (driverDistances.contains(journey.driverId)) {
        driverDistances(journey.driverId) = driverDistances(journey.driverId) + journey.distance
      }
      else {
        driverDistances(journey.driverId) = journey.distance
      }
    }
    driverDistances.toMap
  }

  /**
   * Obtains the driver with most kilometers in the JourneySet
   * @return a tuple containing (driverId, totalMileage) for the most active driver in the JourneySet
   */
  def mostActiveDriver: (String, Double) = {
    // This is a way of obtaining the (key, value) of a Map that has the maximum value: (https://stackoverflow.com/a/39713197/437012)
    mileageByDriver.maxBy(_._2)
    // TODO: we could memoize the mileageByDriver for this journey set and avoid computing it again here if it was already computed
  }

  //** Returns the number of journeys in the JourneySet */
  def length: Long = journeys.length
}
