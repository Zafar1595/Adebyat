package space.adebyat.adebyat.ui.creation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Theme
import space.adebyat.adebyat.databinding.RvThemeItemBinding

class ThemeAdapter : RecyclerView.Adapter<ThemeAdapter.ListViewHolder>() {
    var models: List<Theme> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onItemClick: (theme: String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (theme: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class ListViewHolder(private val binding: RvThemeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(theme: Theme, position: Int) {
            binding.apply {
                textViewTheme.text = theme.name
                textViewTheme.setBackgroundResource(if (theme.isSelected) R.drawable.selected_tag_bg else R.drawable.tag_bg)
                root.setOnClickListener {
                    theme.isSelected = !theme.isSelected
                    notifyItemChanged(position)
                    onItemClick.invoke(theme.name)
                }
            }
            itemView.findViewById<TextView>(R.id.textViewTheme).text = theme.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_theme_item, parent, false)
        val binding = RvThemeItemBinding.bind(itemView)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.populateModel(models[position], position)
    }

    override fun getItemCount(): Int = models.size
}