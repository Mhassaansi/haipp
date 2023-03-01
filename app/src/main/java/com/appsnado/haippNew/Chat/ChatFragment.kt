package com.appsnado.haippNew.Chat

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.appsnado.haippNew.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.redview
import com.appsnado.haippNew.Comman_Pacakges.data.DbContract
import com.appsnado.haippNew.Helper.UIHelper
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Chat
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener

import java.util.*

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_task_detail.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp





class ChatFragment : BaseFragment<ChatViewModel>(), View.OnClickListener {
    var binding: Chat? = null
    var chatAdapter: ChatParentAdapter? = null
    var chatMessageAdapter: ChatMessageAdapter? = null

    //chat
    var storage: FirebaseStorage? = null
    private var  registration: DocumentReference? = null
    private var database2: FirebaseFirestore? = null

    var storageReference: StorageReference? = null
    var greaterid: String? = null
    var smallerid: String? = null
    var deviceid: String? = ""
    var devicetitle: String? = ""
    var messagetype: String? = ""
    private var messageAdapter: Chatsadapter? = null
    private var callstate: Boolean = false
    var documentsnap: DocumentSnapshot? = null
    var listenerRegistration : ListenerRegistration? = null

        private var id_model: String? = null
    private var id_username: String? = null
    private var user_img: String? = null
    private var user_type: String? = null
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private var vss: Boolean = false
    var id: String? = null
    var usertypedata: String? = null
    var keycreate = false
    private var chat: Mess? = null
    var checkid = ""

    // TODO need to change serverkey
    val serverKey = "key=" + "AAAAsNXgU-M:APA91bFH4bFiNtth4YihTsDml0CjCwb7rgElelhKzgM-1ZEeug6NE2lrMyO4IEu0qQyT9XP6xaO8_g6h78sGnlAhRxgQ6EyYIdIcNIM5PRsccghmjHGZQAUcXaJml3hmU9ZJ5kUjXvWJ"
    private val contentType = "application/json"
    private var userId: String? = null
    var mSendEventListner : ValueEventListener? = null
    private var database: FirebaseFirestore? = null

    @ServerTimestamp
    var serverTime: Date? = null

    companion object {
        @kotlin.jvm.JvmField
        var not_detail: ChatFragment? = null
        fun newInstance() = ChatFragment()
        private val mArrayList = java.util.ArrayList<FirebasetimeClass?>()
        private val mArrayListtest = java.util.ArrayList<Mess?>()

    }

    private lateinit var viewModelfeature: ChatViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_chat,
                container,
                false
        )
        startTopToBottomAnimation()






      //    var timestamp = com.google.firebase.firestore.FieldValue.serverTimestamp();
      //    Log.i("TAG", "onCreateView: "+timestamp)
      //    val timestamp: Timestamp = snapshot.get("created_at") as Timestamp
      //     val now: FieldValue = com.google.firebase.firestore.FieldValue.serverTimestamp();
      //     val dateFormatter = SimpleDateFormat("E, y-M-d 'at' h:m:s a z")
      //     Log.i("Format 1:   ", dateFormatter.format(now))
      //     var timestamp = com.google.firebase.firestore.FieldValue.serverTimestamp();




        callstate = true
        database = FirebaseFirestore.getInstance()
        database2 = FirebaseFirestore.getInstance()
        userId = mainActivity!!.preferenceManager?.getUser()!!.userID


//        if(mainActivity!!.preferenceManager?.gettype() != "parent"){
//
//        }else{
//
//        }









         deviceid = mainActivity?.preferenceManager!!.getdevicesid()
         devicetitle = mainActivity?.preferenceManager!!.getdevicestitle()

         not_detail = this




        UIHelper.showDilog(mainActivity)
        val handler = Handler()
        handler.postDelayed(
            Runnable {
                setData(deviceid,devicetitle)
                UIHelper.hidedailog()
            },
            7000
        )



         binding!!.btnSendview?.setOnClickListener {
            try {
                   val message = binding!!.inputtext.text.toString()
                    if (message.length > 0) {
                        sendMessage(view, message, "")

                    }
                } catch (e: Exception) {
                    e.stackTrace
                }
          }

         return binding!!.root;

    }



    fun sendMessage(view: View?, MESS: String?, image: String?) {
        //if (message.length > 0) {
        val tsLong = System.currentTimeMillis()
        val Number = tsLong.toString()



        if (keycreate == false) {
            keycreate = true
            id = database!!.collection("Chats").document().id
            if (id == null)
                database!!.collection("Chats").document()
                 feildsadd()
                  calling_array(id!!)
                 creatthrea(Number)
        }


        chat = Mess()
        chat!!.setContent(MESS)
        chat!!.setfirebasetime(FieldValue.serverTimestamp())

        if(mainActivity!!.preferenceManager?.gettype() != "parent"){
            chat!!.setSenderID(deviceid)
            chat!!.setotheruserid(id_model)
            chat!!.setSenderName(devicetitle)
            chat!!.setisSeen(false)
            chat!!.setothername(mainActivity!!.preferenceManager!!.getUser()!!.userEmail)


        }else{
            chat!!.setSenderID(id_model)
            chat!!.setotheruserid(deviceid)
            chat!!.setisSeen(false)
            chat!!.setSenderName(mainActivity!!.preferenceManager!!.getUser()!!.userEmail)
            chat!!.setothername(devicetitle)

        }
        chat!!.setusertype(mainActivity!!.preferenceManager!!.gettype())
        chat!!.setId(userId)

        chat!!.setCretaednumber(Number.toLong().toString())


//        val map: MutableMap<String, Any> = HashMap()
//        map["date"] = FieldValue.serverTimestamp()


        database!!.collection("Chats").document(id!!).collection("thread").add(chat!!)

 //       database.child(user.getUid()).child("Date").setValue(ServerValue.TIMESTAMP);




        binding!!.inputtext.text!!.clear()

       // messageAdapter!!.add(chat)
        binding!!.listdata!!.setSelection(messageAdapter!!.getCount() - 1);


        val handler = Handler()
        handler.postDelayed(
            Runnable {
                feildsupdate()
            },
            1000
        )





        //notificationdata(MESS)

        //}
    }
    private fun creatthrea(time: String) {
        var t :Double = 0.0
        t = userId!!.toDouble()
        var ide = Integer.valueOf(t!!.toInt())

        var  messanger = Messangermodel()
        messanger!!.setcreatedAt(time.toLong())
        messanger!!.setLoginUserId(ide.toString())
        messanger!!.setLoginUserImage("")
        messanger!!.setLoginUserName("")
        messanger!!.setLoginUserType(mainActivity!!.preferenceManager!!.gettype())
        messanger!!.setOtherUserId(ide.toString())
        messanger!!.setOtherUserImage(user_img)
        messanger!!.setOtherUserName(id_username)
        messanger!!.setdeviceid(deviceid)
        database!!.collection("Threads").document(id!!).set(messanger!!)
    }
    private fun feildsadd() {

        //database.collection("Chats").document();
        val mStringList = java.util.ArrayList<String?>()
        mStringList.add(smallerid)
        mStringList.add(greaterid)
        val docData: MutableMap<String, Any> = HashMap()
        docData["users"] = mStringList
        database!!.collection("Chats").document(id!!).set(docData)
        // ...
// future.get() blocks on response
        println("Update time : $docData")
        // Gettingdata();
    }

    private fun feildsupdate() {

        val tsLong = System.currentTimeMillis()
        val currentDateandTime = tsLong.toString()


        keycreate = true
        val mStringList = java.util.ArrayList<String?>()
        mStringList.add(smallerid)
        mStringList.add(greaterid)
        val docData: MutableMap<String, Any> = HashMap()
        docData["users"] = mStringList
        database!!.collection("Chats").document(id!!).delete()
        database!!.collection("Chats").document(id!!).set(docData)

        database!!.collection("Threads").document(id!!).update("createdAt", tsLong)
        println("Update time : $docData")




    }

    private fun startTopToBottomAnimation() {
    }

    fun setData(device_id: String?, devicetitleuser: String?) {

        UIHelper.showprogress(mainActivity)


        deviceid = device_id
        devicetitle = devicetitleuser

        storage = FirebaseStorage.getInstance();



        usertypedata = mainActivity?.preferenceManager!!.gettype()
        id_model = mainActivity?.preferenceManager!!.getUser()!!.userID
        id_username = mainActivity?.preferenceManager!!.getUser()!!.userEmail

        ///  chattype = bundle!!.getString("type")
        if (!vss) {
            messagetype = "Message"

        } else {
            messagetype = "Message"
        }
        if (mArrayList != null)
            mArrayList.clear()


        messageAdapter = Chatsadapter(mainActivity, mArrayList, usertypedata)
        binding!!.listdata!!.adapter = messageAdapter


        val ui: Double = mainActivity!!.preferenceManager!!.getUser()!!.userID!!.toDouble()
        val i = ui!!.toInt()
        val vi: Double = deviceid!!.toDouble()
        val v = vi!!.toInt()


        var A: Int = i!!.toInt()
        var B: Int = v!!.toInt()

        if (A < B) {
            greaterid = B.toString()
            smallerid = A.toString()
            Log.d("TAG", "onCreateView: " + A)
        } else {
            greaterid = A.toString()
            smallerid = B.toString()
        }
        Gettingdataone()
        //sendMessage(view,content,postimages)



        if(mainActivity!!.preferenceManager?.gettype() != "parent"){
            binding!!.rvChatHeads.visibility =View.GONE
        }else{
//            chatArrayList = ArrayList()
//
//            chatArrayList!!.add("Task")
//            chatArrayList!!.add("Task")
//            chatArrayList!!.add("Task")
//            chatArrayList!!.add("Task")
//            chatArrayList!!.add("Task")
//            chatArrayList!!.add("Task")

            binding!!.rvChatHeads.layoutManager = LinearLayoutManager(
                    mainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            chatAdapter =
                ChatParentAdapter(mainActivity!!, DbContract.newsItems)
            binding!!.rvChatHeads.adapter = chatAdapter
        }





    }
    override fun onDestroyView() {
        callstate = false
        super.onDestroyView()
    }
    private fun Gettingdataone(): String? {
        val citiesRef = database!!.collection("Chats")
        val query = citiesRef.whereEqualTo("users", Arrays.asList(smallerid, greaterid))
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    id = document.id
                    keycreate = true
                    //set = true
                    id = document.id
                    calling_array(id!!)
                }

            }
        }
        Log.i(ContentValues.TAG, "Gettingdata: $query")
        UIHelper.hidedailog()
        return null
    }

    private fun calling_array(id: String) {

        registration = database!!.collection("Chats").document(id)
        listenerRegistration = registration?.addSnapshotListener(EventListener { documentSnapshot, e ->

            if (callstate) {
                if (e != null) {
                   // Log.d("ERROR", e.message)
                    UIHelper.hidedailog()
                    return@EventListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.i(ContentValues.TAG, "onEvent: " + documentSnapshot.data)
                    documentsnap = documentSnapshot
                    getdatafirestore(id)


                }
            }
        })


    }

    private fun getdatafirestore(id: String?) {
        database2!!.collection("Chats").document(id!!).collection("thread")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        mArrayList.clear()

                        val types = documentSnapshot.toObjects(FirebasetimeClass::class.java)
                        mArrayList.addAll(types)
                        UIHelper.hidedailog()
                        try {
                            mArrayList.sortWith(Timesorter())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Log.d(ContentValues.TAG, "onSuccess1: $mArrayList")
                        // Collections.reverse(mArrayList);
                        Log.d(ContentValues.TAG, "onSuccess: $mArrayList")
                      //  val type_array = documentSnapshot.toObjects(Mess::class.java)

                        val type_array = documentSnapshot.toObjects(FirebasetimeClass::class.java)
                        for (i in type_array!!.indices) {

                            var t = documentSnapshot.documents[i].id
                            val contactListener = database2!!.collection("Chats").document(id!!).collection("thread").document(t)
                            contactListener.addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Toast.makeText(mainActivity, "update", Toast.LENGTH_LONG).show()
                                   // Log.d("ERROR", e.message)
                                }
                            }
                            if(mainActivity!!.preferenceManager?.gettype() != "parent"){
                                checkid = mainActivity!!.preferenceManager!!.getdevicesid()!!
                            }else{
                                checkid = userId!!
                            }


                            Log.i("TAG", "getdatafirestore12: "+userId)
                            if (checkid != type_array[i]?.getSenderID()) {
                                if (type_array[i]?.getisSeen() == false) {
                                    var t = documentSnapshot.documents[i].id
                                    database2!!.collection("Chats").document(id!!).collection("thread").document(t).update("isSeen", true)
                                }
                            }


                        }


                        if(redview){
                            mainActivity!!.notification()
                        }
                        messageAdapter = Chatsadapter(mainActivity, mArrayList, usertypedata)
                        binding!!.listdata!!.adapter = messageAdapter
                        binding!!.listdata!!.setStackFromBottom(true);

                      }


                }.addOnFailureListener {
                UIHelper.hidedailog()
                    // Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                    //data="Failed";
                }

    }


    override fun createViewModel(): ChatViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelfeature = ViewModelProviders.of(this, factory).get(ChatViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.text("Chat")
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    fun callchild(deviceTitle: String?, devicesid: String?) {
         keycreate = false
        if (listenerRegistration != null) {
            listenerRegistration!!.remove();
        }
        setData(devicesid,deviceTitle)

    }


}