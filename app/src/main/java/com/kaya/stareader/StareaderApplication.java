package com.kaya.stareader;

import android.app.Application;
import android.content.Context;

import com.kaya.stareader.data.local.prefs.PreferencesHelper;
import com.kaya.stareader.di.component.AppComponent;
import com.kaya.stareader.di.component.DaggerAppComponent;
import com.kaya.stareader.di.module.AppModule;
import com.kaya.stareader.ui.base.CrashHandler;
import com.kaya.stareader.utils.AppUtils;

import javax.inject.Inject;

import leakcanary.AppWatcher;
import leakcanary.ObjectWatcher;

public class StareaderApplication extends Application {


    private static StareaderApplication sInstance;
    public static StareaderApplication getsInstance() {
        return sInstance;
    }
    private ObjectWatcher  appWatcher;
    public static ObjectWatcher  getRefWatcher(Context context) {
        StareaderApplication application = (StareaderApplication) context.getApplicationContext();
        return application.appWatcher;
    }

    @Inject
    PreferencesHelper mAppPreferencesHelper;
    public AppComponent mAppComponet;
    @Override
    public void onCreate() {
        super.onCreate();
        appWatcher =  AppWatcher.INSTANCE.getObjectWatcher();
        sInstance = this;
        AppUtils.init(this);
        CrashHandler.getInstance().init(this);
        getApplicationComponent()
                .inject(this);
    }

    public AppComponent getApplicationComponent() {
        if (mAppComponet == null) {
            mAppComponet = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return mAppComponet;
    }



    public PreferencesHelper getmAppPreferencesHelper()
    {
        return mAppPreferencesHelper;
    }
}
