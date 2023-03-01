package com.appsnado.haippNew.Helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils.TruncateAt
import android.text.format.Time
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.Applocakpacakges.LockApplication
import com.appsnado.haippNew.BrowserApp
import com.appsnado.haippNew.R
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceConstants.PARAMS_BADREQUEST
import com.appsnado.haippNew.retro.WebServiceConstants.PARAMS_TOKEN_BLACKLIST
import com.appsnado.haippNew.retro.WebServiceConstants.PARAMS_TOKEN_EXPIRE
import com.appsnado.haippNew.retro.WebServiceConstants.SUCCESSRESULT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*
import kotlin.jvm.Throws

class UIHelper {
    fun generateNoteOnSD(context: Context?, sFileName: String?, sBody: String?) {
        try {
            val root = File(Environment.getExternalStorageDirectory(), "Notes")
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, sFileName)
            val writer = FileWriter(gpxfile)
            writer.append(sBody)
            writer.flush()
            writer.close()
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun isAboveKitKat(): Boolean {
        var ret = false
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        return ret
    }

    companion object {
        const val READ_BLOCK_SIZE = 100
        var dialog1: Dialog? = null
        private val mToast = Toast.makeText(
                BrowserApp.getInstance()!!.getApplicationContext(),
            "",
            Toast.LENGTH_SHORT
        )
        private val mToastLong = Toast.makeText(
            BrowserApp.getInstance()!!.getApplicationContext(),
            "",
            Toast.LENGTH_LONG
        )
        fun showToast(message: CharSequence?) {
            mToast.setText(message)
            mToast.setGravity(Gravity.CENTER, 0, 0)
            mToast.show()
        }

        fun showToast(@StringRes resId: Int) {
            mToast.setText(resId)
            mToast.setGravity(Gravity.CENTER, 0, 0)
            mToast.show()
        }


        fun showprogress(context: Context?) {
            try {

                if (dialog1 != null) dialog1!!.dismiss()
                ///  AppConstants settingsMain = new AppConstants(context);
                dialog1 = Dialog(context!!, R.style.ThemeTransparent)
                dialog1!!.setContentView(R.layout.dilog_progressbar_view)
                dialog1!!.setCancelable(false)
                val textView = dialog1!!.findViewById<TextView?>(R.id.id_title)
                // textView.setText(settingsMain.getAlertDialogMessage("waitMessage"));
                dialog1!!.show()


            } catch (e: Exception) {
                e.stackTrace
            }
        }


        fun showDilog(context: Context?) {
            try {

               if (dialog1 != null) dialog1!!.dismiss()
                ///  AppConstants settingsMain = new AppConstants(context);
                dialog1 = Dialog(context!!, R.style.ThemeTransparent)
                dialog1!!.setContentView(R.layout.dilog_progressbar)
                dialog1!!.setCancelable(false)
                val textView = dialog1!!.findViewById<TextView?>(R.id.id_title)
                // textView.setText(settingsMain.getAlertDialogMessage("waitMessage"));
                dialog1!!.show()


            } catch (e: Exception) {
                e.stackTrace
            }
        }

        fun hidedailog() {
            if (dialog1 != null) dialog1!!.dismiss()
        }




        @SuppressLint("ResourceType")
        fun showsuccesscustomsnackbar(
                activity: Activity?,
                message: CharSequence?,
                coordinatorLayout: LinearLayout
        ) {


            val snackbar = Snackbar.make(coordinatorLayout, ""!!, 5000)
            // 15 is margin from all the sides for snackbar
            // 15 is margin from all the sides for snackbar
//            val marginFromSides = 15

            //val height = 100f

            //inflate view

            //inflate view
            val snackView: View = activity!!.getLayoutInflater().inflate(R.layout.layout_success_custom_snackbar, null)

            // White background

            // White background
            snackbar.view.setBackgroundColor(Color.WHITE)
            // for rounded edges
            // for rounded edges
            //00aa00
            //snackbar.view.background = activity!!.getResources().getDrawable(R.drawable)

            val snackBarView = snackbar.view as SnackbarLayout
            val parentParams: FrameLayout.LayoutParams =
                    snackBarView.layoutParams as FrameLayout.LayoutParams


            val textView = snackView.findViewById<TextView?>(R.id.tv_message)
            textView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            val typeface = ResourcesCompat.getFont(activity, R.font.nunito)
            textView!!.setTypeface(typeface)
            textView?.text = message

            snackBarView.setBackgroundColor(Color.parseColor("#00aa00"))

            //parentParams.setMargins(marginFromSides, 0, marginFromSides, marginFromSides)
            //parentParams.height = height.toInt()
            //parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT
            snackBarView.layoutParams = parentParams

            snackBarView.addView(snackView, 0)
            snackbar.show()

        }



        @SuppressLint("ResourceType")
        fun showcustomsnackbar(
            activity: Activity?,
            message: CharSequence?,
            coordinatorLayout: LinearLayout
        ) {


            val snackbar = Snackbar.make(coordinatorLayout, ""!!, 5000)
            // 15 is margin from all the sides for snackbar
            // 15 is margin from all the sides for snackbar
//            val marginFromSides = 15

            //val height = 100f

            //inflate view

            //inflate view
            val snackView: View = activity!!.getLayoutInflater().inflate(R.layout.layout_simple_custom_snackbar, null)

            // White background

            // White background
            snackbar.view.setBackgroundColor(Color.WHITE)
            // for rounded edges
            // for rounded edges
            //snackbar.view.background = activity!!.getResources().getDrawable(R.drawable)

            val snackBarView = snackbar.view as SnackbarLayout
            val parentParams: FrameLayout.LayoutParams =
                snackBarView.layoutParams as FrameLayout.LayoutParams

            snackBarView.setBackgroundColor(Color.parseColor("#AA0000"))
            val textView = snackView.findViewById<TextView?>(R.id.tv_message)
            textView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            //val typeface = ResourcesCompat.getFont(activity, R.font.nunito)
           // textView!!.setTypeface(typeface)
            textView?.text = message

            //parentParams.setMargins(marginFromSides, 0, marginFromSides, marginFromSides)
            //parentParams.height = height.toInt()
            //parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT
            snackBarView.layoutParams = parentParams

            snackBarView.addView(snackView, 0)
             snackbar.show()

        }


        //shows snack Bar with message
        fun showMessageSnackBar(activity: Activity?, message: CharSequence?) {
            val viewGroup = (activity!!.findViewById<View?>(android.R.id.content) as ViewGroup).getChildAt(
                0
            ) as ViewGroup
            val snackbar = Snackbar.make(viewGroup, message!!, Snackbar.LENGTH_LONG)
            val snackBarView = snackbar.view
            val textView = snackBarView.findViewById<TextView?>(R.id.snackbar_text)
            textView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            val typeface = ResourcesCompat.getFont(activity, R.font.nunito)
            textView!!.setTypeface(typeface)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView!!.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                textView!!.gravity = Gravity.CENTER_HORIZONTAL
            }
            snackBarView.setBackgroundColor(Color.parseColor("#003466"))
            snackbar.show()
        }

        fun showToastLong(message: CharSequence?) {
            mToastLong.setText(message)
            mToastLong.setGravity(Gravity.CENTER, 0, 0)
            mToastLong.show()
        }

        fun showToastLong(@StringRes resId: Int) {
            mToastLong.setText(resId)
            mToastLong.setGravity(Gravity.CENTER, 0, 0)
            mToastLong.show()
        }

        fun showToastLo(context: Context?, message: String?) {
            //  MaterialToast.makeText(context, message, R.mipmap.ic_launcher, Toast.LENGTH_SHORT).show();
        }

        fun hideSoftKeyboard(activity: MainActivity?, editText: View?) {
            if (activity == null) {
                return
            }
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText!!.getWindowToken(), 0)
        }

