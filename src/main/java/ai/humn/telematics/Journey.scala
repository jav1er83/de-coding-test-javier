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
  def duration: Long = endTime - startTime
}
