package space.adebyat.adebyat.ui.creation.creation_window

import space.adebyat.adebyat.data.Creation

interface CreationWindowModelView {
    fun setData(creation: Creation)
    fun setLoading(loading: Boolean)
    fun showMessage(msg: String?)

}