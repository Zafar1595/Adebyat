package space.adebyat.adebyat.ui.creation.prose

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

class FragmentProse: Fragment(R.layout.fragment_prose), CreationView {
    var adapter = CreationAdapter()
    private val presenter: CreationPresenter by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_prose)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        presenter.init(this)
        presenter.getCreation("prose")
    }

    override fun setCreation(creation: List<Creation>) {
        adapter.models = creation
    }

    override fun showMessage(msg: String?) {
        TODO("Not yet implemented")
    }
}