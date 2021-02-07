/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kaya.stareader.utils;

import android.text.TextUtils;

import androidx.annotation.MainThread;

import com.google.gson.Gson;
import com.kaya.stareader.StareaderApplication;

import java.lang.reflect.Field;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RxUtil {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> clazz) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                LogUtils.d("get data from disk: key==" + key);
                String json = "ACache.get(ReaderApplication.getsInstance()).getAsString(key)";
                LogUtils.d("get data from disk finish , json==" + json);
                if (!TextUtils.isEmpty(json)) {
                    emitter.onNext(json);
                }
                emitter.onComplete();

            }
        })
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String s) throws Throwable {
                        return new Gson().fromJson(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> rxCacheBeanHelper(final String key) {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<T>() {
                            @Override
                            public void accept(T data) throws Throwable {
                                Schedulers.io().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        ACache.get(StareaderApplication.getsInstance())
                                                .put(key, new Gson().toJson(data, data.getClass()));
                                        LogUtils.d("cache finish");
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> rxCacheListHelper(final String key) {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<T>() {
                            @Override
                            public void accept(T data) throws Throwable {
                                Schedulers.io().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {

                                        LogUtils.d("get data from network finish ,start cache...");
                                        //通过反射获取List,再判空决定是否缓存
                                        if (data == null)
                                        { return;}
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            String className = field.getType().getSimpleName();
                                            // 得到属性值
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    if (list != null && !list.isEmpty()) {
                                                        ACache.get(StareaderApplication.getsInstance())
                                                                .put(key, new Gson().toJson(data, clazz));
                                                        LogUtils.d("cache finish");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
