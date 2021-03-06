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
package com.kaya.stareader.ui.fragment;

import com.kaya.stareader.R;
import com.kaya.stareader.data.model.bean.book.BookHelpList;
import com.kaya.stareader.data.model.bean.support.SelectionEvent;
import com.kaya.stareader.di.component.AppComponent;
import com.kaya.stareader.di.component.DaggerCommunityComponent;
import com.kaya.stareader.ui.activity.BookHelpDetailActivity;
import com.kaya.stareader.ui.base.BaseRVFragment;
import com.kaya.stareader.ui.base.Constant;
import com.kaya.stareader.ui.contract.BookHelpContract;
import com.kaya.stareader.ui.easyadapter.BookHelpAdapter;
import com.kaya.stareader.ui.presenter.BookHelpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BookHelpFragment extends BaseRVFragment<BookHelpPresenter, BookHelpList.HelpsBean> implements BookHelpContract.View {

    private String sort = Constant.SortType.DEFAULT;
    private String distillate = Constant.Distillate.ALL;

    @Override
    public int getLayoutResId() {
        return R.layout.common_easy_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCommunityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        initAdapter(BookHelpAdapter.class, true, true);
        onRefresh();
    }

    @Override
    public void showBookHelpList(List<BookHelpList.HelpsBean> list, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.clear();
        }
        mAdapter.addAll(list);
        start = start + list.size();
    }

    @Override
    public void showError() {
        loaddingError();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initCategoryList(SelectionEvent event) {
        mRecyclerView.setRefreshing(true);
        sort = event.sort;
        distillate = event.distillate;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getBookHelpList(sort, distillate, 0, limit);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getBookHelpList(sort, distillate, start, limit);
    }

    @Override
    public void onItemClick(int position) {
        BookHelpList.HelpsBean data = mAdapter.getItem(position);
        BookHelpDetailActivity.startActivity(activity, data._id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
