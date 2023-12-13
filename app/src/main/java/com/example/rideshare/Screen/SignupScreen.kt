package com.example.rideshare.Screen


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.rideshare.Components.*
import com.example.rideshare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController) {

    val auth= FirebaseAuth.getInstance()
    var firstName by remember { mutableStateOf("")}
    var lastName by remember {mutableStateOf("")}
    var userEmail by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var cxt=LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val navOptions= NavOptions.Builder().setPopUpTo("SignupScreen",inclusive = true).build()

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
    }
    val focusManager = LocalFocusManager.current
    val firebaseDatabase = FirebaseDatabase.getInstance();
    val databaseReference = firebaseDatabase.getReference("UserInfo");
    val database= Firebase.database
    val myRef=database.getReference("UserInfo")
    val Fuser = FirebaseAuth.getInstance().getCurrentUser();
    // on below line we are calling method to display UI
  //  firebaseUI(LocalContext.current, databaseReference)

    Surface(color = Color.White,
    modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(28.dp)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
        .verticalScroll(rememberScrollState())
        ) {
        Column(modifier = Modifier.fillMaxSize()) {
           // NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = "Create account")
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentsShapes.small),
                shape = RoundedCornerShape(15.dp),
                label = { Text(text = "FirstName") },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.colorBlue),
                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                    cursorColor = colorResource(id = R.color.colorBlue),
                   containerColor = Color.White
                ),
                keyboardActions= KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)},
                ),

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,imeAction = ImeAction.Next),
                value = firstName,
                onValueChange = {
                    firstName=it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null,tint = colorResource(
                        id = R.color.skyblue))

                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentsShapes.small),
                shape = RoundedCornerShape(15.dp),
                label = { Text(text = "LastName") },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.colorBlue),
                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                    cursorColor = colorResource(id = R.color.colorBlue),
                   containerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction =ImeAction.Next ),
                singleLine = true,
                keyboardActions= KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)},
                ),
                value = lastName,
                onValueChange = {
                    lastName=it
                },

                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null,tint = colorResource(
                        id = R.color.skyblue))

                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentsShapes.small),
                shape = RoundedCornerShape(15.dp),
                label = { Text(text = "Email") },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.colorBlue),
                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                    cursorColor = colorResource(id = R.color.colorBlue),
                   containerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,imeAction = ImeAction.Next),
                keyboardActions= KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)},
                ),
                value = userEmail,
                onValueChange = {
                    userEmail=it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = null,tint = colorResource(
                        id = R.color.skyblue))

                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentsShapes.small),
                shape = RoundedCornerShape(15.dp),
                label = { Text(text = "PhoneNumber") },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.colorBlue),
                    focusedLabelColor = colorResource(id = R.color.colorBlue),
                    cursorColor = colorResource(id = R.color.colorBlue),
                   containerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone,imeAction = ImeAction.Next),
                keyboardActions= KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)},
                ),
                value = phoneNumber,
                onValueChange = {
                    phoneNumber=it
                },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Phone, contentDescription = null,tint = colorResource(
                        id = R.color.skyblue))

                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ComponentsShapes.small),
                shape = RoundedCornerShape(15.dp),
                label = { Text(text = "Password") },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id =R.color.colorBlue ),
                    focusedLabelColor = colorResource(id =R.color.colorBlue ),
                    cursorColor = colorResource(id =R.color.colorBlue ),
                   containerColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
              imeAction = ImeAction.Done),
                keyboardActions= KeyboardActions(
                    onDone = { focusManager.clearFocus()},
                ),
                value = password,
                        onValueChange ={
                    password=it
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = colorResource(
                        id = R.color.skyblue
                    ))

                },
                trailingIcon = {
                    val iconImage = if (passwordVisible) {
                        Icons.Outlined.Visibility
                    } else {
                        Icons.Outlined.VisibilityOff
                    }
                    var description = if (passwordVisible) {
                        "Hide password"
                    } else {
                        "Show password"
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = iconImage, contentDescription = null,tint = colorResource(
                            id = R.color.skyblue
                        ))
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else
                    PasswordVisualTransformation()
            )

           // PasswordTextFieldComponent(labelValue = "Password",icon=Icons.Outlined.Lock)
          Spacer(modifier = Modifier.height(80.dp))
      // ButtonComponent(value = "Register")
            Button(
                onClick={
                        if(userEmail.isNotEmpty()){
                            if(isValidEmail(userEmail)){
                                if(password.length>=6){
                                   showDialog=true
                                    auth.createUserWithEmailAndPassword(userEmail,password)
                                        .addOnCompleteListener{task->
                                            if(task.isSuccessful){
                                                showDialog=false
                                                Toast.makeText(cxt,"Success",Toast.LENGTH_LONG).show()
//                                                val navOptions= NavOptions.Builder()
//                                                    .setPopUpTo("LoginScreen",true)
//                                                    .build()
                                               val user=auth.currentUser
                                                val userId = user!!.uid
                                                val Registerdata=userObj(FirstName = firstName,LastName = lastName, PhoneNumber = phoneNumber,Email=userEmail,Password=password);
                                                myRef.addValueEventListener(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        // inside the method of on Data change we are setting
                                                        // our object class to our database reference.
                                                        // data base reference will sends data to firebase.
                                                       myRef.child(userId).child("RegisterUser").setValue(Registerdata)
                                                        navController.navigate("HomeScreen",navOptions)
                                                        Log.d("Success","$snapshot")
//                                                        Toast.makeText(
//                                                            cxt,
//                                                            "Data added to Firebase Database",
//                                                            Toast.LENGTH_SHORT
//                                                        ).show()
                                                    }
                                                    override fun onCancelled(error: DatabaseError) {
                                                        // if the data is not added or it is cancelled then
                                                        // we are displaying a failure toast message.
                                                        Log.d("Error","$error")
//                                                        Toast.makeText(
//                                                            cxt,
//                                                            "Fail to add data $error",
//                                                            Toast.LENGTH_SHORT
//                                                        ).show()
                                                    }
                                                })


                                            }else{
                                                showDialog=false
                                                val exception = task.exception
                                                Toast.makeText(cxt, "$exception",
                                                    Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }else{
                                    Toast.makeText(cxt, "Password should be 6 characters or longer.", Toast.LENGTH_LONG).show()
                                }

                            }
                            else{
                                Toast.makeText(cxt, "Enter valid Email!", Toast.LENGTH_LONG).show()
                            }


                        }
                        else{
                            Toast.makeText(cxt, "Enter Email!", Toast.LENGTH_LONG).show()
                        }


                },
                modifier= Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                colors=ButtonDefaults.buttonColors(Color.Transparent)
            ){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                colorResource(id = R.color.colorBlue),
                                Color.Black
                            )
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ),
                    contentAlignment = Alignment.Center
                ){
                    Text(text="Register",
                        fontSize = 18.sp,
                        color=Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            DividerTextComponent()
            ClickableLoginTextComponent(onTextSeleted = {
navController.navigate("LoginScreen")
            })
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DefaultPreviewOfSignUpScreen(){

      // ScreenMain()
  //  SignupScreen()
         //   NormalTextComponent(value = stringResource(id = R.string.hello) )




}
