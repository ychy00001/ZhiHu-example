package com.rain.zhihu_example.ui.view;

import com.rain.zhihu_example.mode.bean.SubscribeBean;

/**
 * @author yangchunyu
 *         2016/2/22
 *         14:49
 */
public interface SubscribeView {
    void setListData(SubscribeBean bean);
    void loadDataComplete();
    void notifyLoadMoreData(SubscribeBean moreData);
}
