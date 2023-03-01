package com.appsnado.haippNew.Helper

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.media.ExifInterface
import android.util.DisplayMetrics
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapHelper {
    fun getImageOrientation(_path: String?, bitmap: Bitmap?): Bitmap? {
        var exif: ExifInterface? = null
        var bmp: Bitmap? = null
        try {
            exif = ExifInterface(_path!!)
            val exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            var rotate = 0
            when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = -90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = -180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = -270
                ExifInterface.ORIENTATION_NORMAL -> rotate = 0
            }
            val w = bitmap!!.getWidth()
            val h = bitmap.getHeight()
            return if (rotate != 0) {
                val mtx = Matrix()
                mtx.setRotate(rotate.toFloat())
                mtx.preRotate(rotate.toFloat())
                mtx.postRotate(rotate.toFloat())

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                // if(rotate == 0)
                // bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true);
                // else
                bmp = Bitmap.createBitmap(bitmap!!, 0, 0, w, h, mtx, true)
                // bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                bmp
            } else {
                bmp = Bitmap.createBitmap(bitmap!!, 0, 0, w, h, null, true)
                bmp
            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return null
    }

    fun scaleCenterCrop(source: Bitmap?, newHeight: Int,
                        newWidth: Int): Bitmap? {
        val sourceWidth = source!!.getWidth()
        val sourceHeight = source!!.getHeight()
        val xScale = newWidth as Float / sourceWidth
        val yScale = newHeight as Float / sourceHeight
        val scale = Math.max(xScale, yScale)

        // get the resulting size after scaling
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // figure out where we should translate to
        val dx = (newWidth - scaledWidth) / 2
        val dy = (newHeight - scaledHeight) / 2
        val dest = Bitmap.createBitmap(newWidth, newHeight,
                source.getConfig())
        val canvas = Canvas(dest)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        matrix.postTranslate(dx, dy)
        canvas.drawBitmap(source, matrix, null)
        return dest
    }

    fun getScreenSize(activity: Activity?): Float {
        val metrics = DisplayMetrics()
        activity!!.getWindowManager().defaultDisplay.getMetrics(metrics)
        val height = metrics.heightPixels / metrics.xdpi
        val width = metrics.widthPixels / metrics.ydpi
        return Math.sqrt(height * height + width * width.toDouble()) as Float
    }

    fun getRoundedCornerImage(bitmap: Bitmap?): Bitmap? {
        val output = Bitmap.createBitmap(bitmap!!.getWidth(),
                bitmap!!.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.getWidth(), bitmap.getHeight())
        val rectF = RectF(rect)
        val roundPx = 100f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun getImageOrientation(bitmap: Bitmap?, rotate: Int): Bitmap? {
        var bmp: Bitmap? = null
        try {
            val w = bitmap!!.getWidth()
            val h = bitmap!!.getHeight()
            return if (rotate != 0) {
                val mtx = Matrix()
                mtx.setRotate(rotate.toFloat())
                mtx.preRotate(rotate.toFloat())
                mtx.postRotate(rotate.toFloat())
                bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
                bmp
            } else {
                bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true)
                bmp
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun scaleCenterCrop(srcBmp: Bitmap?): Bitmap? {
        return if (srcBmp!!.getWidth() >= srcBmp.getHeight()) {
            Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2
                    - srcBmp.getHeight() / 2, 0, srcBmp.getHeight(),
                    srcBmp.getHeight())
        } else {
            Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight()
                    / 2 - srcBmp.getWidth() / 2, srcBmp.getWidth(),
                    srcBmp.getWidth())
        }
    }

    fun convertBitmapToFile(context: Context?, mBitmap: Bitmap?): File? {
        val f = File(context!!.getCacheDir(), "temp")
        try {
            // create a file to write bitmap data
            f.createNewFile()

            // Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            mBitmap!!.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos)
            val bitmapdata = bos.toByteArray()

            // write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return f
    }

    fun replace(activity: Activity?, bmp: Bitmap?, paths: String?): String? {
        val bytes = ByteArrayOutputStream()
        var quality = 100
        val density = bmp!!.getDensity()
        if (density > 0 && density <= 160) {
            quality = 75
        } else if (density > 160 && density <= 360) {
            quality = 50
        } else if (density > 360) {
            quality = 25
        }
        bmp.compress(CompressFormat.JPEG, quality, bytes)
        val file = File(paths)
        try {
            file.createNewFile()
            val ostream = FileOutputStream(file)
            ostream.write(bytes.toByteArray())
            ostream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return paths
    }
}