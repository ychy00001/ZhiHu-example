package com.rain.zhihu_example.mode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yangchunyu
 * 2016/1/25 17:34
 */
@SuppressWarnings("unused")
public class HomeMode {

    /**
     * date : 20160125
     * stories : [{"images":["http://pic4.zhimg.com/440cef9acb60c21b85e3e04b300bbea3.jpg"],"type":0,"id":7795176,"ga_prefix":"012517","title":"自拍不「惊悚」，一盏台灯就能做到"},{"images":["http://pic2.zhimg.com/e9956bc7a6d123cbf3e5d6c9ac2df6fd.jpg"],"type":0,"id":7778518,"ga_prefix":"012516","title":"一小块地儿让我塞一千多个字还得好看，一定是在逗我"},{"title":"又想养猫又想养狗？养只狐狸就够了","ga_prefix":"012515","images":["http://pic1.zhimg.com/dfc24dbc3383a4f827399cc4ab73a320.jpg"],"multipic":true,"type":0,"id":7795305},{"images":["http://pic3.zhimg.com/e21e7249f6359cadee14d322e8823c12.jpg"],"type":0,"id":7794462,"ga_prefix":"012514","title":"今天你对崴脚置之不理，明天它会再来找你"},{"images":["http://pic1.zhimg.com/c157860c5993a4a6d7d5d6520d476d00.jpg"],"type":0,"id":7793144,"ga_prefix":"012513","title":"金庸的「射雕三部曲」，其实还有一个隐藏的第一部"},{"images":["http://pic3.zhimg.com/e8ecd61c36e14f62c52f8e2efa854dc2.jpg"],"type":0,"id":7794878,"ga_prefix":"012512","title":"职人介绍所 · 知乎首档视频节目开播"},{"title":"倒贴车费和时间，换来了一段有趣的经历","ga_prefix":"012512","images":["http://pic2.zhimg.com/f68b2f612fce0bf3eb931025d17342ad.jpg"],"multipic":true,"type":0,"id":7793083},{"images":["http://pic3.zhimg.com/18eb3c862482bca1c7f126b8925acfea.jpg"],"type":0,"id":7778552,"ga_prefix":"012511","title":"春节快到了，家里备上这些常用药会比较安心"},{"images":["http://pic3.zhimg.com/8d3fdc6140b5aff8d19979e50627a402.jpg"],"type":0,"id":7793119,"ga_prefix":"012510","title":"孩子想变成最漂亮的人，先别急着否定"},{"images":["http://pic2.zhimg.com/e8ec8a47f8c5ba02b2ca7464e77a907d.jpg"],"type":0,"id":7791684,"ga_prefix":"012509","title":"这到底是广州的第一次下雪、霰、还是冰粒？"},{"title":"一般来说，修建跑道的费用能占整个机场投资的一半","ga_prefix":"012508","images":["http://pic4.zhimg.com/cfa936e4f780abe8106bd8bfbab0fbdf.jpg"],"multipic":true,"type":0,"id":7768668},{"title":"万年备胎小冥的故事","ga_prefix":"012507","images":["http://pic2.zhimg.com/30e98933d98616f9d39fccae9f84d319.jpg"],"multipic":true,"type":0,"id":7783012},{"images":["http://pic2.zhimg.com/0eeb653b6d715b77ff6c08d716a2af3d.jpg"],"type":0,"id":7768541,"ga_prefix":"012507","title":"关于男子亲手勒死自闭症儿子一事，这篇讨论值得一看"},{"images":["http://pic3.zhimg.com/ac388ddbd5bf809a28f0449591b042b2.jpg"],"type":0,"id":7791671,"ga_prefix":"012507","title":"魏坤琳嘲讽郭敬明那句话，是一种我们很容易忽略的「微暴力」"},{"images":["http://pic4.zhimg.com/9870300bf88a73fd5e3f1fafd89bfe5b.jpg"],"type":0,"id":7767884,"ga_prefix":"012506","title":"瞎扯 · 作死"}]
     * top_stories : [{"image":"http://pic2.zhimg.com/31c936f858e386dcc130c2d8239d4d3d.jpg","type":0,"id":7795305,"ga_prefix":"012515","title":"又想养猫又想养狗？养只狐狸就够了"},{"image":"http://pic2.zhimg.com/f5bf43c842405ffb23db6303d3203039.jpg","type":0,"id":7791671,"ga_prefix":"012507","title":"魏坤琳嘲讽郭敬明那句话，是一种我们很容易忽略的「微暴力」"},{"image":"http://pic4.zhimg.com/052dcde0abaf108c0e2a8df7134e4943.jpg","type":0,"id":7794878,"ga_prefix":"012512","title":"职人介绍所 · 知乎首档视频节目开播"},{"image":"http://pic2.zhimg.com/d5b8ebcb3d0216fb93d00ecd97ee09a1.jpg","type":0,"id":7783012,"ga_prefix":"012507","title":"万年备胎小冥的故事"},{"image":"http://pic4.zhimg.com/e2d88c9f3720f706a6c3f7c3ab5abedb.jpg","type":0,"id":7778552,"ga_prefix":"012511","title":"春节快到了，家里备上这些常用药会比较安心"}]
     */

