package com.example.postmachine

class Section {
    var index:Int?=null;
    var checked:Boolean=false;
    constructor(_index:Int){
        index=_index;
    }
    fun Check(){
        checked=!checked;
    }
}