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
package com.kaya.stareader.ui.contract;


import com.kaya.stareader.data.model.bean.book.BooksByTag;
import com.kaya.stareader.ui.base.BaseContract;

import java.util.List;

/**
 * @author lfh.
 * @date 2016/8/30.
 */
public interface SearchByAuthorContract {

    interface View extends BaseContract.BaseView {
        void showSearchResultList(List<BooksByTag.TagBook> list);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getSearchResultList(String author);
    }

}
