package space.adebyat.adebyat.ui.creation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme
import space.adebyat.adebyat.databinding.FragmentCreationListBinding
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class CreationListFragment: Fragment(R.layout.fragment_creation_list), CreationView{

    private var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    lateinit var binding: FragmentCreationListBinding
    private var list: List<Creation> = listOf()
    private var adapterTheme = ThemeAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreationListBinding.bind(view)
        binding.recyclerViewCreationList.adapter = adapter
        binding.recyclerViewCreationList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewThemes.adapter = adapterTheme


        var str: String = arguments?.get("name") as String
        Toast.makeText(view.context, str, Toast.LENGTH_LONG).show()

        presenter.init(this)
        presenter.getCreation(str)
        presenter.getThemes()

        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            //intent.putExtra("Creation", it.name)
            intent.putExtra("creationName", it.name)
            intent.putExtra("creationContent", it.content)
            intent.putExtra("creationUrl", it.audioUrl)
            view.context.startActivity(intent)
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
            binding.progressBarCreationList.visibility = View.VISIBLE
        }else{
            binding.progressBarCreationList.visibility = View.GONE
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

}