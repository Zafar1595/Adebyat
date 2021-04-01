package space.adebyat.adebyat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import space.adebyat.adebyat.di.dataModule
import space.adebyat.adebyat.di.presenterModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = listOf(dataModule, presenterModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            modules(modules)
        }
    }
}