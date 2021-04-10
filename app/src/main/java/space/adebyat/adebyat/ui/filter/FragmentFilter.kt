package space.adebyat.adebyat.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme
import space.adebyat.adebyat.databinding.FragmentFilterBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.CreationView
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class FragmentFilter: Fragment(R.layout.fragment_filter), FilterView {

    var adapter = CreationAdapter()
    lateinit var binding: FragmentFilterBinding
    private val presenter: FilterPresenter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.init(this)
        presenter.getAllCreations()
        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            view.context.startActivity(intent)
        }
    }

    override fun setCreations(creation: List<Creation>) {
        adapter.models = selectionSort(creation)
        setLoading(false)
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun setThemes(themes: List<Theme>) {
        TODO("Not yet implemented")
    }

    fun selectionSort(list: List<Creation>): List<Creation> {
        var listSorted: MutableList<Creation> = list as MutableList<Creation>
        if(list.size > 1) {
            for (i in listSorted.indices) {
                var max = listSorted[i]
                var maxId = i
                for (j in i + 1 until listSorted.size) {
                    if (listSorted[j].viewed > max.viewed) {
                        max = listSorted[j]
                        maxId = j
                    }
                }
                // замена
                var temp = listSorted[i]
                listSorted[i] = max;
                listSorted[maxId] = temp
            }
        }
        return listSorted
    }

}