package com.model



import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable
import javax.mail.AuthenticationFailedException
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.jvm.Throws

class SimpleEmail(
    senderTxt: String,
    passwordTxt: String,
    recipientTxt: String,
    titleTxt: String,
    bodyTxt: String,
) : AbstractEmail(), Serializable {

    override var senderTxt: String = senderTxt
    override var passwordTxt: String = passwordTxt
    override var recipientTxt: String = recipientTxt
    override var titleTxt: String = titleTxt
    override var bodyTxt: String = bodyTxt
    override var file: File? = null

    init {
        super.checkAddress("mittente", this.senderTxt)
        super.checkTxt("password", this.passwordTxt)
        super.checkAddress("destinatario", this.recipientTxt)
        super.checkTxt("titolo", this.titleTxt)
        super.checkTxt("corpo", this.bodyTxt)
    }


    @Throws(MessagingException::class, AuthenticationFailedException::class)
    override suspend fun sendEmail():Boolean? {  //override della classe astratta
        var result:Boolean?=null
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {

            try {
                val session = Session.getInstance(properties,
                    object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(senderTxt, passwordTxt)
                        }
                    })
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(senderTxt))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientTxt)
                )
                message.subject = titleTxt
                message.setText(bodyTxt)
                Transport.send(message)
                result=true
            } catch (_: Exception) {
                result=false
            }
        }
        delay(2000)
        if(result==null){
            delay(1000)
        }
        return result

    }


    override fun toString(): String {    //esclude il mittente e la password
        return super.toString()
    }


}



