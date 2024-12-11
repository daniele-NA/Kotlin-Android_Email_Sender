package com.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import com.view.LoginActivity
import com.view.MainActivity


@SuppressLint("StaticFieldLeak")
object Switcher {

    /*
    inserisci qui tutte le attività
     */

    private val MAIN_PAGE:Class<out ComponentActivity> =MainActivity::class.java
    private val LOGIN_PAGE: Class<out ComponentActivity> =LoginActivity::class.java



    private var currentActivity: ComponentActivity? = null

    fun newCurrentActivity(a: Activity?) {
        this.currentActivity = a as ComponentActivity?   //prende sempre l'attività corrente
    }


    fun switchToMainPage() {        //array con valori da passare
        Intent(currentActivity?.applicationContext, MAIN_PAGE).also {
            currentActivity?.startActivity(it)
            currentActivity?.finish()
        }

    }

    fun switchToLoginPage() {        //obj che estende ComponentActivity
        Intent(currentActivity?.applicationContext, LOGIN_PAGE).also {
            currentActivity?.startActivity(it)
            currentActivity?.finish()
        }

    }

}