package com.rain.zhihu_example.ui.view;

/**
 * Created by yangchunyu
 * 2016/2/4 14:24
 */
public interface StoryView {
    void setVebView(String body);
    void setTitleImg(String imgUrl,String imgTitle,String author);

    void setShareMsg(String title,String imgUrl,String shareUrl);
}
