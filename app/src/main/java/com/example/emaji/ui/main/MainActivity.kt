package com.example.emaji.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.emaji.R
import com.example.emaji.ui.login.LoginActivity
import com.example.emaji.ui.main.file.FileFragment
import com.example.emaji.ui.main.history.HistoryFragment
import com.example.emaji.ui.main.home.HomeFragment
import com.example.emaji.ui.main.profile.ProfileFragment
import com.example.emaji.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragment : Fragment? = null
    companion object{
        var navStatus = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }
        isLoggedIn()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = HomeFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_history -> {
                if(navStatus != 1){
                    fragment = HistoryFragment()
                    navStatus = 1
                }
            }
            R.id.navigation_file -> {
                if(navStatus != 2){
                    fragment = FileFragment()
                    navStatus = 2
                }
            }
            R.id.navigation_account -> {
                if(navStatus != 3){
                    fragment = ProfileFragment()
                    navStatus = 3
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = HomeFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun isLoggedIn(){
        if(Constants.getToken(this@MainActivity).equals("UNDEFINED")){
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }
}