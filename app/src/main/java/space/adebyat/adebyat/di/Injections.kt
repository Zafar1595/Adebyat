package space.adebyat.adebyat.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module
import space.adebyat.adebyat.data.firebase.FirebaseManager
import space.adebyat.adebyat.ui.author.AuthorAdapter
import space.adebyat.adebyat.ui.author.AuthorPresenter
import space.adebyat.adebyat.ui.creation.CreationAdapter
import space.adebyat.adebyat.ui.creation.CreationPresenter
import space.adebyat.adebyat.ui.creation.creation_window.CreationWindowPresenter

val dataModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseManager(get()) }
}

val presenterModule = module {
    single { AuthorPresenter(get()) }
    single { CreationPresenter(get()) }
    single { CreationWindowPresenter(get()) }
}
