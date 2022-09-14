import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat


fun Context.hideKeyboard(currentFocus: View?) {
    if (currentFocus != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.calculateTheDifferenceInTime(time1:String, time2:String?):String{
    if(time2.isNullOrEmpty() || time2.equals("N/A",true)){
        return "N/A"
    }
    val simpleDateFormat = SimpleDateFormat("hh:mm a")

    val t1 = simpleDateFormat.parse(time1)
    val t2 = simpleDateFormat.parse(time2)

    val differenceInMilliSeconds: Long = Math.abs(t2.getTime() - t1.getTime())

    //hours
    var differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000) % 24)

    //Minutes
    var differenceInMinutes = differenceInMilliSeconds / (60 * 1000) % 60

    //Seconds
    var differenceInSeconds = differenceInMilliSeconds / 1000 % 60

    differenceInHours = if (differenceInHours < 0) -differenceInHours else differenceInHours
    differenceInMinutes = if (differenceInMinutes < 0) -differenceInMinutes else differenceInMinutes
    differenceInSeconds = if (differenceInSeconds < 0) -differenceInSeconds else differenceInSeconds

    return "$differenceInHours:$differenceInMinutes:$differenceInSeconds"
}

@SuppressLint("SimpleDateFormat")
fun Context.getValueForProgressFromTimeDifference(time1:String, time2:String?):Int{
    if(time2.isNullOrEmpty() || time2.equals("N/A",true)){
        return 100
    }
    val simpleDateFormat = SimpleDateFormat("hh:mm a")

    val t1 = simpleDateFormat.parse(time1)
    val t2 = simpleDateFormat.parse(time2)

    val differenceInMilliSeconds: Long = Math.abs(t2.getTime() - t1.getTime())

    //hours
    var differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000) % 24)

    //Minutes
    var differenceInMinutes = differenceInMilliSeconds / (60 * 1000) % 60

    //Seconds
    var differenceInSeconds = differenceInMilliSeconds / 1000 % 60

    differenceInHours = if (differenceInHours < 0) -differenceInHours else differenceInHours
    differenceInMinutes = if (differenceInMinutes < 0) -differenceInMinutes else differenceInMinutes
    differenceInSeconds = if (differenceInSeconds < 0) -differenceInSeconds else differenceInSeconds



    return if(differenceInHours <= 0 && differenceInMinutes<=10){
        differenceInMinutes.toInt()
    }else{
        100
    }
}


