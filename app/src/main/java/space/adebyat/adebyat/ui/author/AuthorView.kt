package space.adebyat.adebyat.ui.author

import space.adebyat.adebyat.data.Autor

interface AuthorView {
    fun setData(autors: List<Autor>)
    fun showMessage(msg: String?)
    fun setLoading(loading: Boolean)
}