package com.kaya.stareader.data;

import com.kaya.stareader.data.local.prefs.PreferencesHelper;
import com.kaya.stareader.data.local.prefs.SharedPreferencesUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataManager implements DataManager{


    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(PreferencesHelper mPreferencesHelper) {
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public Boolean isDayMode() {
        return mPreferencesHelper.isDayMode();
    }

    @Override
    public void setDayMode(Boolean DayMode) {
        mPreferencesHelper.setDayMode(DayMode);
    }

    @Override
    public List<String> getSearchHistory() {
        return mPreferencesHelper.getSearchHistory();
    }

    @Override
    public void saveSearchHistory(Object obj) {
        mPreferencesHelper.saveSearchHistory(obj);
    }

    @Override
    public SharedPreferencesUtil removeAll() {
        return mPreferencesHelper.removeAll();
    }

    @Override
    public Boolean isByUpdateSort() {
        return mPreferencesHelper.isByUpdateSort();
    }

    @Override
    public void setUpdateSort(Boolean isByUpdateSort) {
        mPreferencesHelper.setUpdateSort(isByUpdateSort);
    }
}
