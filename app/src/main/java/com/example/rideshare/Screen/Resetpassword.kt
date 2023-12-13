package com.example.rideshare.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.rideshare.Components.ComponentsShapes
import com.example.rideshare.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Resetpassword(navController: NavHostController) {
    var userEmail by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var  onBackPressed by remember { mutableStateOf(false) }

    var cxt=LocalContext.current
    val scope = rememberCoroutineScope()
    if (onBackPressed) {
        LaunchedEffect(onBackPressed) {
            navController.popBackStack()
        }
    }

  Surface(modifier = Modifier.fillMaxSize()) {

          Column(
              modifier =
              Modifier
                  .fillMaxSize()
                  .padding(16.dp)
          ) {

              Spacer(modifier = Modifier.height(150.dp))

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

                  value = userEmail,
                  onValueChange = {
                      userEmail = it
                  },
                  leadingIcon = {

                      Icon(
                          imageVector = Icons.Outlined.Email,
                          contentDescription = null,
                          tint = colorResource(
                              id = R.color.skyblue
                          )
                      )

                  }
              )

              Spacer(modifier = Modifier.height(30.dp))

              Button(
                  onClick = {

                      scope.launch {
                          try {
                              isLoading = true
                              showError = false
                              FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail).await()
                              Log.d(
                                  "forgotPassword",
                                  "Password reset email sent. Check your email."
                              )
                              Toast.makeText(cxt, "Password reset email sent. Check your email.", Toast.LENGTH_LONG).show()
                              // showMessage("Password reset email sent. Check your email.")
                               onBackPressed=true
                          } catch (e: Exception) {
                              showError = true
                              errorMessage = e.localizedMessage ?: "An error occurred."
                          } finally {
                              isLoading = false
                          }
                      }

                  },
                  enabled = !isLoading,
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(50.dp)
              ) {
                  if (isLoading) {
                      CircularProgressIndicator(color = Color.White)
                  } else {
                      Text("Reset Password")
                  }

              }

          }

      }
  }
