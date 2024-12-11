package com.controller

import android.content.Context
import com.model.Database
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.jvm.Throws


object Bridge {

    var database: Database = Database()

    private const val FILE_NAME: String = "repositoryEmail"


    fun writeObjectToFile(context: Context) {
        val fileOutputStream: FileOutputStream =
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(database)
        objectOutputStream.close()
    }

    @Throws(Exception::class)
    fun readObjectFromFile(context: Context) {
        val fileInputStream: FileInputStream = context.openFileInput(FILE_NAME)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val obj = objectInputStream.readObject() as Database
        objectInputStream.close()
        this.database = obj
    }

}






