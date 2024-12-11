package com.model


import android.util.Log
import com.controller.Bridge
import java.io.File
import java.io.Serializable
import javax.mail.AuthenticationFailedException
import javax.mail.MessagingException

import kotlin.jvm.Throws


class Database : Serializable {

    private val listEmail: MutableList<AbstractEmail> = mutableListOf()
    val listEmailToString: MutableList<String> =
        mutableListOf()  //esclude il mittente e la password (toString())

    var SENDER: String = ""
    var PASSWORD: String = ""

    var access: Boolean = true
    var advertising: Boolean = false
    var notification: Boolean = true


    suspend fun sendSimpleEmail(
        recipientTxt: String,//tiene registrato sempre le credenziali
        titleTxt: String,
        bodyTxt: String,

        ): Boolean? {
        val simpleEmail = SimpleEmail(SENDER, PASSWORD, recipientTxt, titleTxt, bodyTxt)
        Log.i("message","sender ${Bridge.database.SENDER} password ${Bridge.database.PASSWORD}")

        val result = simpleEmail.sendEmail()
        listEmail.add(simpleEmail)   //la aggiunge solo se è valida
        listEmailToString.add(listEmail.last().toString())
        return result

    }

    suspend fun sendStructuredEmail(
        recipientTxt: String,
        titleTxt: String,
        bodyTxt: String,
        file: File
    ):Boolean? {
        val structuredEmail =
            StructuredEmail(SENDER, PASSWORD, recipientTxt, titleTxt, bodyTxt, file)
        val result=structuredEmail.sendEmail()
        listEmail.add(structuredEmail)
        listEmailToString.add(listEmail.last().toString())
        return result
    }

    fun clearAllEmailData() {
        listEmail.clear()
        listEmailToString.clear()
    }

    fun clearCredentials() {
        SENDER = ""
        PASSWORD = ""
    }


    @Throws(MessagingException::class, AuthenticationFailedException::class)
    suspend fun sendDefaultLoginEmail(email: String, password: String):Boolean? {
        /*mando una mail a me stesso ,1° per vedere se l'utente è valido
          2°mi avverte di un login nuovo*/
        val simpleEmail = SimpleEmail(
            email,
            password,
            "yourdefaultemail@gmail.com",
            "ATTENZIONE",
            "nuovo utente :  $email "
        )
        return simpleEmail.sendEmail()
    }


}












