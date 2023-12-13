package com.example.rideshare.Screen

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.razorpay.Checkout
import org.json.JSONObject


@Composable
fun PaymentScreen(){

}

@Composable
private fun initiatePayment() {
    val context =  LocalContext.current as? Activity
    //  val keyboardController = LocalSoftwareKeyboardController.current

    // Initialize Razorpay Checkout
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_URMynHVgQvebhO")

    //  Set callback listener
    //checkout.setImage(R.drawable.ic_launcher)
    val options = JSONObject()
    options.put("name", "Your App Name")
    options.put("description", "Purchase Description")
    options.put("currency", "INR")
    options.put("amount", "10000")
    options.put("prefill.email", "customer@example.com")
    options.put("prefill.contact", "1234567890")

    checkout.open(context , options)


}