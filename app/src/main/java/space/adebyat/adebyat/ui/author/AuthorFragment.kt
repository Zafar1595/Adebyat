package space.adebyat.adebyat.ui.author

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.databinding.FragmentAuthorBinding

class AuthorFragment : Fragment(R.layout.fragment_author), AuthorView {

    private var adapter = AuthorAdapter()
    private val presenter: AuthorPresenter by inject()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAuthorBinding
    private var list: List<Author> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorBinding.bind(view)
        binding.recyclerViewAuthor.adapter = adapter
        presenter.init(this)
        presenter.getAllAuthors()
        navController = Navigation.findNavController(view)

        adapter.setOnItemClickListener {
            val action = AuthorFragmentDirections.actionNavigationAuthorToFragmentCreationList(it)
            navController.navigate(action)
        }
        adapter.setOnItemClickListenerAuthorInfo {
            val intent = Intent(view.context, AuthorInfoActivity::class.java)
            intent.putExtra("bio", it.bio)
            intent.putExtra("imageUrl", it.image_url)
            startActivity(intent)
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

    override fun setLoading(loading: Boolean) {
        if (loading) {
            binding.progressBarAuthor.visibility = View.VISIBLE
        } else {
            binding.progressBarAuthor.visibility = View.GONE
        }
    }

    override fun setData(authors: List<Author>) {
        list = authors
        adapter.models = authors
        setLoading(false)
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun search(authorName: String) {
        var filteredList: MutableList<Author> = mutableListOf()
        list.forEach {
            if (it.name.toLowerCase().contains(authorName.toLowerCase())) {
                filteredList.add(it)
            }
        }

        adapter.models = filteredList
    }
}