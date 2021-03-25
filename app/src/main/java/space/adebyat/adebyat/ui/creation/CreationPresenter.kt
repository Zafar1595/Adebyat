package space.adebyat.adebyat.ui.creation

import space.adebyat.adebyat.data.DataModel
import space.adebyat.adebyat.data.firebase.FirebaseManager
import space.adebyat.adebyat.ui.author.AuthorView

class CreationPresenter(private val firebase: FirebaseManager) {
    lateinit var view: CreationView
    fun init(view: CreationView) {
        this.view = view
    }
    fun getCreation(direction: String){

    }
    fun getCreationAuthor(authorName: String){
        firebase.getCreationAuthors(
            {
                view.setCreation(it)
            },
            {
                view.showMessage(it)
            }
        )
    }
}