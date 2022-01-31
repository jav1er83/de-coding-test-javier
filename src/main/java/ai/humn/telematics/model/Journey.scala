package ai.humn.telematics.model

case class Journey(journeyId: String,
                   driverId: String,
                   startTime: Double,
                   endTime: Double,
                   startLat: Double,
                   startLon: Double,
                   endLat: Double,
                   endLon: Double,
                   startOdometer: Double,
                   endOdometer: Double) {
  /** Duration in ms of a journey */
  def duration: Double = endTime - startTime

  /** Distance in Kms of a journey */
  def distance: Double = endOdometer - startOdometer

  /** Average speed of a journey in Km/h */
  def avgSpeed: Double = {
    // If no distance was travelled, speed will be 0:
    if (distance <= 0) return 0.0

    // If no duration is recorded for the trip, we cannot compute speed (it would be infinite).
    // We also return 0 in this case.
    if (duration <= 0) return 0.0

    // For usual cases where we have valid distance and duration, we just
    // transform trip duration to hours and divide distance/duration since we want to return Km/h:
    val durationInHours = duration / (1000 * 60 * 60)
    distance / durationInHours
  }

  /** Validates a Journey */
  def isValid: Boolean = {
    duration >= 0 & distance >= 0
  }

  def summary: String = {
    val durationInt = duration.toInt
    "journeyId: "+journeyId+" "+driverId+" distance "+f"$distance%5s"+" durationMS "+f"$durationInt%8s"+" avgSpeed in kph was "+f"$avgSpeed%1.2f"
  }
}
