package com.example.rideshare.Screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.rideshare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable

fun AddRide(navController: NavHostController) {
    var addedRide by remember { mutableStateOf(false) }
    var userName by remember {
        mutableStateOf("")
    }
    var lastName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var vehicleNumber by remember {
        mutableStateOf("")
    }
    var PhoneNumber by remember {
        mutableStateOf("")
    }
    var destinationexpand by remember {
        mutableStateOf(false) }
    val database= Firebase.database
    val myRef=database.getReference("Ridelist")

    val newDataRef = myRef.push()
    val interactionSource = remember {
        MutableInteractionSource()
    }
   var price by remember {
       mutableStateOf("")
   }
    if (addedRide) {
        LaunchedEffect(addedRide) {
            navController.popBackStack()
        }
    }
    val auth= FirebaseAuth.getInstance()
    val user=auth.currentUser
    val userId = user!!.uid
    val userInf = database.getReference("UserInfo").child(userId).child("RegisterUser")
    LaunchedEffect(Unit) {


        userInf.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(userProfile::class.java)
                Log.d("profile", "Value is: $value")
                if (value != null) {
                    userName = value.FirstName
                    lastName = value.LastName
                    userPhone = value.PhoneNumber
                    userEmail = value.Email
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
    var selectedText by remember { mutableStateOf("") }

    val focusRequester = FocusRequester()

    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    val mContext = LocalContext.current
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val context = LocalContext.current
    val date = remember { mutableStateOf("") }
    val datapickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth-$month-$year"
        }, year, month, day
    )

    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    val time = remember {
        mutableStateOf("")
    }
    var seat by remember { mutableStateOf(0) }
    val timePickerDialog = TimePickerDialog(
        mContext,
        { _, hour: Int, minute: Int ->
            if (hour >= 0 && hour < 12) {
                time.value = "$hour: $minute:AM"
            } else {
                if (hour == 12) {
                    time.value = "$hour:$minute PM"
                } else {
                    var hour = hour - 12;
                    time.value = " $hour:$minute PM"
                }
            }
            //  time.value="$hour:$minute "
        }, hour, minute, false
    )

    var cxt=LocalContext.current
    val mCities = listOf("Delhi", "Mumbai", "Chennai", "Kolkata", "Hyderabad", "Bengaluru", "Pune")
    //val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                    destinationexpand = false

                }
            )

    ) {
        Image(
            painter = painterResource(id = R.drawable.car1),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop
        )
        Scaffold(
            // scaffoldState = scaffoldState,
            topBar = {


                TopAppBar(
                    title = {
                        Text(text = "Add Ride", color = Color.Black)
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()

                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }

                    }
                )
            },
            content = {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                expanded = false
                                destinationexpand = false

                            }
                        )


                ) {

                    Spacer(modifier = Modifier.height(60.dp))







                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .background(colorResource(id = R.color.white))
                            .padding(8.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = null, tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                        //  Spacer(modifier = Modifier.width(16.dp))


                        Column {
                            //  Text(text = "Name", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                            //  TextField(text = value, fontWeight = FontWeight.Bold)

                            TextField(
                                modifier = Modifier
                                    .border(1.dp, Color.LightGray)
                                    .fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                value = source,
                                onValueChange = {
                                    source = it
                                    expanded = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.LocationSearching,
                                        contentDescription = null,
                                        tint = colorResource(id = R.color.skyblue)
                                    )

                                },
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    // below line is used to add font
                                    // size for our text field
                                    fontSize = 16.sp,
                                    // below line is use to change font family.
                                    // fontFamily = FontFamily(Font(R.font.spacegrotesk_regular))
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedLeadingIconColor = Color.Black,
                                    unfocusedLabelColor = Color.Black,
                                    placeholderColor = Color.Black,
                                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                                    cursorColor = colorResource(id = R.color.colorBlue),
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent

                                ),
                                placeholder = { Text(text = "source") },
                                //label = { Text(text = "Source") },
                                //  enabled = false
                            )
                            AnimatedVisibility(visible = expanded) {
                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(10.dp)
                                ) {
                                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {

                                        if (source.isNotEmpty()) {
                                            items(
                                                mCities.filter {
                                                    it.lowercase()
                                                        .contains(source.lowercase()) || it.lowercase()
                                                        .contains("others")
                                                }
                                                    .sorted()
                                            ) {
                                                Categoryitem(title = it) { title ->
                                                    source = title
                                                    expanded = false
                                                }
                                            }
                                        } else {
                                            items(mCities.sorted())
                                            {
                                                Categoryitem(title = it) { title ->
                                                    source = title
                                                    expanded = false
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                            Spacer(modifier = Modifier.height(15.dp))
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.LightGray),
                                textStyle = TextStyle(
                                    color = Color.Black,

                                    fontSize = 16.sp,
                                ),
                                singleLine = true,
                                value = destination, onValueChange = {
                                    destination = it
                                    destinationexpand = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.AddLocation,
                                        contentDescription = null,
                                        tint = colorResource(
                                            id = R.color.skyblue
                                        )
                                    )

                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedLeadingIconColor = Color.Black,
                                    unfocusedLabelColor = Color.Black,
                                    placeholderColor = Color.Black,
                                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                                    cursorColor = colorResource(id = R.color.colorBlue),
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent

                                ),
                                placeholder = { Text(text = "Destination") }
                                //  label = { Text(text = "Destination") },
                                //  enabled = false
                            )

                            AnimatedVisibility(visible = destinationexpand) {
                                Card(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(10.dp)
                                ) {
                                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {

                                        if (destination.isNotEmpty()) {
                                            items(
                                                mCities.filter {
                                                    it.lowercase()
                                                        .contains(destination.lowercase()) || it.lowercase()
                                                        .contains("others")
                                                }
                                                    .sorted()
                                            ) {
                                                Categoryitem(title = it) { title ->
                                                    destination = title
                                                    destinationexpand = false
                                                }
                                            }
                                        } else {
                                            items(mCities.sorted())
                                            {
                                                Categoryitem(title = it) { title ->
                                                    destination = title
                                                    destinationexpand = false
                                                }
                                            }
                                        }
                                    }

                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                            .background(colorResource(id = R.color.white))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {




//                            TextField(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .border(1.dp, Color.LightGray),
//                                textStyle = TextStyle(
//                                    color = Color.Black,
//
//                                    fontSize = 16.sp,
//                                ),
//                                value = seat, onValueChange = {seat=it
//                                        //value->
////                                    if (value.length <= 2) {
////                                        seat= value.filter { it.isDigit() }
////                                    }
//                                    },
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.Outlined.AirlineSeatReclineNormal,
//                                        contentDescription = null,
//                                        tint = colorResource(
//                                            id = R.color.skyblue
//                                        )
//                                    )
//
//                                },
//                                singleLine = true,
//                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                                shape = RoundedCornerShape(50.dp),
//                                colors = TextFieldDefaults.textFieldColors(
//                                    focusedLeadingIconColor = Color.Black,
//                                    unfocusedLabelColor = Color.Black,
//                                    placeholderColor = Color.Black,
//                                    focusedLabelColor = colorResource(id = R.color.colorBlue),
//                                    cursorColor = colorResource(id = R.color.colorBlue),
//                                    containerColor = Color.White,
//                                    focusedIndicatorColor = Color.Transparent,
//                                    unfocusedIndicatorColor = Color.Transparent,
//                                    disabledIndicatorColor = Color.Transparent
//
//                                ),
//                                placeholder = { Text(text = "Available seat") },
//                                //  label = { Text(text = ) },
//                                //  enabled = false
//                            )


                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(50.dp),
                               singleLine=true,
                                textStyle = TextStyle(
                                    color = Color.Black,

                                    fontSize = 16.sp,
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Gray,
                                    disabledTextColor = Color.Transparent,
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),

                                value = vehicleNumber,
                                onValueChange = {vehicleNumber=it},
                                placeholder = { Text("Vehicle Number") },
                                //label = { Text("Selected Date") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.BikeScooter,
                                        contentDescription = null,
                                        tint = colorResource(
                                            id = R.color.skyblue
                                        )
                                    )
                                },

                                )


//                            Spacer(modifier = Modifier.height(15.dp))
//
//                            TextField(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .border(1.dp, Color.LightGray),
//                                shape = RoundedCornerShape(50.dp),
//
//                                textStyle = TextStyle(
//                                    color = Color.Black,
//
//                                    fontSize = 16.sp,
//                                ),
//                                colors = TextFieldDefaults.textFieldColors(
//                                    textColor = Color.Gray,
//                                    disabledTextColor = Color.Transparent,
//                                    containerColor = Color.White,
//                                    focusedIndicatorColor = Color.Transparent,
//                                    unfocusedIndicatorColor = Color.Transparent,
//                                    disabledIndicatorColor = Color.Transparent
//                                ),
//                              singleLine=true,
//                                value = PhoneNumber,
//                                onValueChange = {PhoneNumber=it},
//                                placeholder = { Text("MobileNumber") },
//                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                                //label = { Text("Selected Date") },
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.Default.Phone,
//                                        contentDescription = null,
//                                        tint = colorResource(
//                                            id = R.color.skyblue
//                                        )
//                                    )
//                                },
//                                )
                            Spacer(modifier = Modifier.height(15.dp))



                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(50.dp),

                                textStyle = TextStyle(
                                    color = Color.Black,

                                    fontSize = 16.sp,
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Gray,
                                    disabledTextColor = Color.Transparent,
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),

                                value = date.value,
                                onValueChange = {},
                                placeholder = { Text("Select Date") },
                                //label = { Text("Selected Date") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = colorResource(
                                            id = R.color.skyblue
                                        )
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { datapickerDialog.show() }) {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarMonth,
                                            contentDescription = null
                                        )
                                    }
                                },
                                readOnly = true,
                                singleLine = true,

                                )
                            Spacer(modifier = Modifier.height(15.dp))

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(50.dp),

                                textStyle = TextStyle(
                                    color = Color.Black,

                                    fontSize = 16.sp,
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Gray,
                                    disabledTextColor = Color.Transparent,
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                value = time.value,
                                onValueChange = {},
                                placeholder = { Text("Select Time") },
                                //label = { Text("Selected Date") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LockClock,
                                        contentDescription = null,
                                        tint = colorResource(
                                            id = R.color.skyblue
                                        )
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { timePickerDialog.show() }) {
                                        Icon(
                                            imageVector = Icons.Filled.CalendarMonth,
                                            contentDescription = null
                                        )
                                    }
                                },
                                readOnly = true,
                                )

                            Spacer(modifier = Modifier.height(15.dp))
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(50.dp),

                                textStyle = TextStyle(
                                    color = Color.Black,

                                    fontSize = 16.sp,
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Gray,
                                    disabledTextColor = Color.Transparent,
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                value = price,
                                onValueChange = {price=it},
                                placeholder = { Text("Price") },
                                //label = { Text("Selected Date") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = null,
                                        tint = colorResource(
                                            id = R.color.skyblue
                                        )
                                    )
                                },



                                )
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

                        }
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),

                        // .background(colorResource(id = R.color.white))
                        // .padding(8.dp)

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Button(
                                shape = RectangleShape,
                                onClick = {

                                   // val user=auth.currentUser
                                  //  val userId = user!!.uid
                                    val AddRide=RideDetail(Source = source,Destination = destination, Seats = seat,Date=date.value,Time=time.value,Price=price, vehicleNumber = vehicleNumber, PhoneNumber = userPhone, FirstName = userName, LastName = lastName,Email=userEmail);
                                   if(source.isNotEmpty()){
                                       if(destination.isNotEmpty()){
                                           if(date.value.isNotEmpty()){
                                               if(time.value.isNotEmpty()){
                                                   if(price.isNotEmpty()){

                                     if(seat!=0){

                                    myRef.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            newDataRef.setValue(AddRide)

                                            Log.d("Success","$snapshot")
                                            addedRide=true
                                           // navController.popBackStack()

                                        }
                                        override fun onCancelled(error: DatabaseError) {

                                            Log.d("Error","$error")

                                        }
                                    })
                                                       } } } } } }
                                    else{
                                       Toast.makeText(cxt, "Enter all field.", Toast.LENGTH_LONG).show()
                                   }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    // .padding(15.dp)
                                    .background(colorResource(id = R.color.colorBlue))
                                    .heightIn(48.dp),
                            ) {
                                Text(text = "Submit", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

        )
    }
}
@Composable
fun CountButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val rippleColor = MaterialTheme.colorScheme.primary

    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .size(25.dp)
            .background(rippleColor, shape = MaterialTheme.shapes.small)
            //(rememberRipple(bounded = false, color = rippleColor))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
@Composable

fun Categoryitem(
    title: String,
    onSelect:(String)->Unit
){
    Row(modifier= Modifier
        .fillMaxWidth()
        .clickable {
            onSelect(title)
        }
        .padding(10.dp)
    ) {
Text(text=title)

    }
}






