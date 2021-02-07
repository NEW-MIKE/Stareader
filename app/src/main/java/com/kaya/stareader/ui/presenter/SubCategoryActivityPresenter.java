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

import com.kaya.stareader.data.model.bean.category.CategoryListLv2;
import com.kaya.stareader.data.remote.BookApi;
import com.kaya.stareader.ui.base.RxPresenter;
import com.kaya.stareader.ui.contract.SubCategoryActivityContract;
import com.kaya.stareader.utils.LogUtils;
import com.kaya.stareader.utils.RxUtil;
import com.kaya.stareader.utils.StringUtils;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SubCategoryActivityPresenter extends RxPresenter<SubCategoryActivityContract.View> implements SubCategoryActivityContract.Presenter<SubCategoryActivityContract.View> {

    private BookApi bookApi;

    @Inject
    public SubCategoryActivityPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getCategoryListLv2() {
        String key = StringUtils.creatAcacheKey("category-list2");
        Observable<CategoryListLv2> fromNetWork = bookApi.getCategoryListLv2()
                .compose(RxUtil.<CategoryListLv2>rxCacheListHelper(key));

        //依次检查disk、network
        Observable.concat(RxUtil.rxCreateDiskObservable(key, CategoryListLv2.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryListLv2>() {
                    @Override
                    public void onComplete() {
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getCategoryListLv2:" + e.toString());
                        mView.showError();
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscrebe(d);
                    }

                    @Override
                    public void onNext(CategoryListLv2 categoryListLv2) {
                        mView.showCategoryList(categoryListLv2);
                    }
                });
    }

}
