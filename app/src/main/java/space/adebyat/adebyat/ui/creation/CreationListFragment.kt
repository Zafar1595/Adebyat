package space.adebyat.adebyat.ui.creation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.databinding.FragmentCreationListBinding
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class CreationListFragment: Fragment(R.layout.fragment_creation_list), CreationView{

    private var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    lateinit var binding: FragmentCreationListBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreationListBinding.bind(view)
        binding.recyclerViewCreationList.adapter = adapter
        binding.recyclerViewCreationList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        var str: String = arguments?.get("name") as String
        Toast.makeText(view.context, str, Toast.LENGTH_LONG).show()

        presenter.init(this)
        presenter.getCreation(str)

        adapter.setOnItemClickListener {
            val intent = Intent(view.context, CreationWindowActivity::class.java)
            intent.putExtra("Creation", it)
            view.context.startActivity(intent)
        }
    }

    override fun setCreation(creation: List<Creation>) {
        adapter.models = creation
        setLoading(false)
    }

    override fun showMessage(msg: String?) {
        TODO("Not yet implemented")
    }

    override fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBarCreationList.visibility = View.VISIBLE
        }else{
            binding.progressBarCreationList.visibility = View.GONE
        }
    }
}