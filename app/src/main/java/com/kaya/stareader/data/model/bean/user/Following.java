package com.kaya.stareader.data.model.bean.user;

import com.kaya.stareader.data.model.bean.base.Base;

import java.util.List;

public class Following extends Base {

    public List<FollowingsBean> followings;

    public static class FollowingsBean {
        public String _id;
        public String avatar;
        public String nickname;
        public int followers;
        public int followings;
        public int tweets;
    }
}