    private String date;
    /**
     * images : ["http://pic4.zhimg.com/440cef9acb60c21b85e3e04b300bbea3.jpg"]
     * type : 0
     * id : 7795176
     * ga_prefix : 012517
     * title : 自拍不「惊悚」，一盏台灯就能做到
     */

    private List<StoriesEntity> stories;
    /**
     * image : http://pic2.zhimg.com/31c936f858e386dcc130c2d8239d4d3d.jpg
     * type : 0
     * id : 7795305
     * ga_prefix : 012515
     * title : 又想养猫又想养狗？养只狐狸就够了
     */

    private List<TopStoriesEntity> top_stories;


    public void setDate(String date) { this.date = date;}

    public void setStories(List<StoriesEntity> stories) { this.stories = stories;}

    public void setTop_stories(List<TopStoriesEntity> top_stories) { this.top_stories = top_stories;}

    public String getDate() { return date;}

    public List<StoriesEntity> getStories() { return stories;}

    public List<TopStoriesEntity> getTop_stories() { return top_stories;}

    public static class StoriesEntity implements Parcelable {
        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public StoriesEntity() {
        }

        public void setType(int type) { this.type = type;}

        public void setId(int id) { this.id = id;}

        public void setGa_prefix(String ga_prefix) { this.ga_prefix = ga_prefix;}

        public void setTitle(String title) { this.title = title;}

        public void setImages(List<String> images) { this.images = images;}

        public int getType() { return type;}

        public int getId() { return id;}

        public String getGa_prefix() { return ga_prefix;}

        public String getTitle() { return title;}

        public List<String> getImages() { return images;}

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeInt(id);
            dest.writeString(ga_prefix);
            dest.writeStringList(images);
        }

        public static final Parcelable.Creator<StoriesEntity> CREATOR = new Parcelable.Creator<StoriesEntity>(){

            @Override
            public StoriesEntity createFromParcel(Parcel source) {
                return new StoriesEntity(source);
            }

            @Override
            public StoriesEntity[] newArray(int size) {
                return new StoriesEntity[size];
            }
        };

        private StoriesEntity(Parcel in) {
            type = in.readInt();
            id = in.readInt();
            ga_prefix = in.readString();
            title = in.readString();
            in.readStringList(images);
        }
    }

    public static class TopStoriesEntity {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public void setImage(String image) { this.image = image;}

        public void setType(int type) { this.type = type;}

        public void setId(int id) { this.id = id;}

        public void setGa_prefix(String ga_prefix) { this.ga_prefix = ga_prefix;}

        public void setTitle(String title) { this.title = title;}

        public String getImage() { return image;}

        public int getType() { return type;}

        public int getId() { return id;}

        public String getGa_prefix() { return ga_prefix;}

        public String getTitle() { return title;}
    }
}
