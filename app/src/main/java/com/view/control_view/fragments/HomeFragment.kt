package com.view.control_view.fragments


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.controller.Bridge
import com.controller.Utils
import com.view.R
import com.view.databinding.FragmentHomeBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.mail.MessagingException


class HomeFragment : Fragment(), Utils {

    private lateinit var binding: FragmentHomeBinding   //chiama il suo xml
    private lateinit var parent: Context

    private var fileExtracted: File? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        parent = requireContext()
        this.setHasOptionsMenu(true)  //specifica la presenza di un menu
        readyForOperations()
        return binding.root
    }


    @SuppressLint("Recycle")
    private fun readyForOperations() {

        binding.btnSending.setOnClickListener {
            run {
                startAnimation(binding.btnSending)

                binding.progressBar.max = 5
                ObjectAnimator.ofInt(binding.progressBar, "progress", 3)
                    .setDuration(1000)
                    .start()
                MainScope().launch {
                    try {
                        val result: Boolean?

                        val recipientTxt = binding.fieldEmailRecipient.text.toString().trim()
                        val titleTxt = binding.fieldTitleSender.text.toString().trim()
                        val bodyTxt = binding.fieldBodySender.text.toString().trim()
                        if (fileExtracted != null) {
                            result=Bridge.database.sendStructuredEmail(
                                recipientTxt,
                                titleTxt,
                                bodyTxt,
                                fileExtracted!!
                            )
                            fileExtracted = null
                            binding.uploadFile.setImageResource(R.drawable.upload_icon)
                        } else {
                            result =
                                Bridge.database.sendSimpleEmail(recipientTxt, titleTxt, bodyTxt)
                        }

                        if (result == true) {
                            ObjectAnimator.ofInt(binding.progressBar, "progress", 6)
                                .setDuration(1000)
                                .start()

                            clearInput()
                            shortToast(parent, "Email inviata")
                            sendNotification(parent, "Email inviata con successo")
                        } else {
                            shortToast(parent, "Email non inviata,controlla le credenziali")
                        }
                    } catch (ilg: IllegalArgumentException) {
                        shortToast(parent, ilg.message.toString())
                    } catch (na: NullPointerException) {
                        shortToast(parent, "File non trovato")
                    } catch (ex: Exception) {
                        shortToast(parent, "Email non inviata,controlla le credenziali")
                    }


                }
                /*
                         aspetta pochi secondi e svuota la progressBar
                         */
                Handler().postDelayed({
                    binding.progressBar.progress = 0  // Reset progress
                }, 3000)  // 3000ms = 3 secondi

            }


        }

        binding.uploadFile.setOnClickListener { run { openFileChooser() } }

        binding.cancelFile.setOnClickListener {
            run {
                fileExtracted = null
                binding.uploadFile.setImageResource(R.drawable.upload_icon)
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //vale solo per questo frammento
        /*
      override solo del Clear che in ogni Fragment esegue una cosa diversa,  LogOut fatto nel MAINACTIVITY
       */
        return when (item.itemId) {
            R.id.clear_item -> {
                clearInput()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun clearInput() {
        binding.fieldEmailRecipient.text.clear()
        binding.fieldTitleSender.text.clear()
        binding.fieldBodySender.text.clear()

        fileExtracted = null
        binding.uploadFile.setImageResource(R.drawable.upload_icon)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (1 == requestCode && resultCode == Activity.RESULT_OK) {
            fileExtracted = copyUriToFile(data?.data!!)  //assegna al file globale il file estratto
            if (fileExtracted != null) {
                binding.uploadFile.setImageResource(R.drawable.file_present_icon)
            }
        }


    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 1)
    }


    private fun copyUriToFile(uri: Uri): File? {
        val contentResolver = parent.contentResolver

        // Ottieni il nome del file (inclusa l'estensione) dal ContentResolver
        val fileName = getFileNameFromUri(uri)

        // Crea un file temporaneo nella directory cache
        val tempFile = File(parent.cacheDir, fileName ?: "tempFile_${System.currentTimeMillis()}")

        try {
            // Ottieni un InputStream dal ContentResolver
            val inputStream = contentResolver.openInputStream(uri)

            // Scrivi i dati nel file temporaneo
            inputStream?.use { input ->
                val outputStream = FileOutputStream(tempFile)
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Restituisci il file creato
            return tempFile

        } catch (_: Exception) {
        }

        return null
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null

        // Se il URI ha uno schema content://, ottieni il nome dal ContentResolver
        if (uri.scheme == "content") {
            val cursor = parent.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex("_display_name")
                    fileName = cursor.getString(columnIndex)
                }
            }
        } else if (uri.scheme == "file") {
            // Se è un URI file://, ottieni il nome dal percorso del file
            fileName = uri.path?.substringAfterLast("/")
        }

        return fileName
    }


}












