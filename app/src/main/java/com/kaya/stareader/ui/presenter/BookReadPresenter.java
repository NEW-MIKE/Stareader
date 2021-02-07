/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kaya.stareader.ui.presenter;

import android.content.Context;


import com.kaya.stareader.data.model.bean.book.BookMixAToc;
import com.kaya.stareader.data.model.bean.other.ChapterRead;
import com.kaya.stareader.data.model.bean.recommend.Recommend;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.BookReadContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BookReadPresenter extends RxPresenter<BookReadContract.View>
        implements BookReadContract.Presenter<BookReadContract.View> {

    private Context mContext;
    private BookApi bookApi;

    @Inject
    public BookReadPresenter(Context mContext, BookApi bookApi) {
        this.mContext = mContext;
        this.bookApi = bookApi;
    }

    @Override
    public void getBookMixAToc(final String bookId, String viewChapters) {
        String key = StringUtils.creatAcacheKey("book-toc", bookId, viewChapters);
        Observable<BookMixAToc.mixToc> fromNetWork = bookApi.getBookMixAToc(bookId, viewChapters)
                .map(new Function<BookMixAToc, BookMixAToc.mixToc>() {
                    @Override
                    public BookMixAToc.mixToc apply(BookMixAToc bookMixAToc) throws Throwable {
                        return bookMixAToc.mixToc;
                    }

                })
                .compose(RxUtil.<BookMixAToc.mixToc>rxCacheListHelper(key));

        //依次检查disk、network
        Observable
                .concat(RxUtil.rxCreateDiskObservable(key, BookMixAToc.mixToc.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc.mixToc>() {
                    @Override
                    public void onNext(BookMixAToc.mixToc data) {
                        List<BookMixAToc.mixToc.Chapters> list = data.chapters;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showBookToc(list);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
                        mView.netError(0);
                    }
                });
    }

    @Override
    public void getChapterRead(String url, final int chapter) {
        bookApi.getChapterRead(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChapterRead>() {
                    @Override
                    public void onNext(ChapterRead data) {
                        if (data.chapter != null && mView != null) {
                            mView.showChapterRead(data.chapter, chapter);
                        } else {
                            mView.netError(chapter);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
                        mView.netError(chapter);
                    }
                });
    }

    @Override
    public void merginAllBook(Recommend.RecommendBooks recommendBooks, List<BookMixAToc.mixToc.Chapters> list) {

        String fileName = recommendBooks.title;
//        File file = FileUtils.getMerginBook(fileName + ".txt");
//        Subscription rxSubscription = new ObserverSubscriber<Object>
//                (){};


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String o) {

                    }
                });//);


//        DownloadProgress progress

    }
}