package space.adebyat.adebyat.ui.creation.poetry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.databinding.FragmentPoetryBinding
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.CreationPresenter
import space.adebyat.adebyat.ui.creation.CreationView
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowActivity

class FragmentPoetry: Fragment(R.layout.fragment_poetry), CreationView {
    lateinit var binding: FragmentPoetryBinding
    private val adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPoetryBinding.bind(view)
        binding.recyclerViewPoetry.adapter = adapter
        binding.recyclerViewPoetry.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.init(this)
        presenter.getCreation("Poeziya")
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
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBarPoetry.visibility = View.VISIBLE
        }else{
            binding.progressBarPoetry.visibility = View.GONE
        }
    }
}