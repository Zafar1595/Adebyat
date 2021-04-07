package space.adebyat.adebyat.ui.creation.prose

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme
import space.adebyat.adebyat.databinding.FragmentProseBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.CreationPresenter
import space.adebyat.adebyat.ui.creation.CreationView
import space.adebyat.adebyat.ui.creation.ThemeAdapter
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class FragmentProse: Fragment(R.layout.fragment_prose), CreationView {
    lateinit var binding: FragmentProseBinding
    private var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    private var list: List<Creation> = listOf()
    private var adapterTheme = ThemeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProseBinding.bind(view)
        binding.recyclerViewProse.adapter = adapter
        binding.recyclerViewProse.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewThemes.adapter = adapterTheme
        presenter.init(this)
        presenter.getCreation("Proza")
        presenter.getThemes()
        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("Creation", it.name)
            view.context.startActivity(intent)
        }
        adapterTheme.setOnItemClickListener {
            //searchTheme(it)
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
    }

    override fun setCreation(creation: List<Creation>) {
        list = creation
        adapter.models = creation
        setLoading(false)
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBarProse.visibility = View.VISIBLE
        }else{
            binding.progressBarProse.visibility = View.GONE
        }
    }

    override fun setThemes(themes: List<Theme>) {
        adapterTheme.models = themes
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

    fun searchTheme(theme: String){
        var filterList: MutableList<Creation> = mutableListOf()
        list.forEach {
            it.theme.forEach { it1->
                if(it1.toLowerCase().contains(theme.toLowerCase())){
                    filterList.add(it)
                }
            }
        }
        adapter.models = filterList
    }
}