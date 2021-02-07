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
package com.kaya.stareader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kaya.adapterlibrary.glide.GlideCircleTransform;
import com.kaya.stareader.R;
import com.kaya.stareader.data.model.bean.discussion.Disscussion;
import com.kaya.stareader.data.model.bean.other.CommentList;
import com.kaya.stareader.di.component.AppComponent;
import com.kaya.stareader.di.component.DaggerCommunityComponent;
import com.kaya.stareader.ui.adapter.BestCommentListAdapter;
import com.kaya.stareader.ui.base.BaseRVActivity;
import com.kaya.stareader.ui.base.Constant;
import com.kaya.stareader.ui.contract.BookDiscussionDetailContract;
import com.kaya.stareader.ui.easyadapter.CommentListAdapter;
import com.kaya.stareader.ui.presenter.BookDiscussionDetailPresenter;
import com.kaya.stareader.utils.FormatUtils;
import com.kaya.stareader.utils.ttsplayer.OnRvItemClickListener;
import com.kaya.stareader.views.BookContentTextView;
import com.kaya.stareader.views.SupportDividerItemDecoration;
import com.kaya.stareader.views.recyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 综合讨论区详情
 */
public class BookDiscussionDetailActivity extends BaseRVActivity<CommentList.CommentsBean>
        implements BookDiscussionDetailContract.View, OnRvItemClickListener<CommentList.CommentsBean> {

    private static final String INTENT_ID = "id";

    public static void startActivity(Context context, String id) {
        context.startActivity(new Intent(context, BookDiscussionDetailActivity.class)
                .putExtra(INTENT_ID, id));
    }

    private String id;
    private List<CommentList.CommentsBean> mBestCommentList = new ArrayList<>();
    private BestCommentListAdapter mBestCommentListAdapter;
    private HeaderViewHolder headerViewHolder;

    @Inject
    BookDiscussionDetailPresenter mPresenter;

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    static class HeaderViewHolder {
        @BindView(R.id.ivBookCover)
        ImageView ivAvatar;
        @BindView(R.id.tvBookTitle)
        TextView tvNickName;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvContent)
        BookContentTextView tvContent;
        @BindView(R.id.tvBestComments)
        TextView tvBestComments;
        @BindView(R.id.rvBestComments)
        RecyclerView rvBestComments;
        @BindView(R.id.tvCommentCount)
        TextView tvCommentCount;
        Unbinder bind;
        public HeaderViewHolder(View view) {
            bind = ButterKnife.bind(this, view);//view绑定
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_community_book_discussion_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCommunityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("详情");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        id = getIntent().getStringExtra(INTENT_ID);

        mPresenter.attachView(this);
        mPresenter.getBookDisscussionDetail(id);
        mPresenter.getBestComments(id);
        mPresenter.getBookDisscussionComments(id, start, limit);
    }

    @Override
    public void configViews() {
        initAdapter(CommentListAdapter.class, false, true);

        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = LayoutInflater.from(BookDiscussionDetailActivity.this).inflate(R.layout.header_view_book_discussion_detail, parent, false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {
                headerViewHolder = new HeaderViewHolder(headerView);
            }
        });

    }

    @Override
    public void showBookDisscussionDetail(Disscussion disscussion) {
        Glide.with(mContext)
                .load(Constant.IMG_BASE_URL + disscussion.post.author.avatar)
                .placeholder(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(mContext))
                .into(headerViewHolder.ivAvatar);

        headerViewHolder.tvNickName.setText(disscussion.post.author.nickname);
        headerViewHolder.tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(disscussion.post.created));
        headerViewHolder.tvTitle.setText(disscussion.post.title);
        headerViewHolder.tvContent.setText(disscussion.post.content);
        headerViewHolder.tvCommentCount.setText(String.format(mContext.getString(R.string.comment_comment_count), disscussion.post.commentCount));
    }

    @Override
    public void showBestComments(CommentList list) {
        if (list.comments.isEmpty()) {
            gone(headerViewHolder.tvBestComments, headerViewHolder.rvBestComments);
        } else {
            mBestCommentList.addAll(list.comments);
            headerViewHolder.rvBestComments.setHasFixedSize(true);
            headerViewHolder.rvBestComments.setLayoutManager(new LinearLayoutManager(this));
            headerViewHolder.rvBestComments.addItemDecoration(new SupportDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, true));
            mBestCommentListAdapter = new BestCommentListAdapter(mContext, mBestCommentList);
            mBestCommentListAdapter.setOnItemClickListener(this);
            headerViewHolder.rvBestComments.setAdapter(mBestCommentListAdapter);
            visible(headerViewHolder.tvBestComments, headerViewHolder.rvBestComments);
        }
    }

    @Override
    public void showBookDisscussionComments(CommentList list) {
        mAdapter.addAll(list.comments);
        start = start + list.comments.size();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        mPresenter.getBookDisscussionComments(id, start, limit);
    }

    @Override
    public void onItemClick(View view, int position, CommentList.CommentsBean data) {

    }

    @Override
    public void onItemClick(int position) {
        CommentList.CommentsBean data = mAdapter.getItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        headerViewHolder.bind.unbind();
        /*ButterKnife.bind(headerViewHolder).unbind();*/
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
