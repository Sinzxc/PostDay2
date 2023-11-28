package com.example.postmachine

class Operation {
    /*
    0 - <--
    1 - -->
    2 - V
    3 - X
    4 - ?
    5 - !
     */
    private var index: Int?=null
    private var comandId:Int?=null
    private var link:Int?=null
    private var link2:Int?=null

    constructor(inputIndex:Int,inputCommandId:Int,inputLink:Int){
        index=inputIndex
        comandId=inputCommandId
        link=inputLink
    }
    constructor(inputIndex:Int,inputCommandId:Int,inputLink:Int,inputLink2:Int){
        index=inputIndex
        comandId=inputCommandId
        link=inputLink
        link2=inputLink2
    }

    fun getOperationIcon():String{
        if(comandId==0)
            return "←"
        if(comandId==1)
            return "→"
        if(comandId==2)
            return "V"
        if(comandId==3)
            return "X"
        if(comandId==4)
            return "?"
        if(comandId==5)
            return "!"

         return ""
    }
    fun getOperationId(): Int {
        if (comandId!=null)
            return comandId as Int

        return 0
    }

    fun getLink():Int{
        if (link!=null)
            return link as Int
        return 0
    }

    fun getLink2():Int{
        if (link2!=null)
            return link2 as Int
        return 0
    }
}