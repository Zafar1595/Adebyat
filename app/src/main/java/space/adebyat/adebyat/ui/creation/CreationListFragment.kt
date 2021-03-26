package space.adebyat.adebyat.ui.creation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation

class CreationListFragment: Fragment(R.layout.fragment_creation_list), CreationView{

    private var adapter = CreationAdapter()
    private  val presenter: CreationPresenter by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_creation_list)
        recyclerView.adapter = adapter

        var str: String = arguments?.get("name") as String

        presenter.init(this)
        presenter.getCreation(str)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        Toast.makeText(view.context, str, Toast.LENGTH_LONG).show()

    }

    override fun setCreation(creation: List<Creation>) {
        adapter.models = creation
    }

    override fun showMessage(msg: String?) {
        TODO("Not yet implemented")
    }
}