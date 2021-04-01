package space.adebyat.adebyat.ui.author

import space.adebyat.adebyat.data.Author

interface AuthorView {
    fun setData(authors: List<Author>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
}