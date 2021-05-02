package space.adebyat.adebyat.ui.about

import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.data.firebase.FirebaseManager
import space.adebyat.adebyat.ui.author.AuthorView

class AboutPresenter(private val firebase: FirebaseManager) {
    lateinit var view: AboutView

    fun init(view: AboutView) {
        this.view = view
    }

    fun getAbout() {
        view.setLoading(true)
        firebase.getAbout {
            view.setData(it)
        }
    }
}