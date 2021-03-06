package space.adebyat.adebyat.ui.creation

import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.firebase.FirebaseManager

class CreationPresenter(private val firebase: FirebaseManager) {
    lateinit var view: CreationView
    fun init(view: CreationView) {
        this.view = view
    }

    fun getCreation(columnName: String){
        view.setLoading(true)
        firebase.getCreations(
            {
                view.setCreation(it)
            },
            {
                view.showMessage(it)
            },
                columnName
        )
    }

    fun getThemes(){
        firebase.getThemes(
                {
                    view.setThemes(it)
                },
                {
                    view.showMessage(it)
                }
        )
    }

    fun viewedIncrement(creation: Creation){
        creation.viewed++
        firebase.viewedIncrement(creation)
    }

}