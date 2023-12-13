package com.example.rideshare.Screen


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
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Badge
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
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DrawerHeader(userName:String,userEmail:String) {

        Row(
            modifier = Modifier
                .fillMaxSize()

                .padding(8.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.carpng2),
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .padding(2.dp)
                    .background(Color.LightGray),

            )
            Spacer(modifier = Modifier.width(8.dp))
            Column( modifier=Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
            .weight(1f)) {
                Text(text = userName,

                    fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)), fontWeight = FontWeight.Bold,color=Color.White)
                Text(
                    text = userEmail,
                    fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),
                    color = Color.White,

                )
            }
        }
    }



data class Item(val id: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(navController: NavHostController) {
    val scaffoldState = rememberDrawerState(initialValue = DrawerValue.Closed)
   // val scaffoldState = rememberScaffoldState()
    val auth= FirebaseAuth.getInstance()
    val scope= rememberCoroutineScope()
   // var currentDrawerScreen by remember { mutableStateOf(DrawerScreens.Home) }
    var itemList by remember { mutableStateOf<List<RideDetails>>(emptyList()) }
    val textState=remember{
        mutableStateOf(TextFieldValue(""))
    }
    val data= remember {
        DataProvider.rideList
    }
    val navOptions= NavOptions.Builder().setPopUpTo("HomeScreen",inclusive = true).build()
//Log.d("Login","Login user $user")
    var Logout by remember { mutableStateOf(false) }
    if (Logout) {
        LaunchedEffect(Logout) {
          //  navController.popBackStack()

            FirebaseAuth.getInstance().signOut()
            if(auth.currentUser==null){
            navController.navigate("LoginScreen", navOptions)
                }
        }
    }
    //val auth= FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val user=auth?.currentUser
    val userId = user?.uid
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("john.doe@example.com") }
    val myRef = userId?.let { database.getReference("UserInfo").child(it).child("RegisterUser") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {


        if (myRef != null) {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue(userProfile::class.java)
                    Log.d("profile", "Value is: $value")
                    if (value != null) {
                        userName = value.FirstName
                        userEmail = value.Email

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //  Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
       getStudentData { items ->
            itemList = items
        }
    }

    ModalNavigationDrawer(

        drawerState = scaffoldState,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier
                    .height(150.dp)
                    .background(colorResource(id = R.color.colorBlue))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()

                                .padding(8.dp),

                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.carpng2),
                                contentDescription = "User Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape)
                                    .padding(2.dp)
                                    .background(Color.LightGray),

                                )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column( modifier=Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)) {
                                Text(text = userName,

                                    fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)), fontWeight = FontWeight.Bold,color=Color.White)
                                Text(
                                    text = userEmail,
                                    fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),
                                    color = Color.White,

                                    )
                            }
                        }


                      //  DrawerHeader(userName=userName,userEmail=userEmail)
                    }
                }
               // Text("Drawer title", modifier = Modifier.padding(16.dp))
//                Divider()

                //Spacer(modifier = Modifier.height(16.dp))

                    NavigationDrawerItem(
                        icon = {
                            Icon(imageVector = Icons.Filled.Home, contentDescription =null )
                        },
                        label = { Text(text = "Home") },
                        selected = false,
                        onClick = {  scope.launch {
                            scaffoldState.close()
                        }}
                    )

                    NavigationDrawerItem(
                        icon = {
                            Icon(imageVector = Icons.Filled.PersonOutline, contentDescription =null )
                        },

                        label = { Text(text = "Profile") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                scaffoldState.close()
                            }
                            navController.navigate("ProfileScreen") }

                    )
                NavigationDrawerItem(
                    icon = {
                        Icon(imageVector = Icons.Filled.Badge, contentDescription =null )
                    },

                    label = { Text(text = "Booked") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            scaffoldState.close()
                        }
                        navController.navigate("BookedUser") }

                )
                    NavigationDrawerItem(
                        icon = {
                            Icon(imageVector = Icons.Filled.Backpack, contentDescription =null )
                        },

                        label = { Text(text = "Ride History") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                scaffoldState.close()
                            }
                            navController.navigate("Ridehistroy")
                        }

                    )
                    NavigationDrawerItem(

                        icon = {
                            Icon(imageVector = Icons.Filled.Logout, contentDescription =null )
                        },
                        label = { Text(text = "Logout") },

                        selected = false,
                        onClick = {
                            scope.launch {
                                scaffoldState.close()
                            }
                            isLoading = true

                            // Perform sign-out in a coroutine
                            coroutineScope.launch {
                                try {
                                    FirebaseAuth.getInstance().signOut()
                                    navController.navigate("LoginScreen", navOptions)
                                    // Navigate to the login screen or any other appropriate screen
                                    // For example, using the context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle sign-out error
                                } finally {
                                    // Stop loading state
                                    isLoading = false
                                }
                            }



                            //   Logout=true
                              //  FirebaseAuth.getInstance()?.signOut()
                                                           // Log.d("error","Logout failed")

                        }


                    )


            }
        },
    ) {
    Scaffold(
       // scaffoldState = scaffoldState,
       topBar= {
//            AppToolbar(toolbarTitle = "HomeScreen", )

           TopAppBar(
               title = {
                   Text(text = "Home Screen", color = Color.White)
               },
               colors= TopAppBarDefaults.smallTopAppBarColors(
                   containerColor= colorResource(id = R.color.colorBlue)
               ),
               //backgroundColor = colorResource(id = R.color.colorBlue),

               navigationIcon = {
                   IconButton(onClick={
               scope.launch {
              scaffoldState.open()
             }
                   }) {
                       Icon(
                           imageVector = Icons.Filled.Menu,
                           contentDescription = "Menu",
                           tint = Color.White
                       )
                   }

               }
           )
       },


    ) {paddingValues ->
        LaunchedEffect(drawerState.isClosed) {
            if (drawerState.isClosed) {
                coroutineScope.launch {
                    drawerState.close()
                }
            }
        }
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
                           // .fillMaxWidth()
                           // .width(100.dp)
                            .padding(bottom = 16.dp)
                    )
                    Box(modifier = Modifier

                        .padding(10.dp)
                        .clickable { }


                    )
                    IconButton(onClick = { navController.navigate("AddRide")}) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                          modifier=Modifier.size(70.dp),
                            contentDescription = null,
                            tint = colorResource(
                                id = R.color.skyblue
                            )
                        )
                    }
                   // Text(modifier = Modifier.padding(3.dp),text="add")
                }

            LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
             //   items(items=data, itemContent = {
                items(itemList.filter { it.Destination.contains(searchText, ignoreCase = true) }) { item ->
              //  items(itemList) { item ->
                        ListItem(item=item,navController)

                }
                }}
          // RecylerView(navController.navigate("Login"))
        }

    }}
}
@Composable
fun Profile(){
    Surface(color = Color.Gray,
        modifier=Modifier.padding(

            ),
        shape= RoundedCornerShape(corner= CornerSize(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.carpng2),
            contentDescription = "null",
            modifier = Modifier
                .padding(8.dp)
                .size(54.dp)
                .clip(
                    RoundedCornerShape(corner = CornerSize(16.dp))

                )
        )
    }
}

