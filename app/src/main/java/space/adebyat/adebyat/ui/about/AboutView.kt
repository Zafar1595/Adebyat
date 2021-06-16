package space.adebyat.adebyat.ui.about

import space.adebyat.adebyat.data.About

interface AboutView {
    fun setData(about: About)
    fun setLoading(load: Boolean)
}