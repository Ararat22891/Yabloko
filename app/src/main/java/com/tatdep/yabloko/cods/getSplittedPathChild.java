package com.tatdep.yabloko.cods;

public class getSplittedPathChild {
    public String getSplittedPathChild(String email){
        email = email.replaceAll("[^A-Za-zA]", "");
        autosave.setDbSetUserTableName(email);
        return   email;
    }
}
