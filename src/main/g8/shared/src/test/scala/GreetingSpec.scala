import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import models.Greeting

object GreetingSpec extends Properties("Greeting") {

  property("custom") = forAll { (s: String) =>
    Greeting(s).custom.contains(s)
  }
}
