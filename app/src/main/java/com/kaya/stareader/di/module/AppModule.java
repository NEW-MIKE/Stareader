package com.kaya.stareader.di.module;

import android.app.Application;
import android.content.Context;

import com.kaya.stareader.data.AppDataManager;
import com.kaya.stareader.data.DataManager;
import com.kaya.stareader.data.local.prefs.AppPreferencesHelper;
import com.kaya.stareader.data.local.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mApplication;

    @Provides
    public Context provideContext() {
        return mApplication;
    }

    public AppModule(Application application) {
        mApplication = application;
    }
    @Provides
    PreferencesHelper providePreferencesHelper() {
        return new AppPreferencesHelper(mApplication);
    }

    @Provides
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }
}
