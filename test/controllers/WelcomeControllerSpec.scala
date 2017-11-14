package controllers

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}
import services.{CalendarService, ConcreteGreetingService, GreetingService}

object FakeCalendarService extends CalendarService {
  val formatter : DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  override def today: DateTime = DateTime.parse("2017-10-12 12:52:21", formatter)
}

object MorningCalendarService extends CalendarService {
  val formatter : DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  override def today: DateTime = DateTime.parse("2017-10-12 11:52:21", formatter)
}

//object FakeGreetingService extends GreetingService {
//  override def greeting: String = "Good morning!"
//
//  override val calendarService: CalendarService = FakeCalendarService
//}
//
//object FakeAfternoonGreetingService extends GreetingService {
//  override def greeting: String = "Good afternoon!"
//
//  override val calendarService: CalendarService = FakeCalendarService
//}

class WelcomeControllerSpec extends PlaySpec with GuiceOneAppPerTest {

  val greetingService = new ConcreteGreetingService(FakeCalendarService)

  "WelcomeController GET" should {

    "return a successful response" in {
      val controller = new WelcomeController(greetingService)
      val result = controller.welcome.apply(FakeRequest())
      status(result) mustBe OK
    }

    "respond to the /welcome url" in {
      val request = FakeRequest(GET, "/welcome").withHeaders("Host" -> "localhost")
      val home = route(app, request).get
      status(home) mustBe OK
    }

    "return some html" in {
      val controller = new WelcomeController(greetingService)
      val result = controller.welcome().apply(FakeRequest())
      contentType(result) mustBe Some("text/html")
    }

    "say good morning and have a title" in {

      val controller = new WelcomeController(new ConcreteGreetingService(MorningCalendarService))
      val result = controller.welcome().apply(FakeRequest(GET, "/foo"))
      contentAsString(result) must include ("<h1>Good morning!</h1>")
      contentAsString(result) must include ("<title>Welcome!</title>")
    }

    "say good afternoon when it's the afternoon and have a title" in {
      val controller = new WelcomeController(greetingService)
      val result = controller.welcome().apply(FakeRequest(GET, "/foo"))
      contentAsString(result) must not include ("<h1>Good morning!</h1>")
      contentAsString(result) must include ("<h1>Good afternoon!</h1>")
      contentAsString(result) must include ("<title>Welcome!</title>")
    }

  }

}