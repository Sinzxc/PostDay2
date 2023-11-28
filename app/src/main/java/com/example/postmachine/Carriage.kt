package com.example.postmachine

class Carriage(locationIndex: Int) {
    private var locationIndex:Int?= locationIndex
    private var lastLocation= mutableListOf<Int>()
    fun  carriageRight() {
        lastLocation.add(lastLocation.size,locationIndex!!)
        if (locationIndex!=null)
            locationIndex = locationIndex!! + 1
    }
    fun  carriageLeft() {
        lastLocation.add(lastLocation.size,locationIndex!!)
        if (locationIndex!=null)
            locationIndex = locationIndex!! - 1
    }


    fun setNewLocation(index:Int){
        lastLocation.add(locationIndex!!)
        locationIndex=index
    }
    fun  getCarriageLocation():Int{
        if (locationIndex!=null)
            return locationIndex as Int

        return 0
    }

    fun  getLastCarriageLocation():MutableList<Int>{
            return lastLocation
    }

    fun  clearCarrigeList(){
        lastLocation= mutableListOf()
    }

}