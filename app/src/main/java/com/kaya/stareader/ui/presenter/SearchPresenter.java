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



import com.kaya.stareader.data.model.bean.other.AutoComplete;
import com.kaya.stareader.data.model.bean.other.HotWord;
import com.kaya.stareader.data.model.bean.other.SearchDetail;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.SearchContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter<SearchContract.View> {

    private BookApi bookApi;

    @Inject
    public SearchPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public void getHotWordList() {
        String key = StringUtils.creatAcacheKey("hot-word-list");
        Observable<HotWord> fromNetWork = bookApi.getHotWord()
                .compose(RxUtil.<HotWord>rxCacheListHelper(key));

        //依次检查disk、network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, HotWord.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotWord>() {
                    @Override
                    public void onNext(HotWord hotWord) {
                        List<String> list = hotWord.hotWords;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showHotWordList(list);
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
                        LogUtils.e("onError: " + e);
                    }
                });
    }

    @Override
    public void getAutoCompleteList(String query) {
        bookApi.getAutoComplete(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AutoComplete>() {
                    @Override
                    public void onNext(AutoComplete autoComplete) {
                        LogUtils.e("getAutoCompleteList" + autoComplete.keywords);
                        List<String> list = autoComplete.keywords;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showAutoCompleteList(list);
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
                        LogUtils.e(e.toString());
                    }
                });
    }

    @Override
    public void getSearchResultList(String query) {
        bookApi.getSearchResult(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchDetail>() {
                    @Override
                    public void onNext(SearchDetail bean) {
                        List<SearchDetail.SearchBooks> list = bean.books;
                        if (list != null && !list.isEmpty() && mView != null) {
                            mView.showSearchResultList(list);
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
                        LogUtils.e(e.toString());
                    }
                });
    }
}
