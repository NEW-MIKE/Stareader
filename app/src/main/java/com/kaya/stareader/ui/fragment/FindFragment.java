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

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaya.stareader.R;
import com.kaya.stareader.data.model.bean.support.FindBean;
import com.kaya.stareader.di.component.AppComponent;
import com.kaya.stareader.ui.activity.SubjectBookListActivity;
import com.kaya.stareader.ui.activity.TopCategoryListActivity;
import com.kaya.stareader.ui.activity.TopRankActivity;
import com.kaya.stareader.ui.adapter.FindAdapter;
import com.kaya.stareader.ui.base.BaseFragment;
import com.kaya.stareader.utils.ttsplayer.OnRvItemClickListener;
import com.kaya.stareader.views.SupportDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseFragment implements OnRvItemClickListener<FindBean> {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private FindAdapter mAdapter;
    private List<FindBean> mList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initDatas() {
        mList.clear();
        mList.add(new FindBean("排行榜", R.drawable.home_find_rank));
        mList.add(new FindBean("主题书单", R.drawable.home_find_topic));
        mList.add(new FindBean("分类", R.drawable.home_find_category));
    }

    @Override
    public void configViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SupportDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, true));

        mAdapter = new FindAdapter(mContext, mList, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void onItemClick(View view, int position, FindBean data) {
        switch (position) {
            case 0:
                TopRankActivity.startActivity(activity);
                break;
            case 1:
                SubjectBookListActivity.startActivity(activity);
                break;
            case 2:
                startActivity(new Intent(activity, TopCategoryListActivity.class));
                break;
            default:
                break;
        }
    }

}
