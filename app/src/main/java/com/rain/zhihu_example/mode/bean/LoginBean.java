package com.rain.zhihu_example.mode.bean;

/**
 * 登录的bean对象
 *
 * @author yangchunyu
 *         2016/3/7
 *         14:39
 */
public class LoginBean {
    private String userId;
    private String nickName;
    private String ico;
    private String token;
    private long expiresTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }
}
