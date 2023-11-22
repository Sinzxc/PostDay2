package com.example.postmachine

class Operation {
    /*
    0 - <--
    1 - -->
    -1 - !
    2 - V
    3 - X
    4 - ?
     */
    var number: Int?=null;
    var comand:Int?=null;
    var link:Int?=null;
    var link2:Int?=null;
    constructor(_number:Int,_command:Int,_link:Int){
        if(_number!=null&&_command!=null&&_link!=null){
            number=_number;
            comand=_command;
            link=_link;
        }
    }
    constructor(){
        number=null;
        comand=null;
        link=null;
    }
}