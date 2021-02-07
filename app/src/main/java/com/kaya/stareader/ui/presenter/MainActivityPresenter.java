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
package com.kaya.stareader.ui.presenter;


import com.kaya.stareader.data.model.bean.book.BookMixAToc;
import com.kaya.stareader.data.model.bean.recommend.Recommend;
import com.kaya.stareader.data.model.bean.user.Login;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.logic.CollectionsManager;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.MainContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.ToastUtils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View> {

    private BookApi bookApi;
    public static boolean isLastSyncUpdateed = false;

    @Inject
    public MainActivityPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void login(String uid, String token, String platform) {
        bookApi.login(uid, token, platform).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onNext(Login data) {
                        if (data != null && mView != null && data.ok) {
                            mView.loginSuccess();
                            LogUtils.e(data.user.toString());
                        }
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("login" + e.toString());
                    }
                });
    }

    @Override
    public void syncBookShelf() {
        List<Recommend.RecommendBooks> list = CollectionsManager.getInstance().getCollectionList();
        List<Observable<BookMixAToc.mixToc>> observables = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Recommend.RecommendBooks bean : list) {
                if (!bean.isFromSD) {
                    Observable<BookMixAToc.mixToc> fromNetWork = bookApi.getBookMixAToc(bean._id, "chapters")
                            .map(new Function<BookMixAToc, BookMixAToc.mixToc>() {
                                @Override
                                public BookMixAToc.mixToc apply(BookMixAToc bookMixAToc) throws Throwable {
                                    return bookMixAToc.mixToc;
                                }

                            })
//                    .compose(RxUtil.<BookMixAToc.mixToc>rxCacheListHelper(
//                            StringUtils.creatAcacheKey("book-toc", bean._id, "chapters")))
                            ;
                    observables.add(fromNetWork);
                }
            }
        } else {
            ToastUtils.showSingleToast("书架空空如也...");
            mView.syncBookShelfCompleted();
            return;
        }
        isLastSyncUpdateed = false;
        Observable.mergeDelayError(observables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc.mixToc>() {
                    @Override
                    public void onNext(BookMixAToc.mixToc data) {
                        String lastChapter = data.chapters.get(data.chapters.size() - 1).title;
                        CollectionsManager.getInstance().setLastChapterAndLatelyUpdate(data.book, lastChapter, data.chaptersUpdated);
                    }

                    @Override
                    public void onComplete() {
                        mView.syncBookShelfCompleted();
                        if(isLastSyncUpdateed){
                            ToastUtils.showSingleToast("小説已更新");
                        }else{
                            ToastUtils.showSingleToast("你追的小説沒有更新");
                        }

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError: " + e);
                        mView.showError();
                    }
                });
    }
}
