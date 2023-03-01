package com.appsnado.haippNew.Comman_Pacakges.data

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.Helper.BitmapHelper
import com.appsnado.haippNew.Helper.FileHelper
import com.appsnado.haippNew.Helper.OSHelper
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.R
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraGalleryActionDialog(si: String, image : Boolean, video :Boolean) : DialogFragment(), View.OnClickListener {
    var mListener: CamGalleryActionListener? = null
    var CropIntent: Intent? = null
    private var btnClose: ImageButton? = null
    private var btnCamCorder: TextView? = null
    private var btnGalleryImg: TextView? = null
    private var btnGalleryvideo: TextView? = null
    private var mImageCaptureUri: Uri? = null
    private var fileImagePath: String? = null
    private var videostatus: String? = null
    private val imagePath: File? = null

    var camlayout :LinearLayout? = null
    var piclay :LinearLayout? = null
    var videolay :LinearLayout? = null

    private var Pic : Boolean? = image
    private var Vid : Boolean? = video


    //  private val Imagebolean : Boolean?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.getWindow()!!.setFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.DIM_AMOUNT_CHANGED)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
/*
        dialog!!.getWindow()!!.setBackgroundDrawable(
                ColorDrawable(Color.argb(95, 0, 0, 0)))*/
//		 setStyle( STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog);
//		getActivity().setTheme(
//				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen );
        val view = inflater.inflate(R.layout.dialog_camera_gallery_option, null)
        btnCamCorder = view.findViewById<View?>(R.id.btnCamCoder) as TextView
        btnGalleryImg = view.findViewById<View?>(R.id.btnGalleryImg) as TextView
        btnClose = view.findViewById<View?>(R.id.btnClose) as ImageButton
        btnGalleryvideo = view.findViewById<View?>(R.id.btnGalleryvideo) as TextView

        camlayout = view.findViewById<View?>(R.id.cameralay) as LinearLayout
        piclay = view.findViewById<View?>(R.id.imagelay) as LinearLayout
        videolay = view.findViewById<View?>(R.id.videolay) as LinearLayout


        btnCamCorder!!.setOnClickListener(this)
        btnGalleryImg!!.setOnClickListener(this)
        btnClose!!.setOnClickListener(this)
        btnGalleryvideo!!.setOnClickListener(this)

        camlayout!!.visibility = View.GONE
        piclay!!.visibility = View.GONE
        videolay!!.visibility =View.GONE

        if(Pic!!){
            camlayout!!.visibility =View.VISIBLE
            piclay!!.visibility =View.VISIBLE
        }
        if(Vid!!){
            videolay!!.visibility =View.VISIBLE
        }


        return view
    }

    fun setOnCamGalleryActionListener(listener: CamGalleryActionListener?) {
        mListener = listener
    }

    override fun onClick(v: View?) {
        when (v!!.getId()) {
            R.id.btnCamCoder -> if (requestAppPermissions()) {
                try {
                    setCameraIntent()
                } catch (e: Exception) {
                    e.cause
                }
            }
            R.id.btnGalleryImg -> try {
                if (requestAppPermissions()) {
                    browsePic()
                }
            } catch (e: Exception) {
                e.cause
            }
            R.id.btnClose -> {
                mListener!!.closeClick()
                dismiss()
            }
            R.id.btnGalleryvideo -> try {
                if (requestAppPermissions()) {
                    callvideo()
                }
            } catch (e: Exception) {
                e.cause
            }

        }
    }

    private fun callvideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            intent.type = "video/*"
            startActivityForResult(intent, SELECT_VIDEO)
        } else {
            UIHelper.Companion.showToastLong("No app found")
        }
    }

    private fun requestAppPermissions(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true
        }
        if (hasCameraPermission() && hasReadPermissions() && hasWritePermissions()) {
            return true
        }
        ActivityCompat.requestPermissions(requireActivity(), arrayOf<String?>(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), RC_CAMERA_STORAGE_PERM)
        return false
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String?>(Manifest.permission.CAMERA), REQUEST_CAMERA)

