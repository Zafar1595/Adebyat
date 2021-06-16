package space.adebyat.adebyat.ui.author

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Author

class AuthorAdapter: RecyclerView.Adapter<AuthorAdapter.ListViewHolder>() {

    var models: List<Author> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    private var onItemClick:(departmentName: String)-> Unit = {}
    fun setOnItemClickListener(onItemClick:(departmentName: String)-> Unit){
        this.onItemClick = onItemClick
    }

    private var onItemClickAuthorInfo:(author: Author)-> Unit = {}
    fun setOnItemClickListenerAuthorInfo(onItemClickAuthorInfo:(author: Author)-> Unit){
        this.onItemClickAuthorInfo = onItemClickAuthorInfo
    }
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun populateModel(author: Author){
            itemView.findViewById<TextView>(R.id.textViewPoetsName).text = author.name
            itemView.findViewById<TextView>(R.id.textViewPoetsDate).text = author.date
            if(author.image_url != ""){
                val imageView: ImageView = itemView.findViewById(R.id.imageViewPoets)
                Glide.with(itemView).load(author.image_url).into(imageView)
            }
            //image set
            itemView.setOnClickListener {
                onItemClick.invoke(author.name)
            }
            itemView.findViewById<ImageButton>(R.id.imageButtonAuthorInfo).setOnClickListener {
                onItemClickAuthorInfo.invoke(author)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_person_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int = models.size

}