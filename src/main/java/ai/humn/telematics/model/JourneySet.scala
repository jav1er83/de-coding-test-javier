package ai.humn.telematics.model

/**
 * A JourneySet contains all the journeys for a concrete day and provides some helper methods to compute aggregations
 * on all the journeys (e.g total mileage by driver)
 *
 * @param journeys Sequence of journeys for a day
 */
case class JourneySet(journeys: Seq[Journey]) {

  /**
   * Computes mileage per driver
   *
   * @return a Map of driverId -> total mileage
   */
  def mileageByDriver: Map[String, Double] = {
    var driverDistances = Map[String, Double]()
    for (journey <- journeys) {
      if (driverDistances.contains(journey.driverId)) {
        val newDistance = driverDistances(journey.driverId) + journey.distanceKm
        driverDistances = driverDistances + (journey.driverId -> newDistance)
      }
      else {
        driverDistances = driverDistances + (journey.driverId -> journey.distanceKm)
      }
    }
    driverDistances
  }

  /**
   * Obtains the driver with most kilometers in the JourneySet
   *
   * @return a tuple containing (driverId, totalMileage) for the most active driver in the JourneySet
   */
  def mostActiveDriver: Option[Driver] = {
    // This is a way of obtaining the (key, value) of a Map that has the maximum value: (https://stackoverflow.com/a/39713197/437012)
    val driverMileages = mileageByDriver
    if (driverMileages.isEmpty) None else {
      val (driverId, mileage) = driverMileages.maxBy(_._2)
      Some(Driver(driverId, mileage))
    }
  }

  //** Returns the number of journeys in the JourneySet */
  def length: Long = journeys.length
}
