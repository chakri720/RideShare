package com.example.rideshare.Screen

import Notificationservice
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

import com.example.rideshare.R
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.razorpay.Checkout

import org.json.JSONObject



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DetailView(
    navController: NavHostController,
    source:String,
    destination:String,
    firstName:String,
    lastName:String,
    email:String,
    vehicleNumber: String,
    phoneNumber:String,
    seats:Int?,
    date:String,
    time:String,
    price:String
    ){
    var bookeduserFName by remember { mutableStateOf("John Doe") }
    var bookeduserLName by remember { mutableStateOf("John Doe") }
    var bookeduserEmail by remember { mutableStateOf("john.doe@example.com") }
    var bookeduserPhone by remember { mutableStateOf("123-456-7890") }
    var isEditing by remember { mutableStateOf(false) }
    var showPayment by remember { mutableStateOf(false) }
    var seat by remember { mutableStateOf(0) }
   val selectedSeat= remember {
       mutableStateListOf<String>()

   }


    //val selectedItem = sharedViewModel.selectedItem
  //  Log.d("data)
   // val context = LocalContext.current
    var paymentStatus by remember { mutableStateOf<PaymentStatus?>(null) }
    val activity = LocalContext.current as? Activity
    //val keyboardController = LocalSoftwareKeyboardController.current
    val auth= FirebaseAuth.getInstance()
    val user=auth.currentUser
    val userId = user!!.uid
    val databaseReference = Firebase.database.getReference("UserInfo").child(userId).child("BookedInfo").push()
    // Initialize Razorpay Checkout
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_URMynHVgQvebhO")
    var fcmToken by remember { mutableStateOf("") }


    getFcmToken { token ->
        fcmToken = token
        // You can now use the FCM token for sending push notifications
        Log.d("FCM Token", token)
    }
    val context= LocalContext.current
    val notification=Notificationservice(context)
    LaunchedEffect(Unit) {
        val auth= FirebaseAuth.getInstance()
        val user=auth.currentUser
        val userId = user!!.uid
        val database= Firebase.database
        val userInf = database.getReference("UserInfo").child(userId).child("RegisterUser")

        userInf.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(userProfile::class.java)
                Log.d("profile", "Value is: $value")
                if (value != null) {
                    bookeduserFName = value.FirstName
                    bookeduserLName = value.LastName
                    bookeduserPhone = value.PhoneNumber
                    bookeduserEmail = value.Email
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    var cxt=LocalContext.current

    var book by remember { mutableStateOf(false) }
    if (book) {
        LaunchedEffect(book) {
            navController.popBackStack()
        }
    }

    Surface(modifier= Modifier
        .fillMaxSize()

    )
    {
        Scaffold(
            topBar = {

                TopAppBar(
                    title = {
                        Text(text = "Ride Details", color = Color.White)
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = colorResource(id = R.color.colorBlue)
                    ),

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                )
            },

            content = {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                           // .padding(16.dp)
                    ) {
                       Spacer(modifier = Modifier.height(50.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(colorResource(id = R.color.colorBlue))
                        ) {
                        Image(
                            painter = painterResource(id = R.drawable.carpng2),
                            contentDescription = "User Image",
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .size(220.dp)
                                .clip(CircleShape)
                                //.background(Color.Gray)
                                .clickable {

                                    //  launcher.launch("image/*")
                                }
                                .padding(8.dp),
                            // contentScale = ContentScale.Crop
                        )}

                        Row( modifier = Modifier
                            .fillMaxWidth()
                            .background(colorResource(id = R.color.white))
                            .padding(0.dp),

                            verticalAlignment = Alignment.CenterVertically){

                            Column(modifier=Modifier.padding(16.dp)) {
                                //  Text(text = "Name", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                                //  TextField(text = value, fontWeight = FontWeight.Bold)

                                Text(text = "Driver Information", fontSize = 20.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    // Icon(Icons.Default.LocationSearching)
                                    Text(text =" Name $firstName $lastName", fontSize = 16.sp)
                                }

                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = "Phone number $phoneNumber", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.BikeScooter,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = "Vehicle number $vehicleNumber", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text ="Email $email", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))


                                Text(text = "Ride Information", fontSize = 20.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.LocationSearching,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    // Icon(Icons.Default.LocationSearching)
                                    Text(text =" Source point $source", fontSize = 16.sp)
                                }

                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = "Destination point $destination", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = "Ride date $date", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.LockClock,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text ="Ride time $time", fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))

                                        Text(text="Ride Price $ $price")

                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.AirlineSeatReclineNormal,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))

                                    Text(text="Available seats $seats")

                                }
                                Spacer(modifier = Modifier.height(50.dp))
                                Row {
//                                    Icon(
//                                        imageVector = Icons.Default.AirlineSeatReclineNormal,
//                                        contentDescription = null
//                                    )
                                   // Spacer(modifier = Modifier.width(20.dp))
                                  //  Text(text = "Available Seats", fontSize = 20.sp,color=Color.Gray)
                                }
                                //Spacer(modifier = Modifier.height(50.dp))
//                                Row {
////                                    Icon(
////                                        imageVector = Icons.Default.AirlineSeatReclineNormal,
////                                        contentDescription = null
////                                    )
//                                    Spacer(modifier = Modifier.width(20.dp))
//                                  //  Text(text = "Book your Seats", fontSize = 16.sp, color = Color.Black)
//                                }
//                                Spacer(modifier = Modifier.height(30.dp))
//                             var da=seats
//                                for(i in 1 .. 1){
//                                    Row(modifier=Modifier.align(Alignment.CenterHorizontally)){
//                                        for(j in 1..da!!){
//                                            val seatNumber="${(4+i).toChar()}$j"
//                                            SeatComp(
//                                                isEnabled = i!=6,
//                                                isSelected =selectedSeat.contains(seatNumber),
//                                                seatNumber=seatNumber
//                                            ){
//                                                selected,seat->
//                                                if(selected){
//                                                    selectedSeat.remove(seat)
//                                                }
//                                                else{
//                                                    selectedSeat.add(seat)
//                                                }
//                                            }
//                                            if(j!=8) Spacer(modifier=Modifier.width(if(j==4)16.dp else 8.dp))
//                                        }
//                                    }
//
//                                }
                                Spacer(modifier = Modifier.height(50.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(text = "Add seats")
                                    Spacer(modifier = Modifier.width(25.dp))
                                    CountButton(
                                        icon = Icons.Default.Remove,
                                        onClick = { if (seat > 0) seat-- }
                                    )

                                    Spacer(modifier = Modifier.width(25.dp))
                                    Text(text = "$seat", )
                                    Spacer(modifier = Modifier.width(25.dp))
                                    CountButton(

                                        icon = Icons.Default.Add,
                                        onClick = { seat++ }
                                    )
                                }


                                Spacer(modifier = Modifier.height(16.dp))



                                Column {
//                                    if(showPayment) {
//                                        initiatePayment()
//                                        showPayment=false
//                                    }
                                    PaymentStatusMessage(paymentStatus)
                                    Spacer(modifier = Modifier.height(30.dp))

                                    Button(
                                        shape = RectangleShape,
                                        onClick = {
                                            notification.showBasicNotification()
                    if (seat!!>seats!!){
                      Toast.makeText(cxt, "Please choose available seats only", Toast.LENGTH_LONG).show()


                                } else {

    val user = auth.currentUser
    val userId = user!!.uid

    val AddRide = BookableItem(
        Source = source,
        Destination = destination,
        FirstName=firstName,
        LastName=lastName,
        Email=email,
        vehicleNumber=vehicleNumber,
        PhoneNumber=phoneNumber,
        seatNumber = seat,
        Date = date,
        Time = time,
        isBooked = true,
        bookedByUserId = userId,
        Price = price,


    )

    databaseReference.addValueEventListener(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            databaseReference.setValue(AddRide)

            Log.d("Success", "$snapshot")
            book = true
            Toast.makeText(cxt, "Booked Success.", Toast.LENGTH_LONG).show()

        }

        override fun onCancelled(error: DatabaseError) {

            Log.d("Error", "$error")
            Toast.makeText(cxt, "$error", Toast.LENGTH_LONG).show()

        }
    })
}
                                        },

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            // .padding(15.dp)
                                            .background(colorResource(id = R.color.colorBlue))
                                            .heightIn(48.dp),
                                    ) {
                                        Text(text = "Book", color = Color.White, fontSize = 16.sp)
                                    }
                                }
                            }


                    }

                }

            }
        ) }}








@Composable
fun SeatComp(
    isEnabled:Boolean=false,
    isSelected:Boolean=false,
    seatNumber:String="",
    onClick:(Boolean,String)->Unit={_,_->},
) {
    val seatColor = when {
        !isEnabled -> Color.Gray
        isSelected -> Color.Yellow
        else -> Color.White
    }
    val textColor = when {
        isSelected -> Color.White
        else -> Color.Black
    }
    Box(modifier = Modifier
        .size(32.dp)
        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .background(color = seatColor)
        .clickable { onClick(isSelected, seatNumber); }
        .padding(8.dp), contentAlignment = Alignment.Center) {
        Text(
            seatNumber,
            //color=textColor)
        )
    }
    fun sendNotification() {
        val registrationTokens = mutableListOf("your_device_fcm_token")

        try {
            val response = FirebaseMessaging.getInstance()
            Log.d("Push Notification", "Successfully sent message: $response")
        } catch (e: Exception) {
            Log.e("Push Notification", "Error sending message: ${e.message}")
        }
    }


}


fun getFcmToken(onTokenReceived: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            onTokenReceived.invoke(token ?: "")
        } else {
            Log.e("FCM Token", "Error getting FCM token: ${task.exception?.message}")
        }
    }
}