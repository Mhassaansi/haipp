package com.appsnado.haippNew.Main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsnado.haippNew.*
import com.appsnado.haippNew.Activityreport.ReportViewModel
import com.appsnado.haippNew.Adddevices.Adddevicesviewmodel
import com.appsnado.haippNew.AppBlockerActivity.AppBlockerViewModel
import com.appsnado.haippNew.BrowsingHistory.BrowsingHistoryViewModel
import com.appsnado.haippNew.Changeemail.ChangeemailViewModel
import com.appsnado.haippNew.Changepass.ChangepasswordViewModel
import com.appsnado.haippNew.Chat.ChatViewModel
import com.appsnado.haippNew.Completeprofile.CompleteViemodel
import com.appsnado.haippNew.Faq.FaqViewModel
import com.appsnado.haippNew.Feature.FeatureViewModel
import com.appsnado.haippNew.GeoFences.GeoFencesViewModel
import com.appsnado.haippNew.Kiddevices.KiddevicesViewModel
import com.appsnado.haippNew.Kiddevices.ListworkViewModel
import com.appsnado.haippNew.Locationhistory.LocationhistroyViewModel
import com.appsnado.haippNew.PreLogin.PreLoginViewModel
import com.appsnado.haippNew.Role.RoleViewModel
import com.appsnado.haippNew.Setting.SettingViewModel
import com.appsnado.haippNew.Smartschedule.SmartscheduleViewModel
import com.appsnado.haippNew.Smartschedule.WorksheetViewModel
import com.appsnado.haippNew.Subscription.SubscriptionViewModel
import com.appsnado.haippNew.Webfilter.WebfilterViewModel
import com.appsnado.haippNew.Worksheet.WorkSheetChildViewModel
import com.appsnado.haippNew.chidverification.ChildParentViewModel
import com.appsnado.haippNew.notification.NotificationViewModel
import com.appsnado.haippNew.privacy.PrivacyViewModel
import com.appsnado.haippNew.taskChild.ClaimViewModel
import com.appsnado.haippNew.taskChild.TaskViewModel
import com.appsnado.haippNew.taskParent.TaskParentDetailViewModel
import com.appsnado.haippNew.taskParent.TaskParentViewModel
import com.appsnado.haippNew.termandcondition.TermsandconditionViewModel

 class MainViewModelFactory(//private final MovieService movieService;
    private val lan: Datamanager, private val activity: Activity?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(lan.getservices(activity)!!,activity) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(FeatureViewModel::class.java)) {
            return FeatureViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(RoleViewModel::class.java)) {
            return RoleViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(PreLoginViewModel::class.java)) {
            return PreLoginViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(AppBlockerViewModel::class.java)) {
            return AppBlockerViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(BrowsingHistoryViewModel::class.java)) {
            return BrowsingHistoryViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(GeoFencesViewModel::class.java)) {
            return GeoFencesViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(KiddevicesViewModel::class.java)) {
            return KiddevicesViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(ChangepasswordViewModel::class.java)) {
            return ChangepasswordViewModel(lan.getservices(activity)!!,activity) as T
        } else if(modelClass.isAssignableFrom(ChangeemailViewModel::class.java)) {
            return ChangeemailViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(PrivacyViewModel::class.java)) {
            return PrivacyViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(TermsandconditionViewModel::class.java)) {
            return TermsandconditionViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(FaqViewModel::class.java)) {
            return FaqViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(TaskParentViewModel::class.java)) {
            return TaskParentViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(ChildParentViewModel::class.java)) {
            return ChildParentViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(LocationhistroyViewModel::class.java)) {
            return LocationhistroyViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(WebfilterViewModel::class.java)) {
            return WebfilterViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(SmartscheduleViewModel::class.java)) {
            return SmartscheduleViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            return SubscriptionViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(TaskParentDetailViewModel::class.java)) {
            return TaskParentDetailViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(ClaimViewModel::class.java)) {
            return ClaimViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(WorksheetViewModel::class.java)) {
            return WorksheetViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(ListworkViewModel::class.java)) {
            return ListworkViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(lan.getservices(activity)!!) as T
        }else if(modelClass.isAssignableFrom(WorkSheetChildViewModel::class.java)) {
            return WorkSheetChildViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(VerificationViewModel::class.java)) {
            return VerificationViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(ForgetViewModel::class.java)) {
            return ForgetViewModel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(Adddevicesviewmodel::class.java)) {
            return Adddevicesviewmodel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(CompleteViemodel::class.java)) {
            return CompleteViemodel(lan.getservices(activity)!!,activity) as T
        }else if(modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(lan.getservices(activity)!!,activity) as T
        }






        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
