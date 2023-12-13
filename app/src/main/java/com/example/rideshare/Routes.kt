package com.example.rideshare

sealed class Routes(val route: String) {
   object Splash : Routes("SplashScreen")
   object Login : Routes("LoginScreen")
    object Signup : Routes("SignupScreen")
    object Profile : Routes("ProfileScreen")
    object Home : Routes("HomeScreen")
    object AddRide : Routes("AddRide")
    object DetailView : Routes("DetailView")
   object Resetpassword : Routes("Resetpassword")
    object Ridehistroy : Routes("Ridehistroy")


    object BookedUser : Routes("BookedUser")
}