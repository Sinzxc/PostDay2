package com.example.postmachine

class Commands {
    private var commandsList= mutableListOf<Operation>()

    fun getCommands():MutableList<Operation>{
        return commandsList
    }

    fun addCommand(inputOperationId:Int,inputLink:Int){
        commandsList.add(Operation(commandsList.size,inputOperationId,inputLink))
    }

    fun addCommand(inputOperationId:Int,inputLink:Int,inputLink2: Int){
        commandsList.add(Operation(commandsList.size,inputOperationId,inputLink,inputLink2))
    }

    fun removeLastCommand(){
        commandsList.removeLast()
    }


}