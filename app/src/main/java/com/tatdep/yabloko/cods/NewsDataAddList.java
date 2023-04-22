package com.tatdep.yabloko.cods;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import java.util.Date;

public class NewsDataAddList {
    private String nameOfUser;
    private String textOfUserPost;

    private  String likesQuantity;
    private  int commentsQuantity;
    private String url;
    private String date;
    private  Long tm;

    public NewsDataAddList(String nameOfUser, String textOfUserPost, String url, String likesQuantity,  String date, Long tm ) {
        this.nameOfUser = nameOfUser;
        this.textOfUserPost = textOfUserPost;
        this.url = url;
        this.likesQuantity = likesQuantity;
        this.date = date;
        this.tm = tm;
    }



    public String getNameOfUser(){
        return nameOfUser;
    }

    public String getTextOfUserPost(){
        return  textOfUserPost;
    }


    public  String getAvatarsPhoto(){
        return  url;
    }

    public String getLikesQuantity(){
        return likesQuantity;
    }

    public int getCommentsQuantity(){
        return commentsQuantity;
    }

    public String getDate(){return date;}

    public Long getTm(){return tm;}


}
