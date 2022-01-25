package ai.humn.telematics

case class Journey(journeyId: String,
                   driverId: String,
                   startTime: Double,
                   endTime: Double,
                   startLat: Double,
                   startLon: Double,
                   endLat: Double,
                   endLon: Double,
                   startOdometer: Double,
                   endOdometer: Double)
{
  /** Duration in ms of a journey */
  def duration: Double = endTime - startTime

  /** Distance in Kms of a journey */
  def distance: Double = endOdometer - startOdometer

  /** Average speed of a journey in Km/h */
  def avgSpeed: Double = {
    val durationInHours = duration / (1000 * 60 * 60)
    distance / durationInHours
  }

  override def toString: String = {
    "journeyId: "+journeyId+" "+driverId+" distance "+distance+" durationMS "+duration+" avgSpeed in kph was "+avgSpeed
  }
}
