package com.kaya.stareader.data.local.prefs;

import java.util.List;

public interface PreferencesHelper {

    Boolean isDayMode();

    void setDayMode(Boolean DayMode);

    List<String> getSearchHistory();

    void saveSearchHistory(Object obj);


    SharedPreferencesUtil removeAll();

    Boolean isByUpdateSort();

    void setUpdateSort(Boolean isByUpdateSort);
}
