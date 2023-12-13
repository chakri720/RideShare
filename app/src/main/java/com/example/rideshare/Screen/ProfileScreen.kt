package com.example.rideshare.Screen



import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*



import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rideshare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun ProfileScreen(navController: NavHostController) {
    var isEditing by remember { mutableStateOf(false) }
    val tag="ProfileScreen"
   // Log.d(tag, "Compositions:$name")
    // Dummy user data
    var userName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("John Doe") }
    var userEmail by remember { mutableStateOf("john.doe@example.com") }
    var userPhone by remember { mutableStateOf("123-456-7890") }
    val auth= FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val user=auth.currentUser
    val userId = user!!.uid
    val myRef = database.getReference("UserInfo").child(userId).child("RegisterUser")
    LaunchedEffect(Unit) {


        myRef.addValueEventListener(object : ValueEventListener {
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

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    var cxt=LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver,it)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver,it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }

        bitmap.value?.let {  btm ->
            Image(bitmap = btm.asImageBitmap(),
                contentDescription =null,
                modifier = Modifier.size(400.dp))
        }
    }

    // Body of the screen
    Scaffold(
        topBar =  {

            TopAppBar (
                title={
                    Text(text="Profile", color = Color.White)
                },
                colors= TopAppBarDefaults.smallTopAppBarColors(
                    containerColor= colorResource(id = R.color.colorBlue)
                ),

                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",tint=Color.White)
                    }
                },
            )
        },

        content = {
//            Image(
//                painter = painterResource(id = R.drawable.car3),
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = "Background Image",
//                contentScale = ContentScale.Crop
//            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))

//                bitmap.value?.let {  btm ->
//                    Image(bitmap = btm.asImageBitmap(),
//                        contentDescription =null,
//                        modifier = Modifier.size(120.dp)
//                            .clip(CircleShape)
//                            .clickable { launcher.launch("image/*") }
//
//                    )
//                }

                Image(
                    painter = painterResource(id = R.drawable.carpng2),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                       // .clickable { launcher.launch("image/*") }
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = { isEditing=true },
                        modifier= Modifier
                            .align(alignment = Alignment.End)
                            .padding(start = 150.dp)

                          //  .align(Alignment.TopEnd)
                ){
                    Icon(imageVector = Icons.Filled.Edit, contentDescription =null,
                        modifier = Modifier.size(60.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
        Text(text=userName,  modifier= Modifier
            .align(alignment = Alignment.CenterHorizontally)

            .heightIn(min = 40.dp),
         style= TextStyle(
        fontSize=35.sp,
        fontWeight= FontWeight.Bold,
        fontStyle = FontStyle.Normal))


            Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.white))
                        .padding(8.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                   // Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = null, tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                    //Spacer(modifier = Modifier.width(16.dp))
                    Column {
                      //  Text(text = "Name", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                        //  TextField(text = value, fontWeight = FontWeight.Bold)
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray),
                            textStyle = TextStyle(color = Color.Black,

                                fontSize = 16.sp,
                            ),
                            value =userName, onValueChange ={userName =it},
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = null,tint = colorResource(
                                    id = R.color.skyblue ))

                            },

                            shape= RoundedCornerShape(50.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedLeadingIconColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                placeholderColor = Color.Black,
                                focusedLabelColor = colorResource(id = R.color.colorBlue),
                                cursorColor = colorResource(id = R.color.colorBlue),
                                containerColor  = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent

                            ),
                            singleLine = true,
                                placeholder={Text(text="FirstName")},
                            enabled = isEditing
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray),
                            textStyle = TextStyle(color = Color.Black,

                                fontSize = 16.sp,
                            ),
                            value = lastName, onValueChange ={lastName =it},
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Person, contentDescription = null ,tint = colorResource(
                                    id = R.color.skyblue ))


                            },
                            shape= RoundedCornerShape(50.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedLeadingIconColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                placeholderColor = Color.Black,
                                focusedLabelColor = colorResource(id = R.color.colorBlue),
                                cursorColor = colorResource(id = R.color.colorBlue),
                                containerColor  = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent

                            ),
                            singleLine = true,
                            enabled = isEditing,
                            placeholder={Text(text="LastName")},
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray),
                            textStyle = TextStyle(color = Color.Black,

                                fontSize = 16.sp,
                            ),
                            value = userEmail, onValueChange ={userEmail =it},
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Email, contentDescription = null ,tint = colorResource(
                                    id = R.color.skyblue ))


                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedLeadingIconColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                placeholderColor = Color.Black,
                                focusedLabelColor = colorResource(id = R.color.colorBlue),
                                cursorColor = colorResource(id = R.color.colorBlue),
                                containerColor  = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent

                            ),
                            singleLine = true,
                            enabled = false,
                            placeholder={ Text(text="Email")},
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        TextField(modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.LightGray),
                            textStyle = TextStyle(color = Color.Black,

                                fontSize = 16.sp,
                            ),
                            value = userPhone, onValueChange ={userPhone =it},
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = null ,tint = colorResource(
                                    id = R.color.skyblue ))


                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedLeadingIconColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                placeholderColor = Color.Black,
                                focusedLabelColor = colorResource(id = R.color.colorBlue),
                                cursorColor = colorResource(id = R.color.colorBlue),
                                containerColor  = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent

                            ),
                            singleLine = true,
                            placeholder={Text(text="PhoneNumber")},
                             enabled = isEditing
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        if(isEditing) {
                            Button(
                                onClick = {
                                    auth.currentUser?.updateEmail(userEmail)

                                    val updatedProfile = userProfile(FirstName = userName, LastName = lastName,Email=userEmail, PhoneNumber = userPhone)
                                    updateProfileInfo(userId, updatedProfile)
                                    makeText(cxt, "Profile Updated Success", LENGTH_LONG).show()


                                    isEditing=false },
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorBlue)),
                                modifier = Modifier
                                    .fillMaxWidth() // Takes the full width
                                    .align(Alignment.CenterHorizontally)

                            ) {
                                Text(
                                    modifier = Modifier.padding(3.dp),
                                    text = "Save",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )


                            }
                        }

                    }
                }
            }
        }
    )
}

private fun DatabaseReference.updateChildren(email: String, firstName: String, lastname: String, phoneNumer: String) {

}

fun updateProfileInfo(userId: String, updatedProfile: userProfile) {
    val databaseReference: DatabaseReference by lazy { Firebase.database.reference.child("UserInfo").child(userId).child("RegisterUser") }

    // Convert UserProfile data to a HashMap
    val profileUpdates = hashMapOf<String, Any>(
        "firstName" to updatedProfile.FirstName,
        "lastName" to updatedProfile.LastName,
        "email" to updatedProfile.Email,
        "phoneNumber" to updatedProfile.PhoneNumber
        // Add more fields as needed
    )

    // Update the entire user node with the new profile information
    databaseReference.updateChildren(profileUpdates)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(icon: ImageVector, label: String, value: String) {
    val password= remember {
        mutableStateOf("ggg")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, color = Color.Black)
          //  TextField(text = value, fontWeight = FontWeight.Bold)
            TextField(modifier = Modifier
                .fillMaxWidth(),value = password.value, onValueChange ={password.value =it})
        }
    }
}

@Composable
//@Preview(showBackground = true)
fun ProfileScreenPreview() {
   //ProfileScreen()
}