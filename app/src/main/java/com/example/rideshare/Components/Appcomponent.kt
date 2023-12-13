package com.example.rideshare.Components

import android.bluetooth.BluetoothGatt
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons

import androidx.compose.material3.*


import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person

import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rideshare.R



@Composable
fun NormalTextComponent(value: String){
    Text(
        text=value,
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style= TextStyle(
            fontSize=25.sp,
            fontWeight= FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
    ,color= colorResource(id = R.color.colorBlue),
    textAlign = TextAlign.Center
    )
}
@Composable
fun HeadingTextComponent(value: String){
    Text(
        text=value,
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(),
        style= TextStyle(
            fontSize=30.sp,
            fontWeight= FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        ,color= colorResource(id = R.color.colorBlue),
        textAlign = TextAlign.Center
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
  fun MyTextFieldComponent(labelValue:String,icon:ImageVector){
    val textValue= remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ComponentsShapes.small),
        shape = RoundedCornerShape(15.dp),
        label={Text(text= labelValue)},

        colors =TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor= colorResource(id =R.color.colorBlue ),
            focusedLabelColor=colorResource(id =R.color.colorBlue ),
            unfocusedLabelColor=colorResource(id =R.color.colorBlue ),
            containerColor = Color.White
            ),
        keyboardOptions = KeyboardOptions.Default ,
        value=textValue.value,
        onValueChange={
            textValue.value= it
        },
     leadingIcon = {
       Icon(imageVector = icon, contentDescription = null )

        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(labelValue:String,icon:ImageVector){
    val password= remember {
        mutableStateOf("")
    }
    val passwordVisible= remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ComponentsShapes.small),
        shape = RoundedCornerShape(15.dp),
        label={Text(text= labelValue)},

        colors = TextFieldDefaults.outlinedTextFieldColors(


        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password) ,
        value=password.value,
        onValueChange={
            password.value= it
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null )

        },
    trailingIcon={
        val iconImage=if(passwordVisible.value){
            Icons.Filled.Visibility
        }else{
            Icons.Filled.VisibilityOff
        }
var description=if(passwordVisible.value){
    "Hide password"
}else{
    "Show password"
}
        IconButton(onClick = {passwordVisible.value=!passwordVisible.value}){
            Icon(imageVector=iconImage,contentDescription = null)
        }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else
            PasswordVisualTransformation()
    )
}

@Composable
fun ButtonComponent(value:String){
    Button(
        onClick={"/"},
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
            Text(text=value,
            fontSize = 18.sp,
                color=Color.White,
                fontWeight = FontWeight.Bold
                )
        }
    }
}

@Composable
fun DividerTextComponent(){
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Divider(
            modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
        Text(modifier = Modifier.padding(8.dp), text="or", fontSize = 18.sp, color = Color.Black)
        Divider(
            modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}
@Composable
fun ClickableLoginTextComponent(onTextSeleted:(String)->Unit){
    val initialText="Already have an account?"
    val loginText="Login"
    val annotatedString= buildAnnotatedString {
        append(initialText)
        withStyle(style=SpanStyle(color=Color.Blue)){
            pushStringAnnotation(tag=loginText, annotation = loginText)
            append(loginText)

        }
    }
    ClickableText(
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style= TextStyle(
            fontSize=16.sp,
            fontWeight= FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),

        text = annotatedString, onClick ={offset->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")
            if(span.item==loginText){
                onTextSeleted(span.item)
            }
            }
    })

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(toolbarTitle:String ){
    TopAppBar (
        title={
Text(text=toolbarTitle, color = Color.White)
        },
       //backgroundColor = colorResource(id = R.color.colorBlue) ,

        navigationIcon={
            Icon(imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
             tint = Color.White
                )

    }
)

}
