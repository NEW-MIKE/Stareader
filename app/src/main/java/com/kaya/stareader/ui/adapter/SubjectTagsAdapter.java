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
package com.kaya.stareader.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaya.adapterlibrary.recyclerview.EasyRVAdapter;
import com.kaya.adapterlibrary.recyclerview.EasyRVHolder;
import com.kaya.stareader.R;
import com.kaya.stareader.data.model.bean.book.BookListTags;
import com.kaya.stareader.utils.ttsplayer.OnRvItemClickListener;

import java.util.List;

public class SubjectTagsAdapter extends EasyRVAdapter<BookListTags.DataBean> {

    private OnRvItemClickListener listener;

    public SubjectTagsAdapter(Context context, List<BookListTags.DataBean> list) {
        super(context, list, R.layout.item_subject_tags_list);
    }

    @Override
    protected void onBindData(EasyRVHolder viewHolder, int position, BookListTags.DataBean item) {
        RecyclerView rvTagsItem = viewHolder.getView(R.id.rvTagsItem);
        rvTagsItem.setHasFixedSize(true);
        rvTagsItem.setLayoutManager(new GridLayoutManager(mContext, 4));
        TagsItemAdapter adapter = new TagsItemAdapter(mContext, item.tags);
        rvTagsItem.setAdapter(adapter);

        viewHolder.setText(R.id.tvTagGroupName, item.name);
    }

    class TagsItemAdapter extends EasyRVAdapter<String> {

        public TagsItemAdapter(Context context, List<String> list) {
            super(context, list, R.layout.item_subject_tag_list);
        }

        @Override
        protected void onBindData(EasyRVHolder viewHolder, final int position, final String item) {
            viewHolder.setText(R.id.tvTagName, item);
            viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, position, item);
                    }
                }
            });
        }
    }

    public void setItemClickListener(OnRvItemClickListener<String> listener) {
        this.listener = listener;
    }
}
