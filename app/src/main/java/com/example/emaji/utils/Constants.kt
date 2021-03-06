package com.example.emaji.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class Constants {
    companion object{
        //val END_POINT = "https://fruitman-project.herokuapp.com/"
        const val END_POINT = "http://emaji.alfara-dev.com/api/"
        //val token = "Bearer EiBxBgjt1afSMsvFcoCLoNxLmxWwt4hA4FPO2JVdvjj27gzMh2TTxFwxGmBmgFLz45OF9hkFX2F9oGni"


        fun getToken(c : Context) : String {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
            val token = s?.getString("TOKEN", "UNDEFINED")
            return token!!
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(pass : String) = pass.length >= 8

        fun setToIDR(num : Int) : String {
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(num)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun changeFormatDate(date : String) : String{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return current.format(formatter)
        }

        fun isAlpha(name : String) = Pattern.matches("[a-zA-Z]+", name)

    }
}