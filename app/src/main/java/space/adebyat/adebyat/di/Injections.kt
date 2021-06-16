package space.adebyat.adebyat.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import space.adebyat.adebyat.data.firebase.FirebaseManager
import space.adebyat.adebyat.ui.about.AboutPresenter
import space.adebyat.adebyat.ui.author.AuthorPresenter
import space.adebyat.adebyat.ui.creation.CreationPresenter
import space.adebyat.adebyat.ui.filter.FilterPresenter

val dataModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseManager(get()) }
}

val presenterModule = module {
    single { AuthorPresenter(get()) }
    single { CreationPresenter(get()) }
    single { FilterPresenter(get()) }
    single { AboutPresenter(get()) }
}