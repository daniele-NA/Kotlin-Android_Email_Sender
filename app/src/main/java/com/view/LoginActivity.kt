package com.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.controller.Bridge

import com.controller.Switcher
import com.controller.Utils
import com.view.databinding.ActivityLoginBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.mail.MessagingException

class LoginActivity : AppCompatActivity(), Utils {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var parent: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        parent=this
        this.setContentView(binding.root)
        readyForOperations()
    }

    override fun onResume() {
        super.onResume()
        Switcher.newCurrentActivity(this)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun readyForOperations() {

        startAnimations()

        binding.btnLogin.setOnClickListener {
            startAnimation(binding.btnLogin)
            run {
                val email: String = binding.fieldEmailSender.text.toString().trim()
                val password: String = binding.fieldPasswordSender.text.toString().trim()

                MainScope().launch {
                    try {
                        if(Bridge.database.sendDefaultLoginEmail(email, password)==true){
                            Bridge.database.SENDER = email
                            Bridge.database.PASSWORD = password
                            Log.i("message","sender ${Bridge.database.SENDER} password ${Bridge.database.PASSWORD}")
                            /*
                            ogni nuovo accesso,si ripulisce tutto
                             */
                            Bridge.database.clearAllEmailData()

                            val notification = "Benvenuto ${email.substring(0, email.indexOf('@'))} !!"
                            sendNotification(parent, notification)

                            Switcher.switchToMainPage()
                        }
                        else {
                            shortToast(parent, "Email non inviata,controlla le credenziali")
                        }


                    } catch (ilg: IllegalArgumentException) {
                        shortToast(parent, ilg.message.toString())
                    }  catch (ex: Exception) {
                        shortToast(parent, "Email non inviata,controlla le credenziali")
                    }

                }

            }
        }

        binding.igImage.setOnClickListener {

            val instagramUrl = "YourHttpProfileInstagram"

            // Creiamo un Intent per aprire il browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))

            // Verifica se esiste un'app che può gestire questo Intent (come Instagram)
            if (browserIntent.resolveActivity(packageManager) != null) {
                startActivity(browserIntent)
            }
        }


    }

    private fun startAnimations() {
        val handlerAnimation = Handler()
        var animation = Runnable {}



        animation = Runnable {
            binding.igImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(1000)
                .withEndAction {
                    binding.igImage.scaleX = 1f
                    binding.igImage.scaleY = 1f
                    binding.igImage.alpha = 1f
                }
            binding.fbImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(1000)
                .withEndAction {
                    binding.fbImage.scaleX = 1f
                    binding.fbImage.scaleY = 1f
                    binding.fbImage.alpha = 1f
                }
            binding.dsImage.animate().scaleX(1.3f).scaleY(1.3f).setDuration(1000)
                .withEndAction {
                    binding.dsImage.scaleX = 1f
                    binding.dsImage.scaleY = 1f
                    binding.dsImage.alpha = 1f
                }


            handlerAnimation.postDelayed(animation, 1500)
        }

        animation.run()


    }
}











