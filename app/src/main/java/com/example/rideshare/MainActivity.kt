package com.example.rideshare


import PaymentActivity
import ScreenMain
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rideshare.ui.theme.RideShareTheme
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.elevation.SurfaceColors

import com.google.firebase.FirebaseApp
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener

private lateinit var auth:FirebaseAuth


class MainActivity : ComponentActivity()  {
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rideshare"
            val descriptionText = "Booked success"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        PaymentActivity()
      //  val checkout=Checkout()
       // Checkout.preload(applicationContext)
      //  checkout.setKeyID("rzp_test_URMynHVgQvebhO")
    //    Checkout.sdkCheckIntegration(activity)
        auth = Firebase.auth
        val user=auth.currentUser
        preferenceManager = PreferenceManager(this)
        setContent{

            RideShareTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                        ScreenMain()
                    }
                }
            }
        }
    }



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RideShareTheme {
        ScreenMain()
    }
}
