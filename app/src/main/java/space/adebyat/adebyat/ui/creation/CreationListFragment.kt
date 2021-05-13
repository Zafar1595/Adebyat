package space.adebyat.adebyat.ui.creation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme
import space.adebyat.adebyat.databinding.FragmentCreationListBinding
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity
import java.util.*

class CreationListFragment : Fragment(R.layout.fragment_creation_list), CreationView {

    private var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    lateinit var binding: FragmentCreationListBinding
    private var list: List<Creation> = listOf()
    private var adapterTheme = ThemeAdapter()
    private val args: CreationListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreationListBinding.bind(view)
        binding.recyclerViewCreationList.adapter = adapter

        binding.recyclerViewThemes.adapter = adapterTheme

        val name = args.name

        presenter.init(this)
        presenter.getCreation(name)
        presenter.getThemes()

        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            presenter.viewedIncrement(it)
            view.context.startActivity(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrEmpty()) {
                    search(text)
                }
                return false
            }
        })

        val themeList: MutableList<String> = mutableListOf()
        adapterTheme.setOnItemClickListener { theme ->
            if (!themeList.contains(theme)) {
                themeList.add(theme)
            } else {
                themeList.remove(theme)
            }
            searchTheme(themeList)
        }

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
        if (loading) {
            binding.progressBarCreationList.visibility = View.VISIBLE
        } else {
            binding.progressBarCreationList.visibility = View.GONE
        }
    }

    override fun setThemes(themes: List<Theme>) {
        adapterTheme.models = themes
    }

    fun search(creationName: String) {
        val filteredList: MutableList<Creation> = mutableListOf()
        list.forEach {
            if (it.name.toLowerCase(Locale.ROOT).contains(creationName.toLowerCase(Locale.ROOT))) {
                filteredList.add(it)
            }
        }
        adapter.models = filteredList
    }

    private fun searchTheme(theme: List<String>) {
        val themeList: MutableList<Creation> = mutableListOf()
        Log.d("themeEvent", "поиск")

        list.forEach {
            if (it.theme?.containsAll(theme) == true) {
                themeList.add(it)
            }
        }
        adapter.models = themeList
    }
}