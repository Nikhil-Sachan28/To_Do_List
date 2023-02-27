package com.example.todolist.model

class ToDoItem {
    companion object Factory{
        fun create() : ToDoItem = ToDoItem()
    }

    var objId : String ?=null
    var itemText : String ?=null
    var done : Boolean ? = false
}