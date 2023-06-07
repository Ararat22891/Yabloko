package com.tatdep.yabloko.cods;

import java.io.Serializable;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        requestParty that = (requestParty) o;
        return Objects.equals(regOtd, that.regOtd) && Objects.equals(mestnOtd, that.mestnOtd) && Objects.equals(fio, that.fio) && Objects.equals(telNumber, that.telNumber) && Objects.equals(sex, that.sex) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(grazd, that.grazd) && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(paspSernumer, that.paspSernumer) && Objects.equals(datePassp, that.datePassp) && Objects.equals(whoPassp, that.whoPassp) && Objects.equals(socSet, that.socSet) && Objects.equals(subektDom, that.subektDom) && Objects.equals(nasPunktDom, that.nasPunktDom) && Objects.equals(streetDom, that.streetDom) && Objects.equals(homeDom, that.homeDom) && Objects.equals(kvartDom, that.kvartDom) && Objects.equals(subekt, that.subekt) && Objects.equals(nasPunkt, that.nasPunkt) && Objects.equals(street, that.street) && Objects.equals(home, that.home) && Objects.equals(kvart, that.kvart) && Objects.equals(mestRaboty, that.mestRaboty) && Objects.equals(dolznPoMestuRab, that.dolznPoMestuRab) && Objects.equals(obraz, that.obraz) && Objects.equals(spec, that.spec) && Objects.equals(uchenStepen, that.uchenStepen) && Objects.equals(workInParty, that.workInParty) && Objects.equals(inayaRabota, that.inayaRabota) && Objects.equals(uchastVOrgVlast, that.uchastVOrgVlast) && Objects.equals(UchastVIzbCa, that.UchastVIzbCa) && Objects.equals(dopSved, that.dopSved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regOtd, mestnOtd, fio, telNumber, sex, dateOfBirth, grazd, photoUrl, paspSernumer, datePassp, whoPassp, socSet, subektDom, nasPunktDom, streetDom, homeDom, kvartDom, subekt, nasPunkt, street, home, kvart, mestRaboty, dolznPoMestuRab, obraz, spec, uchenStepen, workInParty, inayaRabota, uchastVOrgVlast, UchastVIzbCa, dopSved);
    }
}
