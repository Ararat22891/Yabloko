package com.tatdep.yabloko.cods;

public class getNormData {
    public static String getDate(String usualDate){
        String normalDate;
        String[] del = usualDate.split(" ");

        switch (del[2]){
            case("Dec"):
                del[2] = "декабря";
                break;
            case ("Jan"):
                del[2] = "января";
                break;
            case("Feb"):
                del[2] = "февраля";
                break;
            case ("Mar"):
                del[2] = "марта";
                break;
            case("Apr"):
                del[2] = "апреля";
                break;
            case ("Jun"):
                del[2] = "июня";
                break;
            case("Jul"):
                del[2] = "июля";
                break;
            case ("Aug"):
                del[2] = "августа";
                break;
            case ("Sep"):
                del[2] = "сентября";
                break;
            case("Oct"):
                del[2] = "октября";
                break;
            case ("Nov"):
                del[2] = "ноября";
                break;
        }



        normalDate = del[1]+" "+del[2] + " в "+ del[4];
        return normalDate;
    }
}