@Composable
fun ListItem(item: RideDetails,navController: NavHostController){
    val expanded= remember{ mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if(expanded.value)24.dp else 0.dp,
        animationSpec= spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,

            stiffness = Spring.StiffnessLow

        )

    )
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
              // sharedViewModel.selectedItem =item
                navController.navigate("DetailView/${item.Source}/${item.Destination}/${item.FirstName}/${item.LastName}/${item.Email}/${item.vehicleNumber}/${item.PhoneNumber}/${item.Seats}/${item.Date}/${item.Time}/${item.Price}") }){
                Profile()
                Column(modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)) {

                    Text(text=item!!.Source,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)), fontSize = 15.sp,style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold))
                    Text(text=item!!.Destination, fontFamily = FontFamily(Font(R.font.spacegrotesk_regular))
                      ,fontSize = 13.sp
                    )

                }
                OutlinedButton(onClick={expanded.value=!expanded.value},

                    ){
                    Text(if(expanded.value) "Hide" else "Show",color= colorResource(id = R.color.colorBlue),fontSize=12.sp)
                }
            }

            if(expanded.value){
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Column(modifier=Modifier.padding(
                    bottom=extraPadding.coerceAtLeast(0.dp)
                )){
                    Row {

                        Text(text = "Starting point",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)), fontSize = 14.sp, modifier = Modifier.width(150.dp))

                        Text(text =item.Source,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                  Row {
                      Text(text = "Destination",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                      Text(text = item.Destination,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                  }
                    Row {
                        Text(text = "Mobilenumber",fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)),fontSize = 14.sp,modifier = Modifier.width(150.dp))

                        Text(text = item.PhoneNumber,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                    Row {
                        Text(text = "Vehicle number",modifier = Modifier.width(150.dp),fontSize = 14.sp,fontFamily = FontFamily(Font(R.font.spacegrotesk_semibold)))

                        Text(text = item.vehicleNumber,fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),fontSize = 14.sp)
                    }
                }
            }
        }
    }
}



@Composable
fun RecylerView(navigateToLogin:(RideData)->Unit){
    val textState=remember{
        mutableStateOf(TextFieldValue(""))
    }
    val data= remember {
        DataProvider.rideList
    }

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
        items(items=data, itemContent = {
//ListItem(name = it)
        }
        )


    }
}

fun getStudentData(callback: (List<RideDetails>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val studentsRef = database.getReference("Ridelist")

    studentsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val RideList = mutableListOf<RideDetails>()

            for (studentSnapshot in snapshot.children) {
                val student = studentSnapshot.getValue(RideDetails::class.java)
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
//suspend fun fetchItemsFromFirebase(onSuccess: (List<RideDetail>) -> Unit) {
//    val items = mutableListOf<RideDetail>()
//
//    try {
//        val database = FirebaseDatabase.getInstance()
//        val reference: DatabaseReference = database.getReference("Ridelist")
//
//
//        withContext(Dispatchers.IO) {
//            reference.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (childSnapshot in snapshot.children) {
//                      //  val id = childSnapshot.key.orEmpty()
//                        val value = snapshot.getValue(RideDetail::class.java)
////                        val Source = childSnapshot.child("source").getValue(String::class.java).orEmpty()
////                        val Destination = childSnapshot.child("source").getValue(String::class.java).orEmpty()
////                        val Seats = childSnapshot.child("source").getValue(String::class.java).orEmpty()
////                        val Date = childSnapshot.child("source").getValue(String::class.java).orEmpty()
////                        val Time = childSnapshot.child("source").getValue(String::class.java).orEmpty()
//                       items.add(value)
//                       // onSuccess(items=value)
//                    }
//                    onSuccess(items)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error
//                }
//            })
//        }
//    } catch (e: Exception) {
//        // Handle error
//    }
//}

@Composable
fun SignOutButton(onSignOut: () -> Unit) {
    Button(onClick = {
        // Sign out the user
        FirebaseAuth.getInstance().signOut()

        // Invoke the provided callback
        onSignOut.invoke()
    }) {
        Text("Sign Out")
    }
}
@Preview
@Composable
fun HomeScreenPreview(){
  //  HomeScreen()
}