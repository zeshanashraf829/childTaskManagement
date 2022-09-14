package com.example.childtaskmanager.home.presentation.adapter

import TasksListResponse
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.childtaskmanager.R
import com.example.childtaskmanager.databinding.ItemTaskListBinding
import com.example.childtaskmanager.home.presentation.TaskDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.withContext

class TaskListAdapter (val context: Context) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {
    private var mList: ArrayList<TasksListResponse> = ArrayList<TasksListResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = mList[position]
        holder.bind(task)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setListItems(tasks: List<TasksListResponse>){
        this.mList.clear()
        this.mList.addAll(tasks)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemTaskListBinding,val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task:TasksListResponse){
            with(binding){
                task.visualAidUrl?.let {
                    Picasso.get().load(it).error(R.drawable.place_holder_image_square).placeholder(R.drawable.place_holder_image_square).into(image)
                } ?: {
                    Picasso.get().load(R.drawable.place_holder_image_square).into(image)
                }

                llParentView.setOnClickListener {
                    val intent= Intent(context,TaskDetailsActivity::class.java)
                    intent.putExtra("taskId",task._id)
                    context.startActivity(intent)
                }

            }

        }

    }
}