        fun hideSoftKeyboard(context: Context?, view: View?) {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }

        fun showAlertDialog(message: String?, title: CharSequence?, context: Context?) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message).setTitle(title).setCancelable(true)
                    .setNegativeButton("OK") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        fun locateView(v: View?): Rect? {
            val loc_int = IntArray(2)
            if (v == null) return null
            try {
                v.getLocationOnScreen(loc_int)
            } catch (npe: NullPointerException) {
                // Happens when the view doesn't exist img_toggle_on screen anymore.
                return null
            }
            val location = Rect()
            location.left = loc_int[0]
            location.top = loc_int[1]
            location.right = location.left + v.width
            location.bottom = location.top + v.height
            return location
        }

        fun textMarquee(txtView: TextView?) {
            // Use this to marquee Textview inside listview
            txtView!!.setEllipsize(TruncateAt.END)
            // Enable to Start Scroll

            // txtView.setMarqueeRepeatLimit(-1);
            // txtView.setHorizontallyScrolling(true);
            // txtView.setSelected(true);
        }

//        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//        fun getScreenWidth(ctx: Activity?): Int {
//            val display = ctx!!.getWindowManager().defaultDisplay
//            return if (OSHelper.Companion.hasHoneycombMR2()) {
//                val size = Point()
//                display.getSize(size)
//                size.x
//            } else {
//                display.width
//            }
//        }

        fun dimBehind(dialog: Dialog?) {
            val lp = dialog!!.getWindow()!!.getAttributes()
            lp.dimAmount = 0.9f
            dialog!!.getWindow()!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog.setCancelable(false)
        }

        fun hideSoftKeyboard(activity: Activity?) {
            val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity!!.getCurrentFocus()
                !!.getWindowToken(), 0
            )
        }

        fun currentTime(): String? {
            val now = Time()
            now.setToNow()
            return (pad(now.year) + "-" + pad(now.month + 1) + "-"
                    + pad(now.monthDay) + " " + pad(now.hour) + ":"
                    + pad(now.minute) + ":" + pad(now.second))
        }

        fun pad(c: Int): String? {
            return if (c >= 10) c.toString() else "0$c"
        }

        fun showAlertDialogWithButtons(
            questionText: String?,
            title: String?, positiveText: String?, negativeText: String?,
            dialogListener: DialogInterface.OnClickListener?, context: Context?
        ): AlertDialog? {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(questionText).setTitle(title).setCancelable(true)
                    .setPositiveButton(positiveText, dialogListener)
                    .setNegativeButton(negativeText, dialogListener)
            val alert = builder.create()
            alert.show()
            return alert
        }

        fun getOrientation(context: Context?, photoUri: Uri?): Int {
            /* it's img_toggle_on the external media. */
            val cursor = context!!.getContentResolver().query(
                photoUri!!, arrayOf<String?>(
                    MediaStore.Images.ImageColumns.ORIENTATION
                ),
                null, null, null
            )
            if (cursor!!.getCount() != 1) {
                return -1
            }
            cursor!!.moveToFirst()
            return cursor.getInt(0)
        }

        fun getOrientaionFromRotation(rotation: Int): Int {
            if (rotation == 90) return 6
            if (rotation == 180) return 3
            return if (rotation == 270) 8 else 0
        }

        fun saveImage(context: Context?, bmp: Bitmap?, fileName: String?): Boolean {
            var file_saved = false
            try {
                val dirList = getDirList()
                for (i in dirList!!.indices) {
                    if (dirList.get(i)!!.contains("sdcard")) {
                        val NewFolder = "/Whistle"
                        var extStorageDirectory: String
                        extStorageDirectory = "/mnt/" + dirList.get(i)
                        val mkTarDir = File(extStorageDirectory + NewFolder)
                        if (mkTarDir.isDirectory) {
                            file_saved = saveImage(
                                bmp, fileName, NewFolder,
                                extStorageDirectory
                            )
                            break
                        } else {
                            val dir = mkTarDir.mkdir()
                            if (dir) {
                                if (dir) {
                                    file_saved = saveImage(
                                        bmp, fileName,
                                        NewFolder, extStorageDirectory
                                    )
                                    break
                                }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return file_saved
        }

        protected fun getDirList(): Array<String?>? {
            val storageDir = File("/mnt/")
            return if (storageDir.isDirectory) {
                storageDir.list()
            } else null
        }

        @Throws(FileNotFoundException::class, IOException::class)
        protected fun saveImage(
            bmp: Bitmap?, fileName: String?,
            NewFolder: String?, extStorageDirectory: String?
        ): Boolean {
            val file_saved: Boolean
            val stream: OutputStream = FileOutputStream(
                extStorageDirectory
                        + NewFolder + "/" + fileName
            ) // "+.jpg"
            file_saved = bmp!!.compress(CompressFormat.JPEG, 50, stream)
            stream.close()
            return file_saved
        }

        fun getFileName(ext: String?): String? {
            val calendar = Calendar.getInstance()
            val now = calendar.time
            val time = now.time
            return "$time.$ext"
        }

        fun getFullImagePath(filename: String?): String? {
            val dirList = getDirList()
            for (i in dirList!!.indices) {
                if (dirList.get(i)!!.contains("sdcard")) {
                    val NewFolder = "/Whistle"
                    val extStorageDirectory: String
                    extStorageDirectory = "/mnt/" + dirList.get(i) + NewFolder
                    return "$extStorageDirectory/$filename"
                }
            }
            return null
        }

        fun deleteAllFiles(): Boolean {
            val direct = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Whistle"
            )
            if (direct.exists()) {
                for (c in direct.listFiles()) {
                    c.delete()
                }
            }
            return false
        }

        // write text to file
        fun WriteFile(context: Activity?, value: String?, fileName: String?) {
            // add-write text into file
            try {

//			OutputStream stream = new FileOutputStream(extStorageDirectory
//					+ NewFolder + "/" + fileName);// "+.jpg"
                val fileout = context!!.openFileOutput("mytextfile.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(fileout)
                outputWriter.write(value)
                outputWriter.close()

                //display file saved message
                Toast.makeText(
                    context, "File saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Read text from file
        fun ReadFile(context: Activity?, fileName: String?) {
            //reading text from file
            try {
                val fileIn = context!!.openFileInput("$fileName.txt")
                val InputRead = InputStreamReader(fileIn)
                val inputBuffer = CharArray(READ_BLOCK_SIZE)
                var s: String? = ""
                var charRead: Int
                while (InputRead.read(inputBuffer).also { charRead = it } > 0) {
                    // char to string conversion
                    val readstring = String(inputBuffer, 0, charRead)
                    s += readstring
                }
                InputRead.close()
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getDividedWidth(activity: Activity?, divider: Int): Int {
            val displayMetrics = DisplayMetrics()
            activity!!.getWindowManager().defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            return displayMetrics.widthPixels / divider
        }

        fun getUriRealPathAboveKitkat(ctx: Context?, uri: Uri?): String? {
            var ret: String? = ""
            if (ctx != null && uri != null) {
                if (isContentUri(uri)) {
                    ret = if (isGooglePhotoDoc(uri.authority)) {
                        uri.lastPathSegment
                    } else {
                        getImageRealPath(ctx.contentResolver, uri, null)
                    }
                } else if (isFileUri(uri)) {
                    ret = uri.path
                } else if (isDocumentUri(ctx, uri)) {

                    // Get uri related document id.
                    val documentId = DocumentsContract.getDocumentId(uri)

                    // Get uri authority.
                    val uriAuthority = uri.authority
                    if (isMediaDoc(uriAuthority)) {
                        val idArr: Array<String?> = documentId.split(":").toTypedArray()
                        if (idArr.size == 2) {
                            // First item is document type.
                            val docType = idArr[0]

                            // Second item is document real id.
                            val realDocId = idArr[1]

                            // Get content uri by document type.
                            var mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            if ("image" == docType) {
                                mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            } else if ("video" == docType) {
                                mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            } else if ("audio" == docType) {
                                mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }

                            // Get where clause with real document id.
                            val whereClause = MediaStore.Images.Media._ID + " = " + realDocId
                            ret = getImageRealPath(
                                ctx.contentResolver,
                                mediaContentUri,
                                whereClause
                            )
                        }
                    } else if (isDownloadDoc(uriAuthority)) {
                        // Build download uri.
                        val downloadUri = Uri.parse("content://downloads/public_downloads")

                        // Append download document id at uri end.
                        val downloadUriAppendId = ContentUris.withAppendedId(
                            downloadUri, java.lang.Long.valueOf(
                                documentId
                            )
                        )
                        ret = getImageRealPath(ctx.contentResolver, downloadUriAppendId, null)
                    } else if (isExternalStoreDoc(uriAuthority)) {
                        val idArr: Array<String?> = documentId.split(":").toTypedArray()
                        if (idArr.size == 2) {
                            val type = idArr[0]
                            val realDocId = idArr[1]
                            if ("primary".equals(type, ignoreCase = true)) {
                                ret = Environment.getExternalStorageDirectory().toString() + "/" + realDocId
                            }
                        }
                    }
                }
            }
            return ret
        }

        /* Check whether this uri represent a document or not. */
        fun isDocumentUri(ctx: Context?, uri: Uri?): Boolean {
            var ret = false
            if (ctx != null && uri != null) {
                ret = DocumentsContract.isDocumentUri(ctx, uri)
            }
            return ret
        }

        /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
        fun isContentUri(uri: Uri?): Boolean {
            var ret = false
            if (uri != null) {
                val uriSchema = uri.scheme
                if ("content".equals(uriSchema, ignoreCase = true)) {
                    ret = true
                }
            }
            return ret
        }

        fun isDownloadDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.providers.downloads.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Check whether this document is provided by MediaProvider. */
        fun isMediaDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.providers.media.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Check whether this document is provided by google photos. */
        fun isGooglePhotoDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.google.android.apps.photos.content" == uriAuthority) {
                ret = true
            }
            return ret
        }

        fun isFileUri(uri: Uri?): Boolean {
            var ret = false
            if (uri != null) {
                val uriSchema = uri.scheme
                if ("file".equals(uriSchema, ignoreCase = true)) {
                    ret = true
                }
            }
            return ret
        }

        /* Check whether this document is provided by ExternalStorageProvider. */
        fun isExternalStoreDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.externalstorage.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }


        fun validateIfWebResponse(response: Response<WebResponse<Any?>?>, act: Activity?): Boolean {
            if (response.body() == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl(WebServiceConstants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                val errorConverter: Converter<ResponseBody, Any>? = retrofit.responseBodyConverter<Any>(
                    WebResponse::class.java, arrayOfNulls(
                        0
                    )
                )
                var error: WebResponse<Any?>? = null
                try {
                    error = errorConverter?.convert(response.errorBody()) as WebResponse<Any?>?
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (response.code() == PARAMS_TOKEN_EXPIRE) {
                    if(MainActivity.not_detail != null && error?.message != null)
                    MainActivity.not_detail?.toast(error?.message!!,0)
//                    UIHelper.showToast(activity, "TOKEN ERROR " + PARAMS_TOKEN_EXPIRE);
                    // tokenRefresh();
                } else if (response.code() == PARAMS_TOKEN_BLACKLIST) {
                    if(MainActivity.not_detail != null && error?.message != null)
                    MainActivity.not_detail?.toast(error?.message!!,0)

                    // showAlertDialog(activity, "Token Authentication Failed, Kindly login again")
                    //  SharedPreferenceManager.getInstance(activity).clearDB()
                    //  clearAllActivitiesExceptThis(MainActivity::class.java)

                } else if (response.code() == PARAMS_BADREQUEST) {
                     if(MainActivity.not_detail != null && error?.message != null)
                      MainActivity.not_detail?.toast(error?.message!!,0)

                   // showcustomsnackbar(act,response.message())

                } else if (response.code() == SUCCESSRESULT) {

                     return true
                    // errorToastForObject(error, true)
                } else {

                }


            }else{
                if (response.body() != null)
                   return true
            }
            return false
        }









        fun getImageRealPath(contentResolver: ContentResolver?, uri: Uri?, whereClause: String?): String? {
            var ret = ""

            // Query the uri with condition.
            val cursor = contentResolver!!.query(uri!!, null, whereClause, null, null)
            if (cursor != null) {
                val moveToFirst = cursor.moveToFirst()
                if (moveToFirst) {

                    // Get columns name by uri type.
                    var columnName = MediaStore.Images.Media.DATA
                    if (uri === MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Images.Media.DATA
                    } else if (uri === MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Audio.Media.DATA
                    } else if (uri === MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Video.Media.DATA
                    }

                    // Get column index.
                    val imageColumnIndex = cursor.getColumnIndex(columnName)

                    // Get column value which is the uri related file local path.
                    ret = cursor.getString(imageColumnIndex)
                }
            }
            return ret
        }
    }




}