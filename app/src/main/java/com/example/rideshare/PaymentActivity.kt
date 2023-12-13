import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.example.rideshare.R
import com.example.rideshare.ui.theme.RideShareTheme
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

 class PaymentActivity: Activity(), PaymentResultWithDataListener, ExternalWalletListener {
    // .....

     private val razorpayBroadcastReceiver = object : BroadcastReceiver() {
         override fun onReceive(context: Context?, intent: Intent?) {
             // Handle Razorpay broadcast messages, if needed
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */

        registerReceiver(
            razorpayBroadcastReceiver,
            IntentFilter("com.razorpay.TOKEN_BROADCAST")
        )
        Checkout.preload(applicationContext)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("rzp_test_URMynHVgQvebhO")
    }

     override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
         TODO("Not yet implemented")
     }

     override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
         TODO("Not yet implemented")
     }

     override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
         TODO("Not yet implemented")
     }

     override fun onDestroy() {
         super.onDestroy()

         unregisterReceiver(razorpayBroadcastReceiver)
     }
     //......
}