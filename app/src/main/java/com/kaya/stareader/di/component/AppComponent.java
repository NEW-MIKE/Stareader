package com.kaya.stareader.di.component;

import android.content.Context;

import com.kaya.stareader.StareaderApplication;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.di.module.AppModule;
import com.kaya.stareader.di.module.BookApiModule;

import dagger.Component;

@Component(modules = {AppModule.class, BookApiModule.class})
public interface AppComponent {
    void inject(StareaderApplication app);
    Context getContext();
    BookApi getReaderApi();
}
