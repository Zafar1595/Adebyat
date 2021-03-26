package space.adebyat.adebyat.ui.creation

import space.adebyat.adebyat.data.firebase.FirebaseManager

class CreationPresenter(private val firebase: FirebaseManager) {
    lateinit var view: CreationView
    fun init(view: CreationView) {
        this.view = view
    }

    fun getCreation(columnName: String){
        firebase.getCreation(
            {
                view.setCreation(it)
            },
            {
                view.showMessage(it)
            },
                columnName
        )
    }
}