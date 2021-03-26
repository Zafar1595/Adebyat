package space.adebyat.adebyat.ui.author

import space.adebyat.adebyat.data.firebase.FirebaseManager

class AuthorPresenter(private val firebase: FirebaseManager) {
    lateinit var view: AuthorView

    fun init(view: AuthorView) {
        this.view = view
    }

    fun getAllAuthors(){
        view.setLoading(true)
        firebase.getAuthors(
            {
                view.setData(it)
            },
            {
                view.showMessage(it)
            }
        )
    }
}