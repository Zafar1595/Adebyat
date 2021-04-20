package space.adebyat.adebyat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

class SplashScreen: Fragment(R.layout.splash_screen) {
    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        Handler(Looper.getMainLooper()).postDelayed({
//            val action = SplashScreenDirections.actionSplashScreenToNavigationFilter()
//            navController.navigate(action)
//        }, 1000)
    }

}