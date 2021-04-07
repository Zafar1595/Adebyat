package space.adebyat.adebyat.ui.creation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.ui.MainActivity
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class CreationAdapter: RecyclerView.Adapter<CreationAdapter.ListViewHolder>() {

    var models: List<Creation> = listOf()
    set(value){
        field = value
        notifyDataSetChanged()
    }
    private var onItemClick:(departmentCreation: Creation)-> Unit = {}
    fun setOnItemClickListener(onItemClick:(departmentCreation: Creation)-> Unit){
        this.onItemClick = onItemClick
    }
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun populateModel(creation: Creation){
            itemView.findViewById<TextView>(R.id.textViewCreationName).text = creation.name
            itemView.findViewById<TextView>(R.id.textViewCreationGenre).text = creation.genre
            itemView.setOnClickListener {
                onItemClick.invoke(creation)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreationAdapter.ListViewHolder {
        return ListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_creation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CreationAdapter.ListViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size
}