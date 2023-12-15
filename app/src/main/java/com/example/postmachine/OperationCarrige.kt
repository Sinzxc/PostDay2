package com.example.postmachine

class OperationCarrige(locationIndex:Int) {
        private var locationIndex:Int?= locationIndex
        private var lastLocation= mutableListOf<Int>()
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