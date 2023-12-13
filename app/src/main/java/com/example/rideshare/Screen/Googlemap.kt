package com.example.rideshare.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.GoogleMap

@Preview
@Composable
fun Googlemap() {
    GoogleMap(
     modifier = Modifier.fillMaxSize().background(Color.White),

     //cameraPositionState = ,
//        properties = mapProperties,
//        uiSettings = uiSettings
    ) {}
}