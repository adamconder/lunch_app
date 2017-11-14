package services

import com.google.inject.{ImplementedBy, Inject}
import org.joda.time.DateTime


@ImplementedBy(classOf[JodaCalender])
trait CalendarService {
  def today : DateTime
}

class JodaCalender extends CalendarService {
  override def today: DateTime = DateTime.now
}

class ConcreteGreetingService @Inject()(override val calendarService: CalendarService) extends GreetingService {

  def greeting : String = {
    val now = calendarService.today
    val currentHour = now.hourOfDay().get()
    if (currentHour < 12)
      "Good morning!"
    else
      "Good afternoon!"
  }

}

@ImplementedBy(classOf[ConcreteGreetingService])
trait GreetingService {

  val calendarService : CalendarService

  def greeting : String

}