import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.rideshare.Routes
import com.example.rideshare.Screen.AddRide
import com.example.rideshare.Screen.BookedUser
import com.example.rideshare.Screen.DetailView
import com.example.rideshare.Screen.HomeScreen
import com.example.rideshare.Screen.LoginScreen
import com.example.rideshare.Screen.ProfileScreen
import com.example.rideshare.Screen.Resetpassword
import com.example.rideshare.Screen.Ridehistory
import com.example.rideshare.Screen.SharedViewModel
import com.example.rideshare.Screen.SignupScreen
import com.example.rideshare.Screen.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Preview
@Composable
fun ScreenMain(){
    val sharedViewModel = remember { SharedViewModel() }
    val navController = rememberNavController()
    val auth= FirebaseAuth.getInstance()
    val user=auth.currentUser
    val startRoute = if(user != null) Routes.Home else Routes.Splash
    NavHost(navController = navController, startDestination = startRoute.route) {

        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)

        }
        composable(Routes.Signup.route ) {

            SignupScreen(navController = navController)
        }
        composable(Routes.Profile.route )
        {

           ProfileScreen(navController = navController)
//                backStackEntry ->
//
//            // Extracting the argument
//            val parameter = backStackEntry.arguments?.getString("parameter")


          // ProfileScreen(navController = navController, name = parameter)
            }


        //composable(Routes.Home.route +"/{parameter}",arguments = listOf(navArgument("parameter") { type = NavType.StringType })) {
        composable(Routes.Home.route ) {
//                backStackEntry ->
//            val parameter = backStackEntry.arguments?.getString("parameter") ?: ""
            HomeScreen(navController = navController)
        }
        composable(Routes.AddRide.route) {
            AddRide(navController = navController)
        }
        composable(Routes.BookedUser.route) {
            BookedUser(navController = navController)
        }
        composable(Routes.Resetpassword.route) {
            Resetpassword(navController = navController)
        }
        composable(Routes.Ridehistroy.route) {
            Ridehistory(navController = navController)
        }
        composable(Routes.DetailView.route+"/{source}/{destination}/{firstname}/{lastname}/{email}/{vehicle}/{phoneNumber}/{seats}/{date}/{time}/{price}"){
            backStackEntry ->
       val param = backStackEntry.arguments?.getString("source") ?: ""
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val seats = backStackEntry.arguments?.getString("seats")?.toIntOrNull()
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""
            val firstName = backStackEntry.arguments?.getString("firstname") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastname") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val vehicle = backStackEntry.arguments?.getString("vehicle") ?: ""
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""

            DetailView(
                    navController = navController,
                    source =param, destination = destination,
                    firstName=firstName,
                    lastName=lastName,
                    email=email,
                    vehicleNumber=vehicle,
                    phoneNumber=phoneNumber,
                    seats=seats,
                    date=date,
                    time=time,
                    price=price
                    )

        }
    }
}


