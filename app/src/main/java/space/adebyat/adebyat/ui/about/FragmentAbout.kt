package space.adebyat.adebyat.ui.about

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.About
import space.adebyat.adebyat.databinding.FragmentAboutBinding
import space.adebyat.adebyat.databinding.FragmentAuthorBinding

class FragmentAbout: Fragment(R.layout.fragment_about), AboutView {
    val presenter: AboutPresenter by inject()
    lateinit var binding: FragmentAboutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutBinding.bind(view)
        presenter.init(this)
        presenter.getAbout()
        setLoading(true)
    }

    override fun setData(about: About) {
        setLoading(false)
        binding.textViewAbout.text =
            HtmlCompat.fromHtml(about.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun setLoading(load: Boolean) {
        if(load) {
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}