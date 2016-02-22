package com.rain.zhihu_example.mode.bean;

import java.util.List;

/**
 * 订阅页面信息实体类
 * @author yangchunyu
 *         2016/2/22
 *         13:42
 */
public class SubscribeBean {


    /**
     * background : http://p3.zhimg.com/f0/c2/f0c253357d99fb72fdb16543ad93ca0c.jpg
     * color : 6123007
     * description : 用技术的眼睛仔细看懂每一部动画和漫画
     * editors : [{"avatar":"http://pic3.zhimg.com/787586244_m.jpg","bio":"MOOK《黑白漫文化》主编","id":63,"name":"纸片做的叶子","url":"http://www.zhihu.com/people/zhi-pian-zuo-de-xie-zi"},{"avatar":"http://pic4.zhimg.com/98ab70dd7_m.jpg","bio":"动画评论人","id":34,"name":"马小褂","url":"http://www.zhihu.com/people/magnil"},{"avatar":"http://pic1.zhimg.com/119575ea8_m.jpg","bio":"动画从业者","id":35,"name":"张一弘","url":"http://www.zhihu.com/people/zhang-yihong"},{"avatar":"http://pic4.zhimg.com/ded7f6bd7_m.jpg","bio":"补旧番的人","id":46,"name":"金渡江","url":"http://www.zhihu.com/people/uncleking"},{"avatar":"http://pic1.zhimg.com/da8e974dc_m.jpg","bio":"新番介绍，关注日本电视动画商业性","id":49,"name":"zecy","url":"http://www.zhihu.com/people/zecy"}]
     * image : http://p3.zhimg.com/b0/e8/b0e8195e93e3cfcba8533c921c8c7794.jpg
     * image_source :
     * name : 动漫日报
     * stories : [{"id":7897992,"images":["http://pic2.zhimg.com/f9653788c71677a94fc74d149784fdd9.jpg"],"title":"手冢你要长命百岁哟！","type":2},{"id":7875825,"images":["http://pic1.zhimg.com/728c28cbaa4d58cb0273d78b4edc4df4.jpg"],"title":"在此相遇，在此离别\u2014\u2014柴门文《东京爱情故事》","type":2},{"id":7855633,"images":["http://pic1.zhimg.com/b822f97d4190f3a75c1877b5e3fc5f7c.jpg"],"title":"《元气囝仔》\u2014\u2014元气满满之人","type":2},{"id":7839776,"images":["http://pic3.zhimg.com/b8904560d226f417c3ec04ef1fbc073e.jpg"],"title":"《火影忍者：博人传》成长没有捷径","type":2},{"id":7828850,"images":["http://pic4.zhimg.com/f6ab575dc202083eaf4a37a0e39c3907.jpg"],"title":"《孤独的美食家》：每次孤身一人就餐都是一次华丽的冒险","type":2},{"id":7826449,"images":["http://pic4.zhimg.com/405f1dc4fa74496e23831bf477966ab3.jpg"],"title":"追忆《中华小当家》①特级厨师","type":2},{"id":7810571,"images":["http://pic1.zhimg.com/85b5fd1f4058a730cae1c06aa784dadc.jpg"],"title":"《浪客行》：成长的故事","type":2},{"id":7804566,"images":["http://pic1.zhimg.com/99c34d5ddbb932427bcc1b173e895610.jpg"],"title":"《小丑》Why So Serious?","type":2},{"id":7804560,"images":["http://pic3.zhimg.com/844f7f3c3b491571fe6f1cea1ea2eda2.jpg"],"title":"《鼠族》：水晶般冷酷的心","type":2},{"id":7787037,"images":["http://pic1.zhimg.com/b9277266710715cd41151b85df6f2118.jpg"],"title":"龙珠30周年《超史集》：鸟山明长篇访谈","type":2},{"id":7787036,"images":["http://pic4.zhimg.com/4ee4674419d7ac089af8db61dfba2683.jpg"],"title":"十五年，我和那位作者的一个小故事","type":2},{"id":7783018,"images":["http://pic4.zhimg.com/2f51c6b10064689267cf5f81fff0035b.jpg"],"title":"爱是利刃还是创伤?\u2014\u2014伊藤润二《至死不渝的爱》","type":2},{"id":7762739,"images":["http://pic4.zhimg.com/8a4aab2ea18f294a0c49a72f5f03670f.jpg"],"title":"深红\u2014\u2014《羔羊之歌》的血与夜","type":2},{"id":7754071,"images":["http://pic1.zhimg.com/63e7a21a6eb35ff94e23bcaeb94f559c.jpg"],"title":"《剑风传奇》：永逝白鹰","type":2},{"id":7729663,"images":["http://pic4.zhimg.com/cabc2cb4201a5060889ffcaa100463fb.jpg"],"title":"【教练，我想学漫画④】：动作场面太拖沓？","type":2},{"id":7706113,"images":["http://pic1.zhimg.com/4791c34ee79910b599273d27a2969388.jpg"],"title":"《女子落语》：一部欣赏女孩子可爱之处的漫画？","type":2},{"id":7701274,"images":["http://pic1.zhimg.com/4791c34ee79910b599273d27a2969388.jpg"],"title":"三十岁的《城市猎人》","type":2},{"id":7684285,"images":["http://pic1.zhimg.com/4791c34ee79910b599273d27a2969388.jpg"],"title":"《月刊少女野崎君》：四格漫画改编的大胜利","type":2},{"id":7669169,"images":["http://pic1.zhimg.com/4791c34ee79910b599273d27a2969388.jpg"],"title":"《火影忍者》的战争与和平","type":2},{"id":7676695,"images":["http://pic1.zhimg.com/4791c34ee79910b599273d27a2969388.jpg"],"title":"【教练，我想学漫画③】：什么叫『分镜抄袭』？","type":2}]
     */

