package space.adebyat.adebyat.ui.filter

import space.adebyat.adebyat.data.*

interface FilterView {
    fun setCreations(creation: List<Creation>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
    fun setThemes(themes: List<Theme>)
    fun setDirections(directions: List<Direction>)
    fun setGenre(genres: List<Genre>)
    fun setPeriod(periods: List<Period>)
    fun setAuthors(authors: List<Author>)
}