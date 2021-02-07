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

import com.kaya.stareader.data.model.bean.book.BooksByTag;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.BooksByTagContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class BooksByTagPresenter extends RxPresenter<BooksByTagContract.View> implements BooksByTagContract.Presenter<BooksByTagContract.View> {

    private BookApi bookApi;

    private boolean isLoading = false;

    @Inject
    public BooksByTagPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBooksByTag(String tags, final String start, String limit) {
        if (!isLoading) {
            isLoading = true;
            String key = StringUtils.creatAcacheKey("books-by-tag", tags, start, limit);
            Observable<BooksByTag> fromNetWork = bookApi.getBooksByTag(tags, start, limit)
                    .compose(RxUtil.<BooksByTag>rxCacheListHelper(key));

            //依次检查disk、network
            Observable.concat(RxUtil.rxCreateDiskObservable(key, BooksByTag.class), fromNetWork)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BooksByTag>() {
                        @Override
                        public void onNext(BooksByTag data) {
                            if (data != null) {
                                List<BooksByTag.TagBook> list = data.books;
                                if (list != null && !list.isEmpty() && mView != null) {
                                    boolean isRefresh = start.equals("0") ? true : false;
                                    mView.showBooksByTag(list, isRefresh);
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                            mView.onLoadComplete(true, "");
                        }

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            addSubscrebe(d);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e(e.toString());
                            isLoading = false;
                            mView.onLoadComplete(false, e.toString());
                        }
                    });
        }
    }
}
