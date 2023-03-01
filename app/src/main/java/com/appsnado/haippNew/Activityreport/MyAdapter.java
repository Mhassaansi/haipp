package com.appsnado.haippNew.Activityreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.appsnado.haippNew.Activities.MainActivity;
import com.appsnado.haippNew.Monitorapp.data.AppItem;
import com.appsnado.haippNew.Monitorapp.util.AppUtil;
import com.appsnado.haippNew.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static com.appsnado.haippNew.Applocakpacakges.base.AppConstants.mTotal;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<AppItem> mData;
    private Context mcontext;

    public MyAdapter(MainActivity mainActivity, ArrayList<AppItem> items) {
        super();
        mData = items;
        mcontext = mainActivity;
    }

    void updateData(List<AppItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    AppItem getItemInfoByPosition(int position) {
        if (mData.size() > position) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        AppItem item = getItemInfoByPosition(position);
        holder.mName.setText(item.mName);
        holder.mUsage.setText(AppUtil.formatMilliSeconds(item.mUsageTime));
        holder.mTime.setText(String.format(Locale.getDefault(),
                "%s · %d %s",
                new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(item.mEventTime)),
                item.mCount,
                mcontext.getResources().getString(R.string.times_only))
        );
        holder.mDataUsage.setText(String.format(Locale.getDefault(), "%s", AppUtil.humanReadableByteCount(item.mMobile)));
        if (mTotal > 0) {
            holder.mProgress.setProgress((int) (item.mUsageTime * 100 / mTotal));
        } else {
            holder.mProgress.setProgress(0);
        }
//        GlideApp.with(MainActivity.this)
//                .load(AppUtil.getPackageIcon(MainActivity.this, item.mPackageName))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transition(new DrawableTransitionOptions().crossFade())
//                .into(holder.mIcon);
        //holder.setOnClickListener(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mUsage;
        private TextView mTime;
        private TextView mDataUsage;
        private ImageView mIcon;
        private ProgressBar mProgress;

        MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.app_name);
            mUsage = itemView.findViewById(R.id.app_usage);
            mTime = itemView.findViewById(R.id.app_time);
            mDataUsage = itemView.findViewById(R.id.app_data_usage);
            mIcon = itemView.findViewById(R.id.app_image);
            mProgress = itemView.findViewById(R.id.progressBar);
            //itemView.setOnCreateContextMenuListener(this);
        }



    }
}