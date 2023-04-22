package com.tatdep.yabloko.cods;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email, password, isNew;

    public User() {
    }

    public User(String email, String password, String isNew) {
        this.email = email;
        this.password = password;
        this.isNew = isNew;

    }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("password", password);
        result.put("isNew", isNew);


        return result;
    }
}
