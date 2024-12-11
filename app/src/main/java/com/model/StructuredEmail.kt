package com.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.AuthenticationFailedException
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import kotlin.jvm.Throws

class StructuredEmail(
    senderTxt: String,
    passwordTxt: String,
    recipientTxt: String,
    titleTxt: String,
    bodyTxt: String,
    file: File
) : AbstractEmail(), Serializable {

    override var senderTxt: String = senderTxt
    override var passwordTxt: String = passwordTxt
    override var recipientTxt: String = recipientTxt
    override var titleTxt: String = titleTxt
    override var bodyTxt: String = bodyTxt
    override var file: File? = file

    init {
        super.checkAddress("mittente", this.senderTxt)
        super.checkTxt("password", this.passwordTxt)
        super.checkAddress("destinatario", this.recipientTxt)
        super.checkTxt("titolo", this.titleTxt)
        super.checkTxt("corpo", this.bodyTxt)
        if (this.file == null) {
            throw IllegalArgumentException("File non valido")
        }
    }

    @Throws(MessagingException::class, AuthenticationFailedException::class)
    override suspend fun sendEmail(): Boolean? {
        var result: Boolean? = null
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {

            try {

                /*

                utilizziamo il try nel metodo perchè la coroutine non può comunicare le eccezioni
                che lancia al thread principale,quindi ci avvaliamo di un boolean
                 */

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderTxt, passwordTxt)
                    }
                })


                // Creazione del messaggio
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(senderTxt))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientTxt))
                message.subject = titleTxt

                // Creazione della parte testuale e dell'allegato
                val textPart = MimeBodyPart()
                textPart.setText(bodyTxt)


                val fileExtension = file?.name.toString().substringAfterLast('.', "")
                val mimeType = when (fileExtension.lowercase()) {
                    "jpg", "jpeg" -> "image/jpeg"
                    "png" -> "image/png"
                    "pdf" -> "application/pdf"
                    "txt" -> "text/plain"
                    "doc", "docx" -> "application/msword"
                    "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    else -> "application/octet-stream" // Default per file sconosciuti
                }


                val attachmentPart = MimeBodyPart()
                val fileDataSource = FileDataSource(file)  // Passa il percorso del file (URI)
                attachmentPart.dataHandler = DataHandler(fileDataSource)
                attachmentPart.fileName = file?.name


                // Creazione della parte multipart
                val multipart = MimeMultipart()
                multipart.addBodyPart(textPart)
                multipart.addBodyPart(attachmentPart)
                attachmentPart.setHeader("Content-Type", mimeType)  // Impostiamo il tipo MIME

                message.setContent(multipart)

                Transport.send(message)
                result = true
            } catch (_: Exception) {
                result = false
            }
        }
        delay(2000)
        if (result == null) {
            delay(1000)
        }
        return result
    }

    override fun toString(): String {    //esclude il mittente e la password,ma ggiunge il nome dle file
        return super.toString() + "\n file= ${this.file?.name}"
    }


}