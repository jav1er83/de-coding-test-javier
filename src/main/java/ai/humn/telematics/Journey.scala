package ai.humn.telematics

case class Journey(journeyId: String,
                   driverId: String,
                   startTime: Long,
                   endTime: Long,
                   startLat: Float,
                   startLon: Float,
                   endLat: Float,
                   endLon: Float,
                   startOdometer: Long,
                   endOdometer: Long)
{
  /** Duration in ms of a journey */
  def duration: Long = endTime - startTime

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
