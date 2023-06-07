package com.tatdep.yabloko.cods;

import java.util.Objects;

public class Accounts_Data {
    public String polit_pred, regilgia, mainInLife, mainInMan, vdox, surname, name, patronomyc,telNumber, region,age, dolz, profileIcon;


    public Accounts_Data() {
    }

    public Accounts_Data(String polit_pred, String regilgia, String mainInLife, String mainInMan, String vdox, String surname, String name, String patronomyc, String telNumber, String region, String age, String dolz, String profileIcon) {
        this.polit_pred = polit_pred;
        this.regilgia = regilgia;
        this.mainInLife = mainInLife;
        this.mainInMan = mainInMan;
        this.vdox = vdox;
        this.surname = surname;
        this.name = name;
        this.profileIcon = profileIcon;
        this.patronomyc = patronomyc;
        this.telNumber = telNumber;
        this.region = region;
        this.age = age;
        this.dolz = dolz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accounts_Data that = (Accounts_Data) o;
        return Objects.equals(polit_pred, that.polit_pred) &&
                Objects.equals(regilgia, that.regilgia) &&
                Objects.equals(mainInLife, that.mainInLife) &&
                Objects.equals(mainInMan, that.mainInMan) &&
                Objects.equals(vdox, that.vdox) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(name, that.name) &&
                Objects.equals(patronomyc, that.patronomyc) &&
                Objects.equals(telNumber, that.telNumber) &&
                Objects.equals(region, that.region) &&
                Objects.equals(age, that.age) &&
                Objects.equals(dolz, that.dolz) &&
                Objects.equals(profileIcon, that.profileIcon);
    }

}
