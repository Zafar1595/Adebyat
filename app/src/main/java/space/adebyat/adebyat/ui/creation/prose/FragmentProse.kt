package space.adebyat.adebyat.ui.creation.prose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
import java.util.*

class FragmentProse : Fragment(R.layout.fragment_prose), CreationView {
    lateinit var binding: FragmentProseBinding
    private var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    private var list: List<Creation> = listOf()
    private var adapterTheme = ThemeAdapter()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProseBinding.bind(view)
        binding.recyclerViewProse.adapter = adapter
        binding.recyclerViewThemes.adapter = adapterTheme
        presenter.init(this)
        presenter.getCreation("Proza")
        presenter.getThemes()
        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            presenter.viewedIncrement(it)
            view.context.startActivity(intent)
        }

        val themeList: MutableList<String> = mutableListOf()
        adapterTheme.setOnItemClickListener {
            if (!themeList.contains(it)) {
                themeList.add(it)
            } else {
                themeList.remove(it)
            }
            searchTheme(themeList)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        if (loading) {
            binding.progressBarProse.visibility = View.VISIBLE
        } else {
            binding.progressBarProse.visibility = View.GONE
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
        if (theme.isEmpty()) {
            adapter.models = list
        } else {
            adapter.models = themeList
        }
//        adapter.models = themeList
    }
}