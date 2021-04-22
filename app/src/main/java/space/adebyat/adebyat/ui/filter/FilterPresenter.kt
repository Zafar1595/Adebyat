package space.adebyat.adebyat.ui.filter

import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.firebase.FirebaseManager

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
    fun getData(){
        firebase.getThemes(
                {
                    view.setThemes(it)
                },
                {
                    view.showMessage(it)
                }
        )
        firebase.getAuthors(
                {
                    val mList: MutableList<Author> = mutableListOf()
                    it.forEach {
                        if(it.name != null){
                            mList.add(it)
                        }
                    }
                    view.setAuthors(mList)
                },
                {
                    view.showMessage(it)
                }
        )
        firebase.getDirections(
                {
                    view.setDirections(it)
                },
                {
                    view.showMessage(it)
                }
        )
        firebase.getGenre(
                {
                    view.setGenre(it)
                },
                {
                    view.showMessage(it)
                }
        )
        firebase.getPeriod(
                {
                    view.setPeriod(it)
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