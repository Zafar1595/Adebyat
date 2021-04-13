package space.adebyat.adebyat.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.*
import space.adebyat.adebyat.databinding.FragmentFilterBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class FragmentFilter: Fragment(R.layout.fragment_filter), FilterView {

    var adapter = CreationAdapter()
    lateinit var binding: FragmentFilterBinding
    private val presenter: FilterPresenter by inject()
    private var list: List<Creation> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.init(this)
        presenter.getAllCreations()
        presenter.getData()
        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            view.context.startActivity(intent)
        }
        binding.imageButtonFilter.setOnClickListener {
            filterContainer()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    search(p0)
                }
                return false
            }
        })
        binding.autoCompleteAuthors.setOnItemClickListener { adapterView, view, i, l ->
            //
        }
        binding.autoCompleteDirections.setOnItemClickListener { adapterView, view, i, l ->
            //
        }
        binding.autoCompleteGenre.setOnItemClickListener { adapterView, view, i, l ->
            //
        }
        binding.autoCompletePeriod.setOnItemClickListener { adapterView, view, i, l ->
            //
        }
        binding.autoCompleteTheme.setOnItemClickListener { adapterView, view, i, l ->
            //
        }
    }

    override fun setCreations(creation: List<Creation>) {
        adapter.models = selectionSort(creation)
        list = creation
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
        var mList = mutableListOf<String>()
        themes.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteTheme.setAdapter(spinnerAdapter)
    }

    override fun setDirections(directions: List<Direction>) {
        var mList = mutableListOf<String>()
        directions.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteDirections.setAdapter(spinnerAdapter)
    }

    override fun setGenre(genres: List<Genre>) {
        val mList = mutableListOf<String>()
        genres.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteGenre.setAdapter(spinnerAdapter)
    }

    override fun setPeriod(periods: List<space.adebyat.adebyat.data.Period>) {
        var mList = mutableListOf<String>()
        periods.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompletePeriod.setAdapter(spinnerAdapter)
    }

    override fun setAuthors(authors: List<Author>) {
        var mList = mutableListOf<String>()
        authors.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteAuthors.setAdapter(spinnerAdapter)
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

    var visivle = false
    fun filterContainer(){
        if(!visivle){
            binding.filterContainer.visibility = View.VISIBLE
            visivle = true
        }else{
            binding.filterContainer.visibility = View.GONE
            visivle = false
        }
    }

    fun search(creationName: String){
        var filteredList: MutableList<Creation> = mutableListOf()
        list.forEach {
            if(it.name.toLowerCase().contains(creationName.toLowerCase())){
                filteredList.add(it)
            }
        }
        adapter.models = filteredList
    }

}