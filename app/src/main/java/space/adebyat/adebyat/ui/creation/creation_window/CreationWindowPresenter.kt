package space.adebyat.adebyat.ui.creation.creation_window

import space.adebyat.adebyat.data.firebase.FirebaseManager

class CreationWindowPresenter(private val firebase: FirebaseManager) {
    private lateinit var view: CreationWindowModelView

    fun init(view: CreationWindowModelView) {
        this.view = view
    }

    fun getCreation(name: String){
        view.setLoading(true)
        firebase.getCreation(
            {
                view.setData(it[0])
            },
            {
                view.showMessage(it)
            },
            name
        )
    }
}