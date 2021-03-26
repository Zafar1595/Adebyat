package space.adebyat.adebyat.ui.creation

import space.adebyat.adebyat.data.Creation

interface CreationView {
    fun setCreation(creation: List<Creation>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
}