//            Snackbar.make(getView(), "R.string.permission_camera_rationale", Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
//                        }
//                    })
//                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String?>(Manifest.permission.CAMERA), REQUEST_CAMERA)
        }
        dismiss()
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA) {

            // Received permission result for camera permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed

//                Snackbar.make(mLayout, R.string.permision_available_camera,Snackbar.LENGTH_SHORT).show();
            } else {

//                Snackbar.make(mLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    override fun onDetach() {
        if (activity != null) {
            requireActivity().setTheme(R.style.AppTheme)
        }
        super.onDetach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (activity != null) {
            requireActivity().setTheme(R.style.AppTheme)
        }
        super.onDismiss(dialog!!)
    }

    private fun setCameraIntent() {
        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = getOutputPhotoFile()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mImageCaptureUri = getOutputMediaFileUri(FileColumns.MEDIA_TYPE_IMAGE)
            } else {
                mImageCaptureUri = Uri.fromFile(file)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun browsePic() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {


                //  startActivityForResult(getPickImageIntent(), PICK_FROM_FILE)

                intent.type = "image/*"
                startActivityForResult(intent, PICK_FROM_FILE)

                // select a file

                // select a file


            } else {
                UIHelper.Companion.showToastLong("No app found")
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

//    private fun getPickImageIntent(): Intent? {

//        var chooserIntent: Intent? = null
//
//
//        var intentList: MutableList<Intent> = ArrayList()
//
//
//        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//
//        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())
//
//
//        intentList = addIntentsToList(this, intentList, pickIntent)
//
//        intentList = addIntentsToList(this, intentList, takePhotoIntent)
//
//        if (intentList.size > 0) {
//
//            chooserIntent = Intent.createChooser(
//
//                    intentList.removeAt(intentList.size - 1),
//
//                    PICK_FROM_FILE
//
//            )
//            chooserIntent!!.putExtra(
//
//                    Intent.EXTRA_INITIAL_INTENTS,
//
//                    intentList.toTypedArray<Parcelable>()
//
//            )
//
//        }
//
//
//
//        return chooserIntent

    // }








    private fun getOutputPhotoFile(): File? {
        val directory = FileHelper(activity).getAvailableCache()
        val timeStamp = SimpleDateFormat("yyyMMdd_HHmmss", Locale.UK)
                .format(Date())
        return File(directory!!.path + File.separator + "IMG_"
                + timeStamp + ".png")
    }

    private fun startCropImageActivity(imageUri: Uri?) {
        CropImage.activity(imageUri)
                .start(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            when (requestCode) {
                PICK_FROM_FILE -> if (mListener != null) mListener!!.browsePic("", null)
                PICK_FROM_CAMERA -> if (mListener != null) mListener!!.setCameraOpen(null)
            }
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        when (requestCode) {
            PICK_FROM_FILE -> try {
                mImageCaptureUri = data!!.getData()
                fileImagePath = OSHelper.Companion.resolvedMediaPath(mImageCaptureUri, activity)
                if (data.getData() == null || fileImagePath == null || fileImagePath!!.startsWith("http")) {
                    mListener!!.browsePic(null, null)
                    dismiss()
                    return
                }
                BitmapRotationAsyncTask(mImageCaptureUri, false).execute("RUN")
                dismiss()
            } catch (e: Exception) {
                dismiss()
            }
            SELECT_VIDEO -> {
                mImageCaptureUri = data!!.getData()
                if (mImageCaptureUri != null) {
                    val videoFile = File(mImageCaptureUri.toString())
                    mListener!!.browsevideo(mImageCaptureUri.toString() + "", videoFile, "video")
                }
                dismiss()
            }
            PICK_FROM_CAMERA -> {
                if (mImageCaptureUri == null) {
                    try {
                        mListener!!.setCameraOpen(null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    dismiss()
                    return
                }
                val imageFile = File(mImageCaptureUri!!.getPath())
                if (imageFile.exists()) {
                    BitmapRotationAsyncTask(mImageCaptureUri, true).execute("RUN")
                }
                dismiss()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Create a file Uri for saving an image or video
     */
    fun getOutputMediaFileUri(type: Int): Uri? {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    interface CamGalleryActionListener {
        open fun setCameraOpen(filPath: File?)
        open fun browsePic(strFilePath: String?, file: File?)
        open fun browsevideo(strFilePath: String?, file: File?, type: String?)
        open fun closeClick()
    }

    private inner class BitmapRotationAsyncTask(private val mUriBitmap: Uri?, private val isFromCamera: Boolean) : AsyncTask<String?, Void?, File?>() {
        private var progressDialog: ProgressDialog? = null
        fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source!!, 0, 0, source.getWidth(), source.getHeight(), matrix, true)
        }

        override fun doInBackground(vararg params: String?): File? {
            var imageCPath: File?
            return try {
                // imagePath = null;
                val origPath = if (isFromCamera) mUriBitmap!!.getPath() else "" + OSHelper.Companion.resolvedMediaPath(mImageCaptureUri, activity)
                imageCPath = File(origPath)
                val o2 = BitmapFactory.Options()
                //                o2.inSampleSize = 2;
                var bmp = BitmapFactory.decodeFile(origPath)
                val maxSize = 1920
                val outWidth: Int
                val outHeight: Int
                val inWidth = bmp.width
                val inHeight = bmp.height
                if (inWidth > inHeight) {
                    outWidth = maxSize
                    outHeight = inHeight * maxSize / inWidth
                } else {
                    outHeight = maxSize
                    outWidth = inWidth * maxSize / inHeight
                }
                var resizedBitmap = Bitmap.createScaledBitmap(bmp, outWidth, outHeight, false)
                val ei = ExifInterface(origPath!!)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED)
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> resizedBitmap = rotateImage(resizedBitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> resizedBitmap = rotateImage(resizedBitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> resizedBitmap = rotateImage(resizedBitmap, 270f)
                    ExifInterface.ORIENTATION_NORMAL -> {
                    }
                    else -> {
                    }
                }
                val path = BitmapHelper.replace(activity, resizedBitmap, imageCPath.absolutePath)
                bmp.recycle()
                bmp = null
                imageCPath = File(path)
                imageCPath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: File?) {
            progressDialog!!.dismiss()
            if (isFromCamera) {
                if (mListener != null && result != null) {
                    mListener!!.setCameraOpen(result)
                }
            } else {
                // mListener.setCameraOpen( result );
                try {
                    if (result != null) {
                        mListener!!.browsePic(result.path + "", result)
                    } else {
                        mListener!!.browsePic(null, null)
                    }
                } catch (e: Exception) {
                }
            }

            //dismiss();
        }

        override fun onPreExecute() {
            try {
                progressDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ProgressDialog(ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light_Dialog))
                } else {
                    ProgressDialog(activity)
                }
                progressDialog!!.setMessage("Processing...")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onProgressUpdate(vararg values: Void?) {}

    }

    companion object {
        private const val REQUEST_CAMERA = 60005
        private const val PICK_FROM_CAMERA = 60001
        private const val PICK_FROM_FILE = 60002
        private const val RC_CAMERA_STORAGE_PERM = 123
        private const val SELECT_VIDEO = 3

        /**
         * Create a File for saving an image or video
         */
        private fun getOutputMediaFile(type: Int): File? {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp")
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile: File
            mediaFile = if (type == FileColumns.MEDIA_TYPE_IMAGE) {
                File(mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".jpg")
            } else if (type == FileColumns.MEDIA_TYPE_VIDEO) {
                File(mediaStorageDir.path + File.separator +
                        "VID_" + timeStamp + ".mp4")
            } else {
                return null
            }
            return mediaFile
        }
    }
}