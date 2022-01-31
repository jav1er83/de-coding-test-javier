package ai.humn.telematics.model

case class Gps(lat: Double, lon: Double)

case class Journey(journeyId: String,
                   driverId: String,
                   startTime: Double,
                   endTime: Double,
                   startPos: Gps,
                   endPos: Gps,
                   startOdometer: Double,
                   endOdometer: Double) {

  def durationMs: Double = endTime - startTime

  def distanceKm: Double = endOdometer - startOdometer

  def avgSpeed: Double = {
    if (distanceKm <= 0) return 0.0

    // If no duration is recorded for the trip, we cannot compute speed (it would be infinite).
    // We also return 0 in this case.
    if (durationMs <= 0) return 0.0

    // For usual cases where we have valid distance and duration, we just
    // transform trip duration to hours and divide distance/duration since we want to return Km/h:
    val durationInHours = durationMs / (1000 * 60 * 60)
    distanceKm / durationInHours
  }

  def isValid: Boolean = durationMs >= 0 & distanceKm >= 0

  def summary: String = {
    val durationInt = durationMs.toInt
    "journeyId: "+journeyId+" "+driverId+" distance "+f"$distanceKm%5s"+" durationMS "+f"$durationInt%8s"+" avgSpeed in kph was "+f"$avgSpeed%1.2f"
  }
}
