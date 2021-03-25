package space.adebyat.adebyat.ui.author

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Autor
import space.adebyat.adebyat.databinding.FragmentAuthorBinding

class AuthorFragment: Fragment(R.layout.fragment_author), AuthorView {

    private var adapter = AuthorAdapter()
    private val presenter: AuthorPresenter by inject()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAuthorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorBinding.bind(view)
        binding.recyclerViewAuthor.adapter = adapter
        presenter.init(this)
        presenter.getAllAuthors()
        navController = Navigation.findNavController(view)

        adapter.setOnItemClickListener{
            val action = AuthorFragmentDirections.actionNavigationAuthorToFragmentCreationList(it)
            navController.navigate(action)
        }

    }

    override fun setLoading(loading: Boolean) {
//        if(loading) {
//            binding.progressBarAuthor.visibility = View.VISIBLE
//        }else{
//            binding.progressBarAuthor.visibility = View.GONE
//        }
    }

    override fun setData(autors: List<Autor>) {
        adapter.models = autors
        //setLoading(false)
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}