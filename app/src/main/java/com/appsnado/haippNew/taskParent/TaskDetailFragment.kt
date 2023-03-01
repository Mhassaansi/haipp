package com.appsnado.haippNew.taskParent

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.TaskParentDetail
import com.appsnado.haippNew.retro.WebResponse
import com.appsnado.haippNew.retro.WebServiceConstants
import com.appsnado.haippNew.retro.WebServiceConstants.userid
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import id.zelory.compressor.Compressor



class TaskDetailFragment : BaseFragment<TaskParentDetailViewModel>(), View.OnClickListener {
    var binding: TaskParentDetail? = null
    var taskAdapter: TaskParentDetailAdapter? = null
    var taskArrayList: ArrayList<String>? = null
    var taskDialog: SetTaskDialog? = null
    var cal_services : Boolean =false
    var add_services : Boolean =false
    private var fileTemporaryProfilePicture: File? = null
    var uri : Uri? = null
    var daycalander :String = ""
    var dayof :String = ""
    var imagedata: MultipartBody.Part? = null
    var taskid: String? = ""

    var parenttoken = ""
    companion object {
        const val MIME_TYPE_IMAGE = "image/*"
        const val MIME_TYPE_Vide = "video/*"
        var jsondatearray : JSONArray? = null
        var jsonkeydata : String? = null
        @kotlin.jvm.JvmField
        var not_detail: TaskDetailFragment? = null
        fun newInstance() = TaskDetailFragment()

    }

    private lateinit var viewModeltaskdetail: TaskParentDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.fragment_task_details,
            container,
            false
        )
        startTopToBottomAnimation()
        setData()
        not_detail = this
        return binding!!.root;

    }

    private fun calanserday(dayOfWeek: Int): String {
        when(dayOfWeek){
            1 ->{ daycalander ="Monday"
                return  daycalander
            }
            2 -> {daycalander = "Tuesday";
                return daycalander
            }
            3 -> {daycalander = "Wed"
                return daycalander
            };
            4 ->  {daycalander ="Thursday"
                return daycalander
            };
            5 -> {daycalander = "Fri"
                return daycalander
            };
            6 -> {daycalander = "Sat"
                return daycalander
            };
            7 -> {daycalander = "Sun"
                return daycalander
            };

        }
        return daycalander

    }
    private fun startTopToBottomAnimation() {
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mainActivity?.preferenceManager?.gettype() != "parent")
            binding!!.childLayout.visibility = View.VISIBLE
    }

    fun setData() {
        taskid  = arguments?.getString("task_id")
        parenttoken  = arguments?.getString("parenttoken")!!

        if(taskid != null) {
            viewModeltaskdetail!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            viewModeltaskdetail!!.userLiveData!!.observe(mainActivity!!, ReObserver())
            viewModeltaskdetail!!.loadDataNetwork(taskid!!)
            cal_services = true
        }


        binding!!.childLayout.setOnClickListener {
            dailog()
        }

//        taskArrayList = ArrayList()
//
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")
//        taskArrayList!!.add("Task Details")


    }

    private fun DataSend() {


        if(jsonkeydata == "Weekly") {
            if (uri != null) {
                imagedata = prepareFilePart2("image",uri!!)
            }

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val dayOfWeek = mcurrentTime[Calendar.DAY_OF_WEEK]
            for (n in 0 until jsondatearray!!.length()) {
                val `jsondate`: JSONObject = jsondatearray!!.getJSONObject(n)
                var datkey = jsondate.getInt("date")
                // var calnderday = calanserday(Calendar.DAY_OF_WEEK)
                // var taskday = callday(datkey)

                if(datkey == dayOfWeek){
                    val currentDate = SimpleDateFormat("yyyy-MM-dd")
                    val todayDate = Date()
                    val thisDate: String = currentDate.format(todayDate)

                    viewModeltaskdetail!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                    viewModeltaskdetail!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver("Task Submit"))
                    viewModeltaskdetail!!.submittask(mainActivity!!.preferenceManager?.getdevicesid(), taskid!!,thisDate,imagedata)
                    add_services = true
                    break
                }
            }
            mainActivity!!.toast("task not submitted",0)
        }else if(jsonkeydata == "Monthly"){
            val currentDate = SimpleDateFormat("dd/MM/yyyy")
            val todayDate = Date()
            val thisDate: String = currentDate.format(todayDate)

            //val partArrayList: ArrayList<Part> = ArrayList<Part>()

            if (uri != null) {
                imagedata = prepareFilePart2("image",uri!!)
            }



            for (n in 0 until jsondatearray!!.length()) {
                val `jsondate`: JSONObject = jsondatearray!!.getJSONObject(n)
                var datkey = jsondate.getString("date")
                if(datkey == thisDate){
                    val currentDate = SimpleDateFormat("yyyy-MM-dd")
                    val todayDate = Date()
                    val thisDate: String = currentDate.format(todayDate)

                    viewModeltaskdetail!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
                    viewModeltaskdetail!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver("Task Submit"))
                    viewModeltaskdetail!!.submittask(mainActivity!!.preferenceManager?.getdevicesid(), taskid!!,thisDate,imagedata)
                    add_services = true
                    break
                }
            }
            mainActivity!!.toast("task not submitted",0)


        }else{

            if (uri != null) {
                imagedata = prepareFilePart2("image",uri!!)
            }
            val currentDate = SimpleDateFormat("yyyy-MM-dd")
            val todayDate = Date()
            val thisDate: String = currentDate.format(todayDate)

            viewModeltaskdetail!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
            viewModeltaskdetail!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver("Task Submit"))
            viewModeltaskdetail!!.submittask(mainActivity!!.preferenceManager?.getdevicesid(), taskid!!,thisDate,imagedata)
            add_services = true
        }




    }

    private fun prepareFilePart2(partName: String, fileUri: Uri): MultipartBody.Part? {
    // use the FileUtils to get the actual file by uri
    //File file = new File(fileUri.getPath());
    try {
        val file: File
        val ss = fileUri.toString()
        val requestFile: RequestBody
        if (partName.startsWith("video")) {
            file = File(fileUri.toString())
            Log.d("file", "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists())
            requestFile = RequestBody.create(MIME_TYPE_Vide.toMediaTypeOrNull(), file)
        } else {
            file = FileUtil.from(mainActivity, fileUri)!!
            requestFile = RequestBody.create(MIME_TYPE_IMAGE.toMediaTypeOrNull(), file)
        }
        return MultipartBody.Part.createFormData("ts_image", file.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


    private fun callday(dayOfWeek: Int): String {
        when(dayOfWeek){
            1 ->{ dayof ="Sun"
                return  dayof
            }
            2 -> {dayof = "Monday";
                return dayof
            }
            3 -> {dayof = "Tuesday"
                return dayof
            };
            4 ->  {dayof ="Wed"
                return dayof
            };
            5 -> {dayof = "Thursday"
                return dayof
            };
            6 -> {dayof = "Fri"
                return dayof
            };
            7 -> {dayof = "Sat"
                return dayof
            };

        }
        return dayof

    }


    @Throws(IOException::class)
    private fun compressImage(imageUri: Uri): Uri {
        val compressedFile: File = Compressor(context)
            .setMaxWidth(500)
            .setMaxHeight(500)
            .setQuality(75)
            .compressToFile(File(imageUri.path))
        return Uri.fromFile(compressedFile)
    }


    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (isLoading) UIHelper.showprogress(mainActivity) else UIHelper.hidedailog()
        }
    }
    fun cropImagePicker(
        context: Context,
        fragment: Fragment
    ) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setFixAspectRatio(true)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setMinCropWindowSize(192, 192)
            .setMinCropResultSize(192, 192)
            .setMultiTouchEnabled(false)
            .setOutputCompressFormat(Bitmap.CompressFormat.JPEG) // FIXME: 15-Jul-17 Fix Quality if required
            .setRequestedSize(640, 640, CropImageView.RequestSizeOptions.RESIZE_FIT)
            .setOutputCompressQuality(100)
            .start(context, fragment)
    }


    fun dailog() {


        val dialog1 = Dialog(mainActivity!!)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_upload)

        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<MaterialButton>(R.id.btnAccept)
        val txtno = dialog1.findViewById<MaterialButton>(R.id.btnReject)

        txtYes.setOnClickListener {
            dialog1.dismiss()


           // if (RunTimePermissions.isAllPermissionGiven(context, mainActivity, true)) {
                cropImagePicker(mainActivity!!, this)
          //  }



        }

        txtno.setOnClickListener {
            dialog1.dismiss()
            DataSend()

        }

    }
    private inner class ReObserver : Observer<TaskdetailModel> {
        override fun onChanged(reponces: TaskdetailModel) {
            if(!cal_services) return
            if (reponces == null) return
                cal_services = false

                binding!!.des.text = reponces.task_description
                binding!!.freq.text = reponces.task_frequency
                binding!!.point.text = reponces.task_reward


            binding!!.rvTask.layoutManager = LinearLayoutManager(
                    mainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
            )

            taskAdapter = TaskParentDetailAdapter(mainActivity!!, reponces.task_submition)
            binding!!.rvTask.adapter = taskAdapter

        }
    }
    override fun createViewModel(): TaskParentDetailViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModeltaskdetail =
            ViewModelProviders.of(this, factory).get(TaskParentDetailViewModel::class.java)
        return viewModeltaskdetail
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Task Details")
    }



    override fun onClick(v: View?) {

    }

    fun changestatus(s: String, tskid: String?) {

        viewModeltaskdetail!!.loadingStatus!!.observe(mainActivity!!, LoadingObserver())
        viewModeltaskdetail!!.ReuserLiveData!!.observe(mainActivity!!, AddObserver("Change Task Status"))
        viewModeltaskdetail!!.statuschange(s, tskid!!)
        add_services = true

    }

    fun NotificationSendTask(ischeck: String, appnname: String,json: String) {
        var texts = ""
        var tokenlis = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {


                if(appnname == "1"){
                    texts = mainActivity!!.preferenceManager?.getdevicestitle()+" has completed the task"
                    mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getUser()!!.userID,"Task",texts,"user")
                    tokenlis = ischeck
                }else{
                    texts = "Parent has approve the task"
                    tokenlis = DbContract.childtoken
                    mainActivity?.sendapi(mainActivity!!.preferenceManager!!.getdevicesid(),"Task",texts,"device")
                }

                dataBody.put(appnname, json)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", tokenlis)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)



        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private inner class AddObserver(var s: String) : Observer<WebResponse<Any?>?> {
        override fun onChanged(reponces: WebResponse<Any?>?) {
            if(!add_services) return
            if (reponces == null) return
            add_services = false
            mainActivity?.toast(reponces.message!!,1)
           // NotificationSend("true","taskstatus",s)
            if(s == "Task Submit"){
                NotificationSendTask(parenttoken,"1",reponces.message!!)
            }else if(s == "Change Task Status"){
                NotificationSendTask("","0",reponces.message!!)
            }

            setData()

        }
    }


    fun dailogimage(con: Context, imageUrl: String?) {
        val dialog = Dialog(con, R.style.CustomDialog)
        if (con.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK ==
            Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            dialog.setContentView(R.layout.dialog_xlarge)
        } else dialog.setContentView(R.layout.dialog_xlarge)
        val im = dialog.findViewById<View>(R.id.imageView) as ImageView
        Glide.with(im.context).load("https://server.appsstaging.com/1461/haipp/public"+imageUrl).placeholder(R.drawable.user).into(im)
        im.setOnClickListener { dialog.dismiss() }
        dialog.show()
        val window = dialog.window
        window!!.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
    }


    fun NotificationSend(ischeck: String, appnname: String,s: String) {
        var texts = ""
        val notification = JSONObject()
        val dataBody = JSONObject()
        val notificationBody = JSONObject()
        try {

            if(DbContract.childtoken != "") {
                texts = s

                dataBody.put(appnname, s)
                dataBody.put("ischeck", ischeck)
                dataBody.put("bodymsg", texts)
                notificationBody.put("title", "Haipp")
                notificationBody.put("body", "Haipp")
                notificationBody.put("click_action", "Haipp")
                notificationBody.put("notification_type", "Haipp")
                notification.put("to", DbContract.childtoken)
                notification.put("priority", "high")
                notification.put("data", dataBody)
                notification.put("notification", notificationBody)
                mainActivity?.sendNotification(notification)

            }else{
                Toast.makeText(mainActivity,"Please again select devices", Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun calldata(urid: Uri) {
        uri = compressImage(urid)//File(result.uri.path)
        DataSend()
    }

    fun imagedailog(toString: String) {
        dailogimage(mainActivity!!,toString)
    }
}