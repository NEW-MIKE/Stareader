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

import com.kaya.stareader.data.model.bean.book.BookLists;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.SubjectFragmentContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;
import com.kaya.stareader.utils.ToastUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class SubjectFragmentPresenter extends RxPresenter<SubjectFragmentContract.View> implements SubjectFragmentContract.Presenter<SubjectFragmentContract.View> {

    private BookApi bookApi;

    @Inject
    public SubjectFragmentPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookLists(String duration, String sort, final int start, int limit, String tag, String gender) {
        String key = StringUtils.creatAcacheKey("book-lists", duration, sort, start + "", limit + "", tag, gender);
        Observable<BookLists> fromNetWork = bookApi.getBookLists(duration, sort, start + "", limit + "", tag, gender)
                .compose(RxUtil.<BookLists>rxCacheListHelper(key));

        //依次检查disk、network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, BookLists.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookLists>() {
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
                        LogUtils.e("getBookLists:" + e.toString());
                        mView.showError();
                    }

                    @Override
                    public void onNext(BookLists tags) {
                        mView.showBookList(tags.bookLists, start == 0 ? true : false);
                        if (tags.bookLists == null || tags.bookLists.size() <= 0) {
                            ToastUtils.showSingleToast("暂无相关书单");
                        }
                    }
                });
    }
}
