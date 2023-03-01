package com.appsnado.haippNew.Helper

import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class FileHelper(private val ctx: Context?) {

    /**
     * This image file can be used for a temporary location to store image file
     * on an external location which can be used for tasks such as displaying an
     * internal use of app.
     *
     * Also deletes existing image file.
     *
     * Dont Use it for external purposes.
     *
     * @return
     */
    fun getTempImageFile(): File? {
        val file = File(getAvailableCache(), "tempImage.jpg")
        if (file.exists()) file.delete()
        return file
    }

    fun getTempTxtFile(fileName: String?): File? {
        val file = File(getAvailableCache(), "$fileName.txt")
        if (file.exists()) file.delete()
        return file
    }

    fun getHttpCacheFile(): File? {
        return File(getAvailableCache(), "httpcache")
    }

    fun getAvailableCache(): File? {
        return if (OSHelper.Companion.isExerternalStorageAvailable()) {
            if (ctx!!.getExternalCacheDir() != null) ctx.getExternalCacheDir()!!.mkdirs()
            ctx.getExternalCacheDir()
        } else {
            ctx!!.getCacheDir().mkdirs()
            ctx.getCacheDir()
        }
    }

    fun writeToFile(file: File?, content: String?) {
        try {
            val fos = FileOutputStream(file)
            fos.write(content!!.toByteArray())
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun getFile(fileName: String?): File? {
            return File(fileName)
        }
    }

}