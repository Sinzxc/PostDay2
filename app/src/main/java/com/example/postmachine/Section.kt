package com.example.postmachine

class Section(inputIndex: Int) {
    private var index:Int?= inputIndex
    private var checked:Boolean=false

    fun check(){
        checked=!checked
    }
    fun setCheck(){
        checked=true
    }
    fun setUncheck(){
        checked=false
    }
    fun getChecked():Boolean{
        return checked
    }
    fun getIndex():Int {
        if(index!=null)
            return index as Int

        return 0
    }
}