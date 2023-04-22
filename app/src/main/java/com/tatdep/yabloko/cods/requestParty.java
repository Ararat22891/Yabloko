package com.tatdep.yabloko.cods;

import java.io.Serializable;

public class requestParty{
    public String regOtd, mestnOtd, fio, telNumber, sex, dateOfBirth, grazd,
            photoUrl, paspSernumer, datePassp, whoPassp, socSet, subektDom, nasPunktDom,
            streetDom, homeDom, kvartDom, subekt, nasPunkt, street, home, kvart, mestRaboty
            , dolznPoMestuRab, obraz, spec, uchenStepen, workInParty, inayaRabota, uchastVOrgVlast, UchastVIzbCa,
            dopSved;
    public requestParty() {}

    public requestParty(String regOtd, String mestnOtd, String fio, String telNumber,
                        String sex, String dateOfBirth, String grazd, String photoUrl, String paspSernumer,
                        String datePassp, String whoPassp, String socSet, String subektDom, String nasPunktDom,
                        String streetDom, String homeDom, String kvartDom, String subekt, String nasPunkt, String street,
                        String home, String kvart, String mestRaboty, String dolznPoMestuRab, String obraz, String spec,
                        String uchenStepen, String workInParty, String inayaRabota, String uchastVOrgVlast,
                        String uchastVIzbCa, String dopSved) {
        this.regOtd = regOtd;
        this.mestnOtd = mestnOtd;
        this.fio = fio;
        this.telNumber = telNumber;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.grazd = grazd;
        this.photoUrl = photoUrl;
        this.paspSernumer = paspSernumer;
        this.datePassp = datePassp;
        this.whoPassp = whoPassp;
        this.socSet = socSet;
        this.subektDom = subektDom;
        this.nasPunktDom = nasPunktDom;
        this.streetDom = streetDom;
        this.homeDom = homeDom;
        this.kvartDom = kvartDom;
        this.subekt = subekt;
        this.nasPunkt = nasPunkt;
        this.street = street;
        this.home = home;
        this.kvart = kvart;
        this.mestRaboty = mestRaboty;
        this.dolznPoMestuRab = dolznPoMestuRab;
        this.obraz = obraz;
        this.spec = spec;
        this.uchenStepen = uchenStepen;
        this.workInParty = workInParty;
        this.inayaRabota = inayaRabota;
        this.uchastVOrgVlast = uchastVOrgVlast;
        UchastVIzbCa = uchastVIzbCa;
        this.dopSved = dopSved;
    }


    public requestParty(String regOtd, String mestnOtd, String fio, String telNumber, String sex, String dateOfBirth, String grazd) {
        this.regOtd = regOtd;
        this.mestnOtd = mestnOtd;
        this.fio = fio;
        this.telNumber = telNumber;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.grazd = grazd;
    }
}
