package space.adebyat.adebyat.ui.filter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.*
import space.adebyat.adebyat.databinding.FragmentFilterBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.ThemeAdapter
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class FragmentFilter: Fragment(R.layout.fragment_filter), FilterView {

    var adapter = CreationAdapter()
    lateinit var binding: FragmentFilterBinding
    private val presenter: FilterPresenter by inject()
    private var list: List<Creation> = listOf()
    private var tempsObject: Creation = Creation()
    var themeList: MutableList<String> = mutableListOf()
    var adapterTheme = ThemeAdapter()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)
        binding.recyclerView.adapter = adapter
        //binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewThemes.adapter = adapterTheme
        presenter.init(this)
        presenter.getAllCreations()
        presenter.getData()
        adapterTheme.setOnItemClickListener { it, view ->
            if(!themeList.contains(it)) {
                themeList.add(it)
                tempsObject.theme.add(it)
                view.findViewById<TextView>(R.id.textViewTheme).setTextAppearance(R.style.textViewStyleOnSelected)
                Log.d("themeEvent", "$it добавлено")
            }else{
                themeList.remove(it)
                view.findViewById<TextView>(R.id.textViewTheme).setTextAppearance(R.style.textViewStyleOnNotSelected)
                Log.d("themeEvent", "$it удалено")
            }
            filterByCriteria()
            //
        }

        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            presenter.viewedIncrement(it)
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
                    filterByName(p0)
                }
                return false
            }
        })

        binding.autoCompleteAuthors.setOnItemClickListener { adapterView, _, i, _ ->
            if(adapterView.getItemAtPosition(i).toString() != "Not selected...") {
                tempsObject.author = adapterView.getItemAtPosition(i).toString()
            }else{
                tempsObject.author = ""
            }
            filterByCriteria()
        }

        binding.autoCompleteDirections.setOnItemClickListener { adapterView, _, i, _ ->
            if(adapterView.getItemAtPosition(i).toString() != "Not selected...") {
                tempsObject.direction = adapterView.getItemAtPosition(i).toString()
            }else{
                tempsObject.direction = ""
            }
            filterByCriteria()
        }
        binding.autoCompleteGenre.setOnItemClickListener { adapterView, _, i, _ ->
            if(adapterView.getItemAtPosition(i).toString() != "Not selected...") {
                tempsObject.genre = adapterView.getItemAtPosition(i).toString()
            }else{
                tempsObject.genre = ""
            }
            filterByCriteria()
        }
        binding.autoCompletePeriod.setOnItemClickListener { adapterView, _, i, _ ->
            if(adapterView.getItemAtPosition(i).toString() != "Not selected...") {
                tempsObject.period = adapterView.getItemAtPosition(i).toString()
            }else{
                tempsObject.period = ""
            }
            filterByCriteria()
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
        adapterTheme.models = themes
    }

    override fun setDirections(directions: List<Direction>) {
        var mList = mutableListOf<String>()
        mList.add("Not selected...")
        directions.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteDirections.setAdapter(spinnerAdapter)
    }

    override fun setGenre(genres: List<Genre>) {
        val mList = mutableListOf<String>()
        mList.add("Not selected...")
        genres.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteGenre.setAdapter(spinnerAdapter)
    }

    override fun setPeriod(periods: List<space.adebyat.adebyat.data.Period>) {
        var mList = mutableListOf<String>()
        mList.add("Not selected...")
        periods.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompletePeriod.setAdapter(spinnerAdapter)
    }

    override fun setAuthors(authors: List<Author>) {
        var mList = mutableListOf<String>()
        mList.add("Not selected...")
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

    fun filterByCriteria(){
        var filterByCriteriaList: MutableList<Creation> = mutableListOf()
        filterByCriteriaList = list.toMutableList()

        if(tempsObject.author != "") { filterByCriteriaList.removeIf { it.author != tempsObject.author } }
        if(tempsObject.direction != "") { filterByCriteriaList.removeIf { it.direction != tempsObject.direction }}
        if(tempsObject.genre != "") { filterByCriteriaList.removeIf { it.genre != tempsObject.genre }}
        if(tempsObject.period != "") {filterByCriteriaList.removeIf { it.period != tempsObject.period }}
        if(themeList != null) {filterByCriteriaList.removeIf { !it.theme.containsAll(themeList)}}
        adapter.models = filterByCriteriaList
    }

    fun filterByName(creationName: String){
        var filteredList: MutableList<Creation> = mutableListOf()
        list.forEach {
            if(it.name.toLowerCase().contains(creationName.toLowerCase())){
                filteredList.add(it)
            }
        }
        adapter.models = filteredList
    }

}