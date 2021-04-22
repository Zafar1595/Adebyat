package space.adebyat.adebyat.ui.author

import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.data.firebase.FirebaseManager

class AuthorPresenter(private val firebase: FirebaseManager) {
    lateinit var view: AuthorView

    fun init(view: AuthorView) {
        this.view = view
    }

    fun getAllAuthors() {
        view.setLoading(true)
        firebase.getAuthors(
            { authorList->
                val mList: MutableList<Author> = mutableListOf()
                authorList.forEach { author->
                    author.name?.let { mList.add(author) }
                }
                view.setData(mList)
            },
            {
                view.showMessage(it)
            }
        )
    }
}