    private String background;
    private int color;
    private String description;
    private String image;
    private String image_source;
    private String name;
    /**
     * avatar : http://pic3.zhimg.com/787586244_m.jpg
     * bio : MOOK《黑白漫文化》主编
     * id : 63
     * name : 纸片做的叶子
     * url : http://www.zhihu.com/people/zhi-pian-zuo-de-xie-zi
     */

    private List<EditorsEntity> editors;
    /**
     * id : 7897992
     * images : ["http://pic2.zhimg.com/f9653788c71677a94fc74d149784fdd9.jpg"]
     * title : 手冢你要长命百岁哟！
     * type : 2
     */

    private List<StoriesEntity> stories;

    public void setBackground(String background) { this.background = background;}

    public void setColor(int color) { this.color = color;}

    public void setDescription(String description) { this.description = description;}

    public void setImage(String image) { this.image = image;}

    public void setImage_source(String image_source) { this.image_source = image_source;}

    public void setName(String name) { this.name = name;}

    public void setEditors(List<EditorsEntity> editors) { this.editors = editors;}

    public void setStories(List<StoriesEntity> stories) { this.stories = stories;}

    public String getBackground() { return background;}

    public int getColor() { return color;}

    public String getDescription() { return description;}

    public String getImage() { return image;}

    public String getImage_source() { return image_source;}

    public String getName() { return name;}

    public List<EditorsEntity> getEditors() { return editors;}

    public List<StoriesEntity> getStories() { return stories;}

    public static class EditorsEntity {
        private String avatar;
        private String bio;
        private int id;
        private String name;
        private String url;

        public void setAvatar(String avatar) { this.avatar = avatar;}

        public void setBio(String bio) { this.bio = bio;}

        public void setId(int id) { this.id = id;}

        public void setName(String name) { this.name = name;}

        public void setUrl(String url) { this.url = url;}

        public String getAvatar() { return avatar;}

        public String getBio() { return bio;}

        public int getId() { return id;}

        public String getName() { return name;}

        public String getUrl() { return url;}
    }

    public static class StoriesEntity {
        private int id;
        private String title;
        private int type;
        private List<String> images;

        public void setId(int id) { this.id = id;}

        public void setTitle(String title) { this.title = title;}

        public void setType(int type) { this.type = type;}

        public void setImages(List<String> images) { this.images = images;}

        public int getId() { return id;}

        public String getTitle() { return title;}

        public int getType() { return type;}

        public List<String> getImages() { return images;}
    }
}
