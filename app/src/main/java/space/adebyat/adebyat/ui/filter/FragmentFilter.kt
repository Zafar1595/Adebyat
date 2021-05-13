package space.adebyat.adebyat.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.*
import space.adebyat.adebyat.databinding.FragmentFilterBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.ThemeAdapter
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity
import java.util.*

class FragmentFilter : Fragment(R.layout.fragment_filter), FilterView {

    var adapter = CreationAdapter()
    private lateinit var binding: FragmentFilterBinding
    private val presenter: FilterPresenter by inject()
    private var list: List<Creation> = listOf()
    private var tempsObject: Creation = Creation()
    private var themeList: MutableList<String> = mutableListOf()
    private var adapterTheme = ThemeAdapter()

    companion object{
        const val SAYLANBADI = "Saylanbadı..."
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)
        binding.recyclerView.adapter = adapter
        binding.recyclerViewThemes.adapter = adapterTheme
        presenter.init(this)
        presenter.getAllCreations()
        presenter.getData()
        adapterTheme.setOnItemClickListener {
            if (!themeList.contains(it)) {
                themeList.add(it)
                tempsObject.theme?.add(it)
            } else {
                themeList.remove(it)
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
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            if (adapterView.getItemAtPosition(i).toString() != SAYLANBADI) {
                tempsObject.author = adapterView.getItemAtPosition(i).toString()
            } else {
                tempsObject.author = ""
            }
            filterByCriteria()
        }

        binding.autoCompleteDirections.setOnItemClickListener { adapterView, _, i, _ ->
            if (adapterView.getItemAtPosition(i).toString() != SAYLANBADI) {
                tempsObject.direction = adapterView.getItemAtPosition(i).toString()
            } else {
                tempsObject.direction = ""
            }
            filterByCriteria()
        }
        binding.autoCompleteGenre.setOnItemClickListener { adapterView, _, i, _ ->
            if (adapterView.getItemAtPosition(i).toString() != SAYLANBADI) {
                tempsObject.genre = adapterView.getItemAtPosition(i).toString()
            } else {
                tempsObject.genre = ""
            }
            filterByCriteria()
        }
        binding.autoCompletePeriod.setOnItemClickListener { adapterView, _, i, _ ->
            if (adapterView.getItemAtPosition(i).toString() != SAYLANBADI) {
                tempsObject.period = adapterView.getItemAtPosition(i).toString()
            } else {
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
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun setThemes(themes: List<Theme>) {
        adapterTheme.models = themes
    }

    override fun setDirections(directions: List<Direction>) {
        val mList = mutableListOf<String>()
        mList.add("Saylanbadı...")
        directions.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteDirections.setAdapter(spinnerAdapter)
    }

    override fun setGenre(genres: List<Genre>) {
        val mList = mutableListOf<String>()
        mList.add(SAYLANBADI)
        genres.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteGenre.setAdapter(spinnerAdapter)
    }

    override fun setPeriod(periods: List<Period>) {
        val mList = mutableListOf<String>()
        mList.add(SAYLANBADI)
        periods.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompletePeriod.setAdapter(spinnerAdapter)
    }

    override fun setAuthors(authors: List<Author>) {
        val mList = mutableListOf<String>()
        mList.add(SAYLANBADI)
        authors.forEach {
            mList.add(it.name)
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mList)
        binding.autoCompleteAuthors.setAdapter(spinnerAdapter)
    }

    private fun selectionSort(list: List<Creation>): List<Creation> {
        val listSorted: MutableList<Creation> = list as MutableList<Creation>
        if (list.size > 1) {
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
                val temp = listSorted[i]
                listSorted[i] = max
                listSorted[maxId] = temp
            }
        }
        return listSorted
    }

    private var visivle = false
    private fun filterContainer() {
        if (!visivle) {
            binding.filterContainer.visibility = View.VISIBLE
            visivle = true
        } else {
            binding.filterContainer.visibility = View.GONE
            visivle = false
        }
    }

    private fun filterByCriteria() {
        var filterByCriteriaList: MutableList<Creation> = list.toMutableList()

        if (tempsObject.author.isNotEmpty()) {
            filterByCriteriaList = filterByCriteriaList.
            filter { it.author == tempsObject.author } as MutableList<Creation>
        }
        if (tempsObject.direction.isNotEmpty()) {
            filterByCriteriaList = filterByCriteriaList.
            filter { it.direction == tempsObject.direction } as MutableList<Creation>
        }
        if (tempsObject.genre.isNotEmpty()) {
            filterByCriteriaList = filterByCriteriaList.
            filter { it.genre == tempsObject.genre } as MutableList<Creation>
        }
        if (tempsObject.period.isNotEmpty()) {
            filterByCriteriaList = filterByCriteriaList.
            filter { it.period == tempsObject.period } as MutableList<Creation>
        }
        if (themeList.isNotEmpty()) {
            filterByCriteriaList = filterByCriteriaList.
            filter { it.theme?.containsAll(themeList) == true } as MutableList<Creation>
        }
        adapter.models = filterByCriteriaList
    }

    fun filterByName(creationName: String) {
        val filteredList: MutableList<Creation> = mutableListOf()
        list.forEach {
            if (it.name.toLowerCase(Locale.ROOT).contains(creationName.toLowerCase(Locale.ROOT))) {
                filteredList.add(it)
            }
        }
        adapter.models = filteredList
    }
}