package space.adebyat.adebyat.ui.creation.poetry

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.CreationPresenter
import space.adebyat.adebyat.ui.creation.CreationView

class FragmentPoetry: Fragment(R.layout.fragment_poetry), CreationView {
    var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_poetry)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.init(this)
        presenter.getCreation("Poeziya")
    }

    override fun setCreation(creation: List<Creation>) {
        adapter.models = creation
    }

    override fun showMessage(msg: String?) {
        TODO("Not yet implemented")
    }
}