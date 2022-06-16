package utils

object DistanceHelper {
  val PI = 3.1415926
  val R: Double  = 6370.99681

  def getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double ={
    val a1 = lat1 * PI /180.0
    val a2 = lon1 * PI /180.0
    val b1 = lat2 * PI /180.0
    val b2 = lon2 * PI /180.0
    var t1: Double = Math.cos(a1) * Math.cos(a2) * Math.cos(b1)* Math.cos(b2)
    var t2: Double = Math.cos(a1) * Math.sin(a2) * Math.cos(b1)* Math.sin(b2)
    var t3: Double = Math.sin(a1) * Math.sin(b1)
    val distance = Math.acos(t1 + t2 + t3) * R
    distance
  }


}
