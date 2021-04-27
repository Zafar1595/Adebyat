package space.adebyat.adebyat.ui.creation

import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme

interface CreationView {
    fun setCreation(creation: List<Creation>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
    fun setThemes(themes: List<Theme>)
}