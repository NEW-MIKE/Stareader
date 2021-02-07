package com.kaya.stareader.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.kaya.stareader.di.PreferenceInfo;

import java.util.List;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {


    private static final String PREF_KEY_DAY_MODE = "PREF_KEY_DAY_MODE";

    private static final String PREF_KEY_SEARCH_HISTORY = "searchHistory";

    private static final String PREF_KEY_IS_BY_UPDATE_SORT = "isByUpdateSort";

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";

    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";

    public AppPreferencesHelper(Context context) {
        SharedPreferencesUtil.init(context, "stareader", Context.MODE_PRIVATE);
    }

    @Override
    public Boolean isDayMode() {
        return SharedPreferencesUtil.getInstance().getBoolean(PREF_KEY_DAY_MODE, false);
    }

    @Override
    public void setDayMode(Boolean DayMode) {
        SharedPreferencesUtil.getInstance().putBoolean(PREF_KEY_DAY_MODE, DayMode);
    }

    @Override
    public List<String> getSearchHistory() {
        return SharedPreferencesUtil.getInstance().getObject(PREF_KEY_SEARCH_HISTORY,List.class);
    }

    @Override
    public void saveSearchHistory(Object obj) {
        SharedPreferencesUtil.getInstance().putObject(PREF_KEY_SEARCH_HISTORY,obj);
    }

    @Override
    public SharedPreferencesUtil removeAll() {
        return SharedPreferencesUtil.getInstance().removeAll();
    }

    @Override
    public Boolean isByUpdateSort() {
        return SharedPreferencesUtil.getInstance().getBoolean(PREF_KEY_DAY_MODE, true);
    }

    @Override
    public void setUpdateSort(Boolean isByUpdateSort) {
        SharedPreferencesUtil.getInstance().putBoolean(PREF_KEY_IS_BY_UPDATE_SORT, isByUpdateSort);
    }
}
