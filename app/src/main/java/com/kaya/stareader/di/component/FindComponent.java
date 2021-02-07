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
package com.kaya.stareader.di.component;



import com.kaya.stareader.ui.activity.SubCategoryListActivity;
import com.kaya.stareader.ui.activity.SubOtherHomeRankActivity;
import com.kaya.stareader.ui.activity.SubRankActivity;
import com.kaya.stareader.ui.activity.SubjectBookListActivity;
import com.kaya.stareader.ui.activity.SubjectBookListDetailActivity;
import com.kaya.stareader.ui.activity.TopCategoryListActivity;
import com.kaya.stareader.ui.activity.TopRankActivity;
import com.kaya.stareader.ui.fragment.SubCategoryFragment;
import com.kaya.stareader.ui.fragment.SubRankFragment;
import com.kaya.stareader.ui.fragment.SubjectFragment;

import dagger.Component;

/**
 * @author yuyh.
 * @date 16/9/1.
 */
@Component(dependencies = AppComponent.class)
public interface FindComponent {

    /** 分类 **/
    TopCategoryListActivity inject(TopCategoryListActivity activity);

    SubCategoryListActivity inject(SubCategoryListActivity activity);

    SubCategoryFragment inject(SubCategoryFragment fragment);

    /** 排行 **/
    TopRankActivity inject(TopRankActivity activity);

    SubRankActivity inject(SubRankActivity activity);

    SubOtherHomeRankActivity inject(SubOtherHomeRankActivity activity);

    SubRankFragment inject(SubRankFragment fragment);

    /** 主题书单 **/
    SubjectBookListActivity inject(SubjectBookListActivity subjectBookListActivity);

    SubjectFragment inject(SubjectFragment subjectFragment);

    SubjectBookListDetailActivity inject(SubjectBookListDetailActivity categoryListActivity);
}
