package com.example.todolist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.todolist.controller.ToDoItemAdapter
import com.example.todolist.model.Constants
import com.example.todolist.model.ToDoItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var mDatabase : DatabaseReference?=null
    var fab : FloatingActionButton?=null
    var toDoItemList : MutableList<ToDoItem> ?=null
    var adapter : ToDoItemAdapter?=null
    var recyclerViewItems : RecyclerView?=null
    var layoutManager: LayoutManager?=null
    var itemListener: ValueEventListener?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //floating action button and click listener
        fab = findViewById(R.id.floatingActionButtonId) as FloatingActionButton
        fab!!.setOnClickListener { view->
            addNewItemDialog()
        }

        recyclerViewItems = findViewById<RecyclerView>(R.id.recyclerViewId)
        //creating obj of databse
        mDatabase = FirebaseDatabase.getInstance().reference
        toDoItemList = mutableListOf()
        adapter = ToDoItemAdapter(this, toDoItemList!!)
        layoutManager = LinearLayoutManager(this)
        recyclerViewItems?.layoutManager = layoutManager
        recyclerViewItems?.adapter = adapter


        itemListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                addDataToList(dataSnapshot)

            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
            }
        }

        mDatabase!!.orderByKey().addListenerForSingleValueEvent(itemListener as ValueEventListener)
    }
    fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()

        if (items.hasNext()) {
            val toDoListIndex = items.next()
            val itemsIterator = toDoListIndex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val todoItem = ToDoItem.create()
                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>

                //key will return Firebase ID
                todoItem.objId = currentItem.key
                todoItem.done = map.get("done") as Boolean?
                todoItem.itemText = map.get("itemText") as String?

                toDoItemList!!.add(todoItem);
            }


        }
        //alert adapter that has changed
        adapter!!.notifyDataSetChanged()
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage("Add New Item")
        alert.setTitle("Enter TO Do List Item")
        alert.setView(itemEditText)
        alert.setPositiveButton("Submit"){dialog, postiveButton ->
            val toDoItem = ToDoItem.create()
            toDoItem.itemText = itemEditText.text.toString()
            toDoItem.done = false;


            val newItem = mDatabase?.child(Constants.FIREBASE_ITEM)?.push()
            if (newItem != null) {
                toDoItem.objId = newItem.key
                newItem.setValue(toDoItem)

                dialog.dismiss()
                Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show()
            }
        }
        alert.show()
    }
}