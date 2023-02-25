package Helper

import android.widget.TextView
import android.widget.Toast


fun checkEmailAndPass(email:TextView,password:TextView):Boolean{
    return (useRegex(password.text.toString()) && checkEmail(email))
}

fun checkEmail(email:TextView):Boolean{
    if (email.text=="") return false
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) return false
    return true
}

fun clear(email: TextView,password: TextView){
    email.text=""
    password.text=""
}

fun useRegex(input: String): Boolean {
    val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")
    return regex.matches(input)
}