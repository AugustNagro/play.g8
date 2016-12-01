package models

case class Greeting(msg: String){
  def custom: String = msg + " world"
}