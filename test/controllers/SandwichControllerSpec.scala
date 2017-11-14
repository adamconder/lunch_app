package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}

class SandwichControllerSpec extends PlaySpec with GuiceOneAppPerTest {

  "SandwichController" should {

    "inform the user we're sold out when there are no sandwiches" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/sandwiches").withHeaders("Host" -> "Localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("<title>Sandwiches</title>")
      contentAsString(home) must include("<h1>Have a look at today's sandwiches</h1>")

      contentAsString(home) must include("<p>Sorry, we're sold out</p>")
    }

  }

}
