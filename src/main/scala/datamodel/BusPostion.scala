package datamodel

case class BusPostion(busId: String, busNo: String, state:String, lineId: String, lineName:String, codeDirection: String, dest:String, latlon: String, timeDiff: String)

case class BusPostionFinal(busId: String, busNo: String, state:String, lineId: String, lineName:String, codeDirection: Int, dest:String, lat: Double, lon: Double, timeDiff: Int)

object BusPostionFinal{
  def fromBusPostion(busPostion:BusPostion) = {
    val latlon =  busPostion.latlon.split(",")

    BusPostionFinal(
      busId = busPostion.busId,
      busNo = busPostion.busNo,
      state = busPostion.state,
      lineId = busPostion.lineId,
      lineName = busPostion.lineName,
      codeDirection = busPostion.codeDirection.toInt,
      dest = busPostion.dest,
      lat = latlon(0).toDouble,
      lon = latlon(1).toDouble,
      timeDiff = busPostion.timeDiff.toInt
    )
  }
}

