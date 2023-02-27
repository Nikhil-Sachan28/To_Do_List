package com.example.todolist.controller

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.ToDoItem

class ToDoItemAdapter(context: Context, toDoItemList : MutableList<ToDoItem>) :
    RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {
    var context : Context = context
    var toDoListItem : MutableList<ToDoItem> = toDoItemList


    class ViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        var label : TextView =  itemView.findViewById(R.id.textView_item_text)
        var isDone : CheckBox = itemView.findViewById(R.id.checkbox_item_is_done)


        fun onBind(item: ToDoItem){
            label.text = item.itemText
            isDone.isChecked = item.done as Boolean

            isDone.setOnClickListener {
                if(isDone.isChecked){
                    label.setPaintFlags(label.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                }else{
                    label.setPaintFlags(label.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view : View = LayoutInflater.from(context).
        inflate(R.layout.row_items, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return toDoListItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(toDoListItem[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}

