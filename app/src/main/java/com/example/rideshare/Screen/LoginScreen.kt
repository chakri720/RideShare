package com.example.rideshare.Screen

import ScreenMain
import android.content.Context
import android.graphics.Paint
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.icons.Icons
//import androidx.compose.material3.icons.outlined.Email
//import androidx.compose.material.icons.outlined.Lock
//import androidx.compose.material3.icons.outlined.Password
//import androidx.compose.material.icons.outlined.Visibility
//import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.rideshare.Components.*
import com.example.rideshare.R
import com.example.rideshare.Routes.AddRide.route
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val auth= FirebaseAuth.getInstance()
    var userEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var rememberMe by remember { mutableStateOf(false) }
    DisposableEffect(context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        userEmail = prefs.getString(KEY_EMAIL, "") ?: ""
        password = prefs.getString(KEY_PASSWORD, "") ?: ""
        rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false)
        onDispose { }
    }

    var cxt= LocalContext.current
    val focusManager = LocalFocusManager.current
    val navOptions= NavOptions.Builder().setPopUpTo("LoginScreen",inclusive = true).build()
    Surface (
        modifier= Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(28.dp))
           {
               Column(modifier = Modifier.fillMaxSize()) {


        HeadingTextComponent(value = "RideShare")
                   Spacer(modifier = Modifier.height(60.dp))
                Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.car_image),
                        contentDescription = ""
                    )
                }
                   NormalTextComponent(value ="Login")
                   Spacer(modifier = Modifier.height(30.dp))
                 //  MyTextFieldComponent(labelValue = "Email", icon =Icons.Outlined.Email )
                 //  PasswordTextFieldComponent(labelValue = "Password", icon =Icons.Outlined.Lock )
                   OutlinedTextField(
                       modifier = Modifier
                           .fillMaxWidth()
                           .clip(ComponentsShapes.small),
                       shape = RoundedCornerShape(15.dp),
                       label = { Text(text = "Email") },
                       singleLine = true,
                       colors = TextFieldDefaults.outlinedTextFieldColors(
                           focusedBorderColor = colorResource(id = R.color.colorBlue),
                           focusedLabelColor = colorResource(id = R.color.colorBlue),
                           cursorColor = colorResource(id = R.color.colorBlue),
                         //  backgroundColor = Bgcolor
                       ),
                       keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                       label = { Text(text = "Password") },

                       colors = TextFieldDefaults.outlinedTextFieldColors(
                           focusedBorderColor = colorResource(id =R.color.colorBlue ),
                           focusedLabelColor = colorResource(id =R.color.colorBlue ),
                           cursorColor = colorResource(id =R.color.colorBlue ),
                           //backgroundColor = Bgcolor
                       ),

                       keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                           imeAction = ImeAction.Done),
                       keyboardActions= KeyboardActions(

                           onDone = {focusManager.clearFocus()
                               savePreferences(context, userEmail, password, rememberMe)
                                    },

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
                       singleLine = true,
                       trailingIcon = {
                           val iconImage = if (passwordVisible) {
                             Icons.Filled.Visibility
                           } else {
                               Icons.Filled.VisibilityOff
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

                   Spacer(modifier = Modifier.height(20.dp))

                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.spacedBy(4.dp)
                   ) {
                       Checkbox(
                           checked = rememberMe,
                           onCheckedChange = { rememberMe = true},
                           modifier = Modifier.size(24.dp)
                       )
                       Text("Remember me", fontSize = 14.sp)



                       ClickableText(
                           text = AnnotatedString("Forgot password?"),


                           onClick = {

                               navController.navigate("Resetpassword")

                           },
                           modifier = Modifier.fillMaxWidth(),
                           style = TextStyle(
                               fontSize = 14.sp,
                               textAlign = TextAlign.End,
                               fontFamily = FontFamily.Default
                           ),

                           )
                   }
                   Spacer(modifier = Modifier.height(60.dp))

                   Row {
                       Box( modifier = Modifier
                           .width(100.dp)
                           .heightIn(48.dp)
                           .weight(1f),)
                       {
                           Column {

                               Text(text = "Not a member?")
                               Spacer(modifier = Modifier.height(7.dp))
                               ClickableText(
                                   text = AnnotatedString("Register"),

                                   onClick = { navController.navigate("SignupScreen")},

                                   style = TextStyle(
                                       fontSize = 14.sp,
                                       color=Color.Blue,
                                       fontFamily = FontFamily.Default
                                   )
                               )
                           }
                       }
                   Button(
                       onClick= {



                           auth.signInWithEmailAndPassword(userEmail, password)
                               .addOnCompleteListener { task ->
                                   if (task.isSuccessful) {
                                   //  val user=auth.currentUser
                                       navController.navigate("HomeScreen",navOptions)


                                   } else {

                                       Log.d("Error", "Login Error")
                                       Toast.makeText(cxt,"Enter valid email and password", Toast.LENGTH_LONG).show()

                                   }
                               }

                    },

                       modifier= Modifier
                          // .fillMaxWidth()
                           .heightIn(48.dp),
                           //.width(50.dp)

                       contentPadding = PaddingValues(),
                       colors= ButtonDefaults.buttonColors(Color.Transparent)
                   )


                   {
                       Box(modifier = Modifier
                           // .fillMaxWidth()
                           .width(100.dp)
                           .heightIn(48.dp)
                           .background(
                               brush = Brush.horizontalGradient(
                                   listOf(
                                       colorResource(id = R.color.colorBlue),
                                       colorResource(id = R.color.lightBlue)
                                   )
                               ),
                               shape = RoundedCornerShape(10.dp)
                           ),
                           contentAlignment = Alignment.Center
                       ){
                           Text(text="Login",
                               fontSize = 18.sp,
                               color=Color.White,
                               fontWeight = FontWeight.Bold
                           )
                       }


                   }
                   }

           }}
}
const val PREFS_NAME = "UserPrefs"
const val KEY_EMAIL = "email"
const val KEY_PASSWORD = "password"
const val KEY_REMEMBER_ME = "remember_me"
private fun savePreferences(context: Context, email: String, password: String, rememberMe: Boolean) {

    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
        putString(KEY_EMAIL, email)
        putString(KEY_PASSWORD, password)
        putBoolean(KEY_REMEMBER_ME, rememberMe)
    }
}
@Preview
@Composable
fun LoginScreenPreview(){
    //LoginScreen()
   // ScreenMain()
}