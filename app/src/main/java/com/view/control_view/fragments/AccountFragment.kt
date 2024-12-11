package com.view.control_view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.controller.Bridge
import com.controller.Utils
import com.view.R
import com.view.databinding.FragmentAccountBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.mail.MessagingException


class AccountFragment : Fragment(), Utils {  //implementa l'interfaccia

    private lateinit var binding: FragmentAccountBinding  //chiama il suo xml
    private lateinit var parent: Context


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        parent = requireContext()
        setHasOptionsMenu(true)
        readyForOperations()
        return binding.root
    }


    private fun readyForOperations() {
        binding.switchAccess.isChecked = Bridge.database.access
        binding.switchAdvertising.isChecked = Bridge.database.advertising
        binding.switchNotification.isChecked = Bridge.database.notification


        binding.switchAccess.setOnCheckedChangeListener { _, isChecked ->
            Bridge.database.access = isChecked
        }
        binding.switchAdvertising.setOnCheckedChangeListener { _, isChecked ->
            Bridge.database.advertising = isChecked
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            Bridge.database.notification = isChecked
        }

        binding.btnNewPassword.setOnClickListener {
            run {
                startAnimation(binding.btnNewPassword)
                val newPassword = binding.fieldNewPassword.text.toString().trim()
                MainScope().launch {
                    try {
                        if(Bridge.database.sendDefaultLoginEmail(Bridge.database.SENDER, newPassword)==true){
                            Bridge.database.PASSWORD = newPassword

                            binding.fieldNewPassword.setText("")
                            shortToast(parent,"Email inviata con successo")
                        }else{
                            shortToast(parent, "Password non valida")
                        }



                    } catch (ilg: IllegalArgumentException) {
                        shortToast(parent, ilg.message.toString())
                    } catch (me: MessagingException) {
                        shortToast(parent, "Email non inviata,controlla le credenziali")
                    } catch (ex: Exception) {
                        shortToast(parent, "Qualcosa è andato storto")
                    }
                }

            }
        }

        binding.btnOpenArchive.setOnClickListener{
            run{
                startAnimation(binding.btnOpenArchive)
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                startActivity(intent)
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //vale solo per questo frammento

        /*
        override solo del Clear che in ogni Fragment esegue una cosa diversa,  LogOut fatto nel MAINACTIVITY
         */
        return when (item.itemId) {
            R.id.clear_item -> {
                Bridge.database.access=true
                Bridge.database.advertising=false
                Bridge.database.notification=true
                binding.switchAccess.isChecked = Bridge.database.access
                binding.switchAdvertising.isChecked = Bridge.database.advertising
                binding.switchNotification.isChecked = Bridge.database.notification
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}





