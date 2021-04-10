package space.adebyat.adebyat.ui.filter

import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme

interface FilterView {
    fun setCreations(creation: List<Creation>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
    fun setThemes(themes: List<Theme>)
}