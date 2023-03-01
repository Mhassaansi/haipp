package com.appsnado.haippNew.Helper

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.appsnado.haippNew.MainActivity
import java.io.File

class OSHelper {
    @TargetApi(11)
    private fun disableHardwareAcceleration(app: Application?, webView: WebView?) {
        if (!webView!!.isHardwareAccelerated()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Log.e("HardwareAcceleration", "disable HardwareAcceleration")
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    companion object {
        fun isExerternalStorageAvailable(): Boolean {
            return if (Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)) true else false
        }

        fun isInternetAvailable(context: Context?): Boolean {
            if (context == null) return true
            val conn = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = conn.activeNetworkInfo
            return if (activeNetworkInfo != null
                    && activeNetworkInfo.isConnectedOrConnecting) {
                true
            } else false
        }

        fun deleteAppCache(context: Context?) {
            if (context == null) return
            deleteFiles(context.cacheDir)
        }

        /**
         * Deletes Directories after removing all files recursively
         *
         * @param dir Directory to clear
         */
        fun deleteFiles(dir: File?) {
            val files = dir!!.listFiles()
            for (file in files) {
                if (file.isDirectory) deleteFiles(dir) else file.delete()
            }
            dir!!.delete()
        }

        fun placeCall(fragment: Fragment?, phoneNumber: String?) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            // tel://
            callIntent.data = Uri.parse("tel:$phoneNumber")
            fragment!!.startActivity(callIntent)
        }

        fun resolvedMediaPath(mURI: Uri?, fragment: FragmentActivity?): String? {
            val projection = arrayOf<String?>(MediaStore.Images.Media.DATA)
            val cursor = fragment!!.managedQuery(mURI, projection, null, null,
                    null)
            val column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index_data)
        }

        fun getFileUri(mURI: Uri?, fragment: FragmentActivity?): String? {
            val projection = arrayOf<String?>(MediaStore.Images.Media.DATA)
            val cursor = fragment!!.managedQuery(mURI, projection, null, null,
                    null)
            val column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val capturedImageFilePath = cursor.getString(column_index_data)
            return "file://$capturedImageFilePath"
        }

        /**
         * Enables strict mode. This should only be called when debugging the
         * application and is useful for finding some potential bugs or best
         * practice violations.
         */
        @TargetApi(11)
        fun enableStrictMode() {
            // Strict mode is only available img_toggle_on gingerbread or later
            if (hasGingerbread()) {

                // Enable all thread strict mode policies
                val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
                        .detectAll().penaltyLog()

                // Enable all VM strict mode policies
                val vmPolicyBuilder = VmPolicy.Builder()
                        .detectAll().penaltyLog()

                // Honeycomb introduced some additional strict mode features
                if (hasHoneycomb()) {
                    // Flash screen when thread policy is violated
                    threadPolicyBuilder.penaltyFlashScreen()
                    // For each activity class, set an instance limit of 1. Any more
                    // instances and
                    // there could be a memory leak.
                    vmPolicyBuilder.setClassInstanceLimit(MainActivity::class.java, 1)
                            .setClassInstanceLimit(MainActivity::class.java, 1)
                }

                // Use builders to enable strict mode policies
                StrictMode.setThreadPolicy(threadPolicyBuilder.build())
                StrictMode.setVmPolicy(vmPolicyBuilder.build())
            }
        }

        /**
         * Uses static final constants to detect if the device's platform version is
         * Gingerbread or later.
         */
        fun hasGingerbread(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
        }

        /**
         * Uses static final constants to detect if the device's platform version is
         * Honeycomb or later.
         */
        fun hasHoneycomb(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        }

        /**
         * Uses static final constants to detect if the device's platform version is
         * Honeycomb MR1 or later.
         */
        fun hasHoneycombMR1(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
        }

        /**
         * Uses static final constants to detect if the device's platform version is
         * Honeycomb MR1 or later.
         */
        fun hasHoneycombMR2(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
        }

        /**
         * Uses static final constants to detect if the device's platform version is
         * ICS or later.
         */
        fun hasICS(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
        }

//        var jsonObjectShipFrom: JsonObject = JsonObject()
//        jsonObjectShipFrom.addProperty("state", "Texas")
//        jsonObjectShipFrom.addProperty("city", "Houston")
//        jsonObjectShipFrom.addProperty("country", "USA")
//        //   jsonObjectShipFrom.put("contact_name", "[DHL] Contact name")
//        //  jsonObjectShipFrom.put("company_name", "[DHL] Testing Company")
//        //   jsonObjectShipFrom.put("street1", "Testing Street")
//
//        // jsonObjectShipFrom.put("phone", "+85212345678")
//        // jsonObjectShipFrom.put("email", "dhl@test.com")
//        // jsonObjectShipFrom.put("type", "business")
//        // jsonObjectShipFrom.put("postal_code", "")
//        // jsonObjectShipFrom.put("company_name", "")
//        // jsonObjectShipFrom.put("street2", "")
//        // jsonObjectShipFrom.put("tax_id", "")
//        // jsonObjectShipFrom.put("street3", "")
//        // jsonObjectShipFrom.put("fax", "")
//
//
//
//        var jsonObjectShipTo: JsonObject = JsonObject()
//        jsonObjectShipTo.addProperty("state", "Indiana")
//        jsonObjectShipTo.addProperty("city", "Yorktown")
//        jsonObjectShipTo.addProperty("country", "USA")
//        //  jsonObjectShipTo.put("contact_name", "Dr. Moises Corwin")
//        // jsonObjectShipTo.put("street1", "28292 Daugherty Orchard")
//
//
//        // jsonObjectShipTo.put("postal_code", "90209")
//
//        //  jsonObjectShipTo.put("phone", "1-140-225-6410")
//        //  jsonObjectShipTo.put("email", "Giovanna42@yahoo.com")
//        //   jsonObjectShipTo.put("type", "residential")
//
//        var jsonObjectItemPrice: JsonObject = JsonObject()
//        jsonObjectItemPrice.addProperty("amount", 22222)
//        jsonObjectItemPrice.addProperty("currency", "USD")
//
//
//        var jsonObjectItemWeight: JsonObject = JsonObject()
//        jsonObjectItemWeight.addProperty("value", 2)
//        jsonObjectItemWeight.addProperty("unit", "kg")
//
//
//        var jsonObjectDimension: JsonObject = JsonObject()
//        jsonObjectDimension.addProperty("width", 120)
//        jsonObjectDimension.addProperty("depth", 100)
//        jsonObjectDimension.addProperty("height", 105)
//        jsonObjectDimension.addProperty("unit", "cm")
//
//
////        var jsonObjectWeight: JSONObject = JSONObject()
////        jsonObjectWeight.put("value", 0.6)
////        jsonObjectWeight.put("unit", "kg")
//
//        var jsonObjectItem = JsonObject()
//
//        // jsonObjectItem.put("origin_country", "USA")
//
//        // jsonObjectItem.put("sku", "imac2014")
//        jsonObjectItem.add("price", jsonObjectItemPrice)
//        jsonObjectItem.addProperty("description", "Food Bar")
//        jsonObjectItem.addProperty("quantity", 1)
//        jsonObjectItem.add("weight", jsonObjectItemWeight)
//
//        val jsonArrayItem = JsonArray()
//        jsonArrayItem.add(jsonObjectItem)
//
//        var jsonObjectParcels: JsonObject = JsonObject()
//        jsonObjectParcels.add("items", jsonArrayItem)
//        jsonObjectParcels.addProperty("box_type", "custom")
//        jsonObjectParcels.add("dimension", jsonObjectDimension)
//        // jsonObjectParcels.put("description", "Food Bar")
//
//        //jsonObjectParcels.put("weight", jsonObjectWeight)
//
//
//
//        val jsonArrayParcel = JsonArray()
//        jsonArrayParcel.add(jsonObjectParcels)
//
//        var jsonObjectShipment = JsonObject()
//        jsonObjectShipment.add("parcels", jsonArrayParcel)
//        jsonObjectShipment.add("ship_from", jsonObjectShipFrom)
//        jsonObjectShipment.add("ship_to", jsonObjectShipTo)
//
//
//        val jsonObjectShipperAccount = JsonObject()
//        jsonObjectShipperAccount.addProperty("id", "0fe56e4f-2c61-4088-8fbf-e9614e4f6d0e")
//
//        val jsonArrayShipperAccount = JsonArray()
//        jsonArrayShipperAccount.add(jsonObjectShipperAccount)
//
//        var jsonObjectShipper = JsonObject()
//        //  jsonObjectShipper.put("async", "false")
//        jsonObjectShipper.add("shipment", jsonObjectShipment)
//        jsonObjectShipper.add("shipper_accounts", jsonArrayShipperAccount)
    }
}