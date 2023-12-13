package com.example.rideshare.Screen

import androidx.compose.runtime.snapshots.SnapshotStateList

data class RideData (
    val id:Int,
    val title: String,
    val startingpoint:String,
    val destination:String,
    val no:String

    )
data class userObj(

    var FirstName: String,
    var LastName: String,
    var PhoneNumber: String,
    var Email:String,
    var Password:String
)
data class userProfile(

    var FirstName: String="",
    var LastName: String="",
    var PhoneNumber: String="",
    var Email:String="",
    var Password:String=""
)
data class RideDetail(
    var Source:String,
    var Destination:String,
    var FirstName: String="",
    var LastName: String="",
    var Email:String="",
    var Seats:Int,
    var Date:String,
    var Time:String,
    var Price:String,
    var vehicleNumber:String,
    var PhoneNumber: String
)
data class RideDetails(
    var Source:String="",
    var Destination:String="",
    var FirstName: String="",
    var LastName: String="",
    var Email:String="",
    var Seats:Int=0,
    var Date:String="",
    var Time:String="",
    var Price:String="",
    var vehicleNumber:String="",
    var PhoneNumber: String=""
)
data class BookableItem(
    var Source:String,
    var Destination:String,
    var FirstName: String,
    var LastName: String,
    var Email: String,
    var vehicleNumber: String,
    var PhoneNumber:String,
    var Date:String,
    var Time:String,
    var Price:String,
    var seatNumber: Int,
    var isBooked: Boolean,
    var bookedByUserId: String,

)
data class BookableItems(
    var Source:String="",
    var Destination:String="",
    var FirstName: String="",
    var LastName: String="",
    var Email: String="",
    var vehicleNumber: String="",
    var PhoneNumber:String="",
    var Date:String="",
    var Time:String="",
    var Price:String="",
    var seatNumber: Int=0,
    var isBooked: Boolean=false,
    var bookedByUserId: String="",

)
