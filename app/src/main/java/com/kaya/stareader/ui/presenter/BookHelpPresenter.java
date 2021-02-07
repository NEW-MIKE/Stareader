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


import com.kaya.stareader.data.model.bean.book.BookHelpList;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.BookHelpContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
public class BookHelpPresenter extends RxPresenter<BookHelpContract.View> implements BookHelpContract.Presenter {

    private BookApi bookApi;

    @Inject
    public BookHelpPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookHelpList(String sort, String distillate, final int start, int limit) {
        String key = StringUtils.creatAcacheKey("book-help-list", "all", sort, start + "", limit + "", distillate);
        Observable<BookHelpList> fromNetWork = bookApi.getBookHelpList("all", sort, start + "", limit + "", distillate)
                .compose(RxUtil.<BookHelpList>rxCacheListHelper(key));

        //依次检查disk、network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, BookHelpList.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookHelpList>() {
                    @Override
                    public void onComplete() {
                        mView.complete();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBookHelpList:" + e.toString());
                        mView.showError();
                    }

                    @Override
                    public void onNext(BookHelpList list) {
                        boolean isRefresh = start == 0 ? true : false;
                        mView.showBookHelpList(list.helps, isRefresh);
                    }
                });
    }

}
