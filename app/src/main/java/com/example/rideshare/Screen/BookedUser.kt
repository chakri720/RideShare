package com.example.rideshare.Screen


import android.app.Activity
import android.content.ClipData
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.rideshare.Components.AppToolbar
import com.example.rideshare.R
import com.example.rideshare.Routes
import com.google.android.engage.common.datamodel.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun BookedUser(navController: NavHostController) {

    // val scaffoldState = rememberScaffoldState()
    val auth= FirebaseAuth.getInstance()
    val scope= rememberCoroutineScope()
    // var currentDrawerScreen by remember { mutableStateOf(DrawerScreens.Home) }
    var itemList by remember { mutableStateOf<List<BookableItems>>(emptyList()) }
    val textState=remember{
        mutableStateOf(TextFieldValue(""))
    }
    val data= remember {
        DataProvider.rideList
    }
    val navOptions= NavOptions.Builder().setPopUpTo("HomeScreen",inclusive = true).build()
//Log.d("Login","Login user $user")

    LaunchedEffect(Unit) {
        getBookedData { items ->
            itemList = items
        }
    }



        Scaffold(
            // scaffoldState = scaffoldState,
            topBar= {
//            AppToolbar(toolbarTitle = "HomeScreen", )

                TopAppBar(
                    title = {
                        Text(text = "Booked", color = Color.White)
                    },
                    colors= TopAppBarDefaults.smallTopAppBarColors(
                        containerColor= colorResource(id = R.color.colorBlue)
                    ),
                    //backgroundColor = colorResource(id = R.color.colorBlue),
                    navigationIcon = {
                        IconButton(onClick={
navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                    }
                )
            },


            ) {paddingValues ->
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(paddingValues)





            ){
                Image(
                    painter = painterResource(id = R.drawable.car1),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop
                )

                var searchText by remember { mutableStateOf("") }
                //   SearchView(state = textState, placeHolder ="Search here" , modifier =Modifier )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)

                ) {
                    Row {
                        OutlinedTextField(

                            shape = RoundedCornerShape(15.dp),
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = colorResource(
                                        id = R.color.skyblue
                                    )
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colorResource(id = R.color.colorBlue),
                                // focusedLabelColor = colorResource(id =R.color.colorBlue ),
                                cursorColor = colorResource(id = R.color.colorBlue),
                                containerColor = Color.White
                            ),
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            searchText = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },

                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Handle search here
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                // .width(100.dp)
                                .padding(bottom = 16.dp)
                        )


                        // Text(modifier = Modifier.padding(3.dp),text="add")
                    }

                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
                        //   items(items=data, itemContent = {
                        items(itemList.filter { it.Destination.contains(searchText, ignoreCase = true) }) { item ->
                            //  items(itemList) { item ->
                            BookedItem(item=item,navController)

                        }
                    }}

            }

        }}


@Composable
fun BookedItem(item: BookableItems,navController: NavHostController){
    val expanded= remember{ mutableStateOf(false) }
    var showPayment by remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if(expanded.value)24.dp else 0.dp,
        animationSpec= spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,

            stiffness = Spring.StiffnessLow

        )

    )
    var result= remember{ mutableStateOf(0) }



    Surface(color = Color.White,
        shadowElevation=20.dp,
        modifier=Modifier.padding(vertical=4.dp,horizontal=8.dp,

            ),
        border= BorderStroke(1.dp, color = colorResource(id = R.color.colorBlue)),
        shape= RoundedCornerShape(corner= CornerSize(16.dp))
    ){

        Column(modifier= Modifier
            .padding(24.dp)
            .fillMaxWidth()){

            Row (Modifier.clickable {

            })

                 {
                     if(showPayment) {
                         initiatePayment(price=item.Price)
                         showPayment=false
                     }
                     Profile()
                Column(modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)) {

                    Text(text=item!!.Source,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)), style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,fontSize = 15.sp
                    ))
                    Text(text=item!!.Destination, fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),
                       fontSize = 13.sp
                    )

                }
                OutlinedButton(onClick={expanded.value=!expanded.value},){
                    Text(if(expanded.value) "Hide" else "Show",color= colorResource(id = R.color.colorBlue))
                }
            }

            if(expanded.value){
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Column(modifier=Modifier.padding(
                  //  bottom=extraPadding.coerceAtLeast(0.dp)
                )
                    //.align(Alignment.CenterHorizontally)

                ){
                    Row {

                        Text(text = "First Name",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)), fontSize = 14.sp, modifier = Modifier.width(150.dp))

                        Text(text =item.FirstName,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Last Name",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = item.LastName,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {

                        Text(text = "PhoneNumber",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)), fontSize = 14.sp, modifier = Modifier.width(150.dp))

                        Text(text =item.PhoneNumber,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Vehicle Number",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = item.vehicleNumber,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {

                        Text(text = "Starting point",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)), fontSize = 14.sp, modifier = Modifier.width(150.dp))

                        Text(text =item.Source,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Destination",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = item.Destination,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Price",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = "$${item.Price}",fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Seats",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = "${item.seatNumber}",fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Date",modifier = Modifier.width(150.dp),fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)))

                        Text(text = item.Date,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Row {
                        Text(text = "Time",modifier = Modifier.width(150.dp),fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)))

                        Text(text = item.Time,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                }
                Button(onClick = {showPayment=true },
                    modifier= Modifier
                        .padding(8.dp)

                        .align(Alignment.CenterHorizontally)
                    // .background(colorResource(id = R.color.colorBlu)
                ) {
                    Text(text="Pay",color=Color.White)
                }
            }
        }
    }
}








fun getBookedData(callback: (List<BookableItems>) -> Unit) {
    val auth= FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val user=auth.currentUser
    val userId = user!!.uid
    val studentsRef = database.getReference("UserInfo").child(userId).child("BookedInfo")

    studentsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val RideList = mutableListOf<BookableItems>()

            for (studentSnapshot in snapshot.children) {
                val student = studentSnapshot.getValue(BookableItems::class.java)
                student?.let {
                    RideList.add(it)
                }
            }

            callback(RideList)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}
fun rupeesToPaise(rupeesString: String): Long {
    val rupees = rupeesString.toDouble()
    return (rupees * 100).toLong()
}
@Composable
private fun initiatePayment(price:String) {
    val context =  LocalContext.current as? Activity
    //  val keyboardController = LocalSoftwareKeyboardController.current

    // Initialize Razorpay Checkout
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_URMynHVgQvebhO")
    val pri="$price L"
val paise= rupeesToPaise(price)
    //  Set callback listener
    //checkout.setImage(R.drawable.ic_launcher)
    val options = JSONObject()
    options.put("name", "Ride Share")
    options.put("description", "Purchase Description")
    options.put("currency","USD" )
    options.put("amount", "${paise}")
    options.put("prefill.email", "customer@example.com")
    options.put("prefill.contact", "1234567890")

    checkout.open(context , options)


}
@Composable
fun PaymentStatusMessage(paymentStatus: PaymentStatus?) {
    when (paymentStatus) {
        is PaymentSuccess -> {
            Text("Payment successful! Payment ID: ${paymentStatus.razorpayPaymentId}")
        }
        is PaymentFailure -> {
            Text("Payment failed. Error code: ${paymentStatus.errorCode}, Message: ${paymentStatus.errorMessage}")
        }
        else -> {
            // Handle other states or show nothing
        }
    }
}
sealed class PaymentStatus
data class PaymentSuccess(val razorpayPaymentId: String?) : PaymentStatus()
data class PaymentFailure(val errorCode: Int, val errorMessage: String?) : PaymentStatus()
