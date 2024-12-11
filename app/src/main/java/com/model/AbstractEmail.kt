package com.model

import java.io.File
import java.io.Serializable
import java.util.Properties

abstract class AbstractEmail:Serializable {

    abstract var senderTxt: String
    abstract var passwordTxt: String
    abstract var recipientTxt: String
    abstract var titleTxt: String
    abstract var bodyTxt: String
    abstract var file: File?

    val properties:Properties=Properties()

    init {
        /*
        stesse proprietà per tutte le classi email
         */
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = "587" // Usa la porta corretta (587 per TLS, 465 per SSL, etc.)
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"
        properties["mail.smtp.ssl.trust"] = "smtp.gmail.com"
        //properties["mail.debug"] = "true"
    }


     open fun checkTxt(parameterName: String, s: String) {
        if (s.isEmpty()) throw IllegalArgumentException(
            if (parameterName == "password") "La $parameterName è vuota"
            else "Il $parameterName è vuoto"
        )
    }

     open fun checkAddress(parameterName: String, s: String) {
        checkTxt(parameterName, s)
        if (!s.contains('@')) throw IllegalArgumentException("Il $parameterName non ha un dominio valido")  //se non contiene la chiocciola
    }

    abstract suspend fun sendEmail():Boolean?  //ogni tipo di email si fa il suo override

    override fun toString(): String {    //esclude il mittente e la password
        return " TO $recipientTxt : \n\n\t\t\t\t$titleTxt  : \n$bodyTxt "
    }
}