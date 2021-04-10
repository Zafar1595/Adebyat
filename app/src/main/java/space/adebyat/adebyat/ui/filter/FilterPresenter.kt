package space.adebyat.adebyat.ui.filter

import space.adebyat.adebyat.data.firebase.FirebaseManager
import space.adebyat.adebyat.ui.author.AuthorView

class FilterPresenter(private val firebase: FirebaseManager) {
    lateinit var view: FilterView

    fun init(view: FilterView) {
        this.view = view
    }

    fun getAllCreations(){
        view.setLoading(true)
        firebase.getAllCreations(
                {
                    view.setCreations(it)
                },
                {
                    view.showMessage(it)
                }
        )
    }
}