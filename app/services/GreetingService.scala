package services

import com.google.inject.ImplementedBy

class ConcreteGreetingService extends GreetingService {

  def greeting : String = "Alright!"

}

@ImplementedBy(classOf[ConcreteGreetingService])
trait GreetingService {

  def greeting : String

}