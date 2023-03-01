//package com.appsnado.haippNew.Monitorapp
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.AsyncTask
//import android.os.Bundle
//import android.view.ContextMenu
//import android.view.ContextMenu.ContextMenuInfo
//import android.view.LayoutInflater
//import android.view.View
//import android.view.View.OnCreateContextMenuListener
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.appsnado.haippNew.Monitorapp.data.AppItem
//import com.appsnado.haippNew.Monitorapp.data.DataManager
//import com.appsnado.haippNew.Monitorapp.util.AppUtil
//import com.appsnado.haippNew.Monitorapp.util.PreferenceManager
//import com.appsnado.haippNew.R
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.facebook.FacebookSdk.getApplicationContext
//import java.lang.String
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//
//class MonitorAPP : Fragment() {
//    private var mList: RecyclerView? = null
//    private val mDialog: AlertDialog? = null
//    private val mSwipe: SwipeRefreshLayout? = null
//    private var mSortName: TextView? = null
//    private val mDay = 0
//    private var mPackageManager: PackageManager? = null
//    companion object {
//        var mTotal: Long = 0
//       /// private var mAdapter: MyAdapter? = null
//        private var mPackageManager: PackageManager? = null
//        private var mData: List<AppItem>? = null
//
//        fun newInstance() = MonitorAPP()
//    }
//
//    private lateinit var viewModel: MonitorAPViewModel
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val  view = inflater.inflate(R.layout.monitor_a_p_fragment, container, false)
//
//        val intent = Intent(activity, AppService::class.java)
//        intent.putExtra(AppService.SERVICE_ACTION, AppService.SERVICE_ACTION_CHECK)
//        activity?.startService(intent)
//
//
//        // https://guides.codepath.com/android/Shared-Element-Activity-Transition
//
//
//        mPackageManager = activity?.getPackageManager()
//
//
//      ///  mAdapter = MyAdapter()
//
//        mList = view.findViewById<RecyclerView>(R.id.list)
//        mList!!.layoutManager = LinearLayoutManager(getApplicationContext())
//        val dividerItemDecoration =
//            DividerItemDecoration(mList!!.context, DividerItemDecoration.VERTICAL)
//
//        mList!!.addItemDecoration(dividerItemDecoration)
//       // mList!!.adapter = mAdapter
//
//
//
//        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
//            process()
//            //activity?.startService(Intent(this, AlarmService::class.java))
//        }
//
//        return view
//    }
//    private fun process() {
//        if (DataManager.getInstance().hasPermission(getApplicationContext())) {
//            mList?.setVisibility(View.INVISIBLE)
//            val sortInt: Int =
//                PreferenceManager.getInstance().getInt(PreferenceManager.PREF_LIST_SORT)
//           // mSortName.setText(getSortName(sortInt))
//                MyAsyncTask().execute(sortInt, 4)
//        }
//    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MonitorAPViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    internal class MyAsyncTask :
//        AsyncTask<Int?, Void?, List<AppItem>>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//        }
//
////        override  fun doInBackground(vararg integers: Int): List<AppItem> {
////
////        }
//
//        override fun onPostExecute(appItems: List<AppItem>) {
//           /// mList.setVisibility(View.VISIBLE)
//            mTotal = 0
//            for (item in appItems) {
//                if (item.mUsageTime <= 0) continue
//                mTotal += item.mUsageTime
//                item.mCanOpen = mPackageManager?.getLaunchIntentForPackage(item.mPackageName) != null
//            }
//
//           // mAdapter.updateData(appItems)
//        }
//
//        override fun doInBackground(vararg integers: Int?): List<AppItem> {
//
//            mData = DataManager.getInstance().getApps(getApplicationContext(), integers[0]!!, integers[1]!!)
//
//            return mData!!;
//        }
//
//
//    }
//
//    ///
//
//
////    internal class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
////        private var mData: List<AppItem>
////        fun updateData(data: List<AppItem>) {
////            mData = data
////            notifyDataSetChanged()
////        }
////
////        fun getItemInfoByPosition(position: Int): AppItem? {
////            return if (mData.size > position) {
////                mData[position]
////            } else null
////        }
////
////        override fun onCreateViewHolder(
////            parent: ViewGroup,
////            viewType: Int
////        ): MyViewHolder {
////            val itemView: View =
////                LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
////            return MyViewHolder(itemView)
////        }
////
////        override fun onBindViewHolder(
////            holder: MyViewHolder,
////            position: Int
////        ) {
////            val item = getItemInfoByPosition(position)
////            holder?.mName.text = item!!.mName
////            holder?.mUsage.setText(AppUtil.formatMilliSeconds(item.mUsageTime))
////            holder?.mTime!!.text = String.format(
////                Locale.getDefault(),
////                "%s · %d %s",
////                SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
////                    .format(Date(item.mEventTime)),
////                item.mCount,
////                getApplicationContext().getResources().getString(R.string.times_only)
////            )
////            holder.mDataUsage.text = String.format(
////                Locale.getDefault(),
////                "%s",
////                AppUtil.humanReadableByteCount(item.mMobile)
////            )
////            if (mTotal > 0) {
////                holder.mProgress.progress = (item.mUsageTime * 100 / mTotal) as Int
////            } else {
////                holder.mProgress.progress = 0
////            }
////            GlideApp.with(getApplicationContext())
////                .load(AppUtil.getPackageIcon(getApplicationContext(), item.mPackageName))
////                .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .transition(DrawableTransitionOptions().crossFade())
////                .into(holder.mIcon)
////            holder.setOnClickListener(item)
////        }
////
////        override fun getItemCount(): Int {
////            return mData.size
////        }
////
////        internal inner class MyViewHolder(itemView: View) :
////            RecyclerView.ViewHolder(itemView), OnCreateContextMenuListener {
////            private val mName: TextView
////            private val mUsage: TextView
////            private val mTime: TextView
////            private val mDataUsage: TextView
////            private val mIcon: ImageView
////            private val mProgress: ProgressBar
////
////            @SuppressLint("RestrictedApi")
////            fun setOnClickListener(item: AppItem?) {
////                itemView.setOnClickListener {
////
////                }
////            }
////
////            override fun onCreateContextMenu(
////                contextMenu: ContextMenu,
////                view: View,
////                contextMenuInfo: ContextMenuInfo
////            ) {
////                val position = adapterPosition
////                val item = getItemInfoByPosition(position)
////                contextMenu.setHeaderTitle(item!!.mName)
////
////
////            }
////
////            init {
////                mName = itemView.findViewById(R.id.app_name)
////                mUsage = itemView.findViewById(R.id.app_usage)
////                mTime = itemView.findViewById(R.id.app_time)
////                mDataUsage = itemView.findViewById(R.id.app_data_usage)
////                mIcon = itemView.findViewById(R.id.app_image)
////                mProgress = itemView.findViewById(R.id.progressBar)
////                itemView.setOnCreateContextMenuListener(this)
////            }
////        }
////
////        init {
////            mData = ArrayList()
////        }
////    }
//}