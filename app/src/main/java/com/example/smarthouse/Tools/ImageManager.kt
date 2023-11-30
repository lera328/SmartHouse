package com.example.smarthouse.Tools

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageManager {

    // Функция для перевода изображения в двоичный формат
    fun imageToByteArray(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val byteArray = inputStream.readBytes()
        inputStream.close()
        val base64String = byteArrayToBase64(byteArray)
        return base64String
    }
    fun imageToByteArray(context: Context, imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val byteArray = inputStream?.readBytes()
        inputStream?.close()

        if (byteArray != null) {
            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return base64String
        } else {
            return ""
        }
    }

    // Функция для перевода двоичных данных обратно в изображение
    fun byteArrayToImage(byteArray: String, outputFile: File) {
        val decodedImageByteArray = base64ToByteArray(byteArray)
        val outputStream = FileOutputStream(outputFile)
        outputStream.write(decodedImageByteArray)
        outputStream.close()
    }

    fun byteArrayToBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Функция для декодирования строки Base64 обратно в массив байтов
    fun base64ToByteArray(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }

}