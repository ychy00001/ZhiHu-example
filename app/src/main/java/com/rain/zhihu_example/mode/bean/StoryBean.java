package com.rain.zhihu_example.mode.bean;

import java.util.List;

/**
 * 故事内容Bean对象
 * Created by yangchunyu
 * 2016/2/4 13:59
 */
public class StoryBean {

    /**
     * body : <div class="main-wrap content-wrap">
    ......
     </div>
     </div>
     * image_source : 《年轻气盛》
     * title : 2015 下半年国内外的好电影，你还差哪部没看？
     * image : http://pic2.zhimg.com/473322e1b302a3a0a8d696576159e5b9.jpg
     * share_url : http://daily.zhihu.com/story/7836830
     * js : []
     * recommenders : [{"avatar":"http://pic3.zhimg.com/6ed61a92400bb0028ffcc9e2272c9f4e_m.jpg"}]
     * ga_prefix : 020321
     * section : {"thumbnail":"http://pic3.zhimg.com/3e7377d39af851317633e99552b3db86.jpg","id":28,"name":"放映机"}
     * type : 0
     * id : 7836830
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=77778"]
     */

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String ga_prefix;
    /**
     * thumbnail : http://pic3.zhimg.com/3e7377d39af851317633e99552b3db86.jpg
     * id : 28
     * name : 放映机
     */

    private SectionEntity section;
    private int type;
    private int id;
    private List<String> js;
    /**
     * avatar : http://pic3.zhimg.com/6ed61a92400bb0028ffcc9e2272c9f4e_m.jpg
     */

    private List<RecommendersEntity> recommenders;
    private List<String> css;

    public void setBody(String body) { this.body = body;}

    public void setImage_source(String image_source) { this.image_source = image_source;}

    public void setTitle(String title) { this.title = title;}

    public void setImage(String image) { this.image = image;}

    public void setShare_url(String share_url) { this.share_url = share_url;}

    public void setGa_prefix(String ga_prefix) { this.ga_prefix = ga_prefix;}

    public void setSection(SectionEntity section) { this.section = section;}

    public void setType(int type) { this.type = type;}

    public void setId(int id) { this.id = id;}

    public void setJs(List<String> js) { this.js = js;}

    public void setRecommenders(List<RecommendersEntity> recommenders) { this.recommenders = recommenders;}

    public void setCss(List<String> css) { this.css = css;}

    public String getBody() {
        String add = "</br></br></br>";
//        String add = "";
        return add+body;}

    public String getImage_source() { return image_source;}

    public String getTitle() { return title;}

    public String getImage() { return image;}

    public String getShare_url() { return share_url;}

    public String getGa_prefix() { return ga_prefix;}

    public SectionEntity getSection() { return section;}

    public int getType() { return type;}

    public int getId() { return id;}

    public List<String> getJs() { return js;}

    public List<RecommendersEntity> getRecommenders() { return recommenders;}

    public List<String> getCss() { return css;}

    public static class SectionEntity {
        private String thumbnail;
        private int id;
        private String name;

        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail;}

        public void setId(int id) { this.id = id;}

        public void setName(String name) { this.name = name;}

        public String getThumbnail() { return thumbnail;}

        public int getId() { return id;}

        public String getName() { return name;}
    }

    public static class RecommendersEntity {
        private String avatar;

        public void setAvatar(String avatar) { this.avatar = avatar;}

        public String getAvatar() { return avatar;}
    }
}
