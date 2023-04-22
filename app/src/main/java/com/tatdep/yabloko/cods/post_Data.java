package com.tatdep.yabloko.cods;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class post_Data {
    public String photoUrl, text, dateOfPubl, userId;
    public String quantityLike;
    public Long tm;

    public post_Data() {
    }

    public post_Data(String photoUrl, String text, String dateOfPubl, String userId, String quantityLike, Long tm) {
        this.photoUrl = photoUrl;
        this.text = text;
        this.dateOfPubl = dateOfPubl;
        this.userId = userId;
        this.quantityLike = quantityLike;
        this.tm = tm;

    }
}
