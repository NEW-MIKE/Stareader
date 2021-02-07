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


import com.kaya.stareader.data.model.bean.book.BookReview;
import com.kaya.stareader.data.model.bean.other.CommentList;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.BookReviewDetailContract;
import com.kaya.stareader.utils.LogUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BookReviewDetailPresenter extends RxPresenter<BookReviewDetailContract.View> implements BookReviewDetailContract.Presenter {

    private BookApi bookApi;

    @Inject
    public BookReviewDetailPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getBookReviewDetail(String id) {
        bookApi.getBookReviewDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookReview>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBookReviewDetail:" + e.toString());
                    }

                    @Override
                    public void onNext(BookReview data) {
                        mView.showBookReviewDetail(data);
                    }
                });
    }

    @Override
    public void getBestComments(String bookReviewId) {
        bookApi.getBestComments(bookReviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentList>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBestComments:" + e.toString());
                    }

                    @Override
                    public void onNext(CommentList list) {
                        mView.showBestComments(list);
                    }
                });
    }

    @Override
    public void getBookReviewComments(String bookReviewId, int start, int limit) {
        bookApi.getBookReviewComments(bookReviewId, start + "", limit + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentList>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getBookReviewComments:" + e.toString());
                    }

                    @Override
                    public void onNext(CommentList list) {
                        mView.showBookReviewComments(list);
                    }
                });
    }

}
