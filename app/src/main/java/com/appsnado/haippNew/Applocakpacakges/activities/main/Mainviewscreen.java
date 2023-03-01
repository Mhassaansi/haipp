package com.appsnado.haippNew.Applocakpacakges.activities.main;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsnado.haippNew.Applocakpacakges.AppInfo;

import com.appsnado.haippNew.Applocakpacakges.Model;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.LockMainContract;
import com.appsnado.haippNew.Applocakpacakges.mvp.p.LockMainPresenter;
import com.appsnado.haippNew.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mainviewscreen extends AppCompatActivity implements LockMainContract.View {

       private LockMainPresenter mLockMainPresenter;
       private ImageView imgview;
       int count = 0;
       HashMap<String, ArrayList<Model>> map = new HashMap<String, ArrayList<Model>>();


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_mainviewscreen_lock);
           FirebaseApp.initializeApp(this);


           mLockMainPresenter = new LockMainPresenter(this, this);
           mLockMainPresenter.loadAppInfo(this);
           imgview = (ImageView)findViewById(R.id.img);

    }

    @Override
    public void loadAppInfoSuccess(List<CommLockInfo> list) {
        PackageManager pm = this.getPackageManager();
        ArrayList<AppInfo> myAppsToUpdate = null;
        ArrayList<Model> appdata = null;
        Log.i("TAG", "loadAppInfoSuccess: "+list);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("Apps");



        myAppsToUpdate = new ArrayList();
        appdata = new ArrayList();

//        for (CommLockInfo info : list) {
//
//
//            AppInfo appInfo = new AppInfo();
//            appInfo.setPacakagename(CommLockInfo.packageName);
//            appInfo.setHide("0");
//            ApplicationInfo appInfoicon = CommLockInfo.getAppInfo();
//
//
//            final HashMap apps = new Model();
//            apps.put("packageName", CommLockInfo.packageName);
//            apps.put("isLocked", false);
//            apps.put("isSysApp", false);
//         //   appdata.add((Model) apps);
//
//
//            PackageInfo appin = null;
//            try {
//                appin = pm.getPackageInfo(CommLockInfo.packageName, 0);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            // appInfo.setVersionName(info.versionName.toString())
//            // appInfo.setVersionCode("" + info.versionCode)
//
//            appInfo.setIcon(appin.applicationInfo.loadIcon(pm));
//            Drawable drawable = appin.applicationInfo.loadIcon(pm);
//            imgview.setImageDrawable(appin.applicationInfo.loadIcon(pm));
//
//            Bitmap bitmaps = drawableToBitmap(imgview.getDrawable());
//            myAppsToUpdate.add(appInfo);
//
//
//            ///   if (count == arrImagesPath!!.size) {
//
//
//
//            try {
//
////
////                var downloadUrl: Task<Uri>? = null
////                val baos = ByteArrayOutputStream()
////                bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, baos)
////                val data: ByteArray = baos.toByteArray()
////                val storage: FirebaseStorage = FirebaseStorage.getInstance()
////                val storageRef: StorageReference =
////                        storage.getReferenceFromUrl("https://apptest-c1699-default-rtdb.firebaseio.com/")
////                val imagesRef: StorageReference = storageRef.child(
////                        "images/" + aInfo.loadLabel(
////                                pm
////                        ).toString() + ".jpg"
////                )
////                val uploadTask: UploadTask = imagesRef.putBytes(data)
//
//
//                    final ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setTitle("Uploading...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//                FirebaseStorage storage  = FirebaseStorage.getInstance();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//               bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] data = baos.toByteArray();
//                StorageReference storageRef =
//                        storage.getReferenceFromUrl("gs://demotestapp-8623a.appspot.com");
//                StorageReference imagesRef = storageRef.child(
//                        "images/" + CommLockInfo.packageName.toString() + ".jpg"
//                );
//
//                UploadTask uploadTask  = imagesRef.putBytes(data);
//
//                final ArrayList<AppInfo> finalMyAppsToUpdate = myAppsToUpdate;
//                final ArrayList<Model> finalAppdata = appdata;
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    progressDialog.dismiss();
//                                    //Commonvarible.globalObj.Dialogsetjob(getActivity(), " Uploaded","");
//                                    //   Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
//                                    //downloadUrl = taskSnapshot.getDownloadUrl();
//                                    /// sendMsg("" + downloadUrl, 2);
//                               /* downloadUrl= taskSnapshot.getUploadSessionUri();
//
//                                Log.d("downloadUrl-->", "" + downloadUrl);*/
//
//
//                                    Task<Uri> downloadUrlss = taskSnapshot.getStorage().getDownloadUrl();
//                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
//                                    while(!uri.isComplete());
//                                    Uri url = uri.getResult();
//                                    Log.i(TAG, "onSuccess: "+downloadUrlss);
//                                    Log.i(TAG, "onSuccess: "+url);
//                                    Log.d("downloadUrl-->", "" + downloadUrlss);
//
//                                    count++;
//
//                                    apps.put("icon",  url.toString());
//                                    finalAppdata.add((Model) apps);
//
//
//                                    if (count == finalMyAppsToUpdate.size()) {
//                                        progressDialog.dismiss();
//                                        Log.i("TAG", "onSuccess: ");
//                                        Log.d("TAG", "setApplication: ");
//                                        map.put("appdata", finalAppdata);
//
//                                        try {
//                                            myRef.setValue(map);
//                                        } catch (Exception e) {
//                                            e.getStackTrace();
//                                        }
//                                    }
//
//
//
//
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    progressDialog.dismiss();
//                                }
//                            })
//                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                                            .getTotalByteCount());
//                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
//                                }
//                            });
//
//            }catch (Exception e){
//                e.getStackTrace();
//            }
//
//
//
//
//        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}