package kr.chirokyel.lifeTool;

import java.util.ArrayList;

public class _2_Weather_4_Object {


    // 년월일시
    String string_date;

    public String getString_date() {
        return string_date;
    }
    public void setString_date(String string_date) {
        this.string_date = string_date;
    }

    // 위도경도
    String latitude;
    String longitude;

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }



    // 행정주소
    String location1;
    String location2;
    String location3;

    public String getLocation1() {
        return location1;
    }
    public void setLocation1(String location1) {
        this.location1 = location1;
    }
    public String getLocation2() {
        return location2;
    }
    public void setLocation2(String location2) {
        this.location2 = location2;
    }
    public String getLocation3() {
        return location3;
    }
    public void setLocation3(String location3) {
        this.location3 = location3;
    }



    // 날씨온도
    ArrayList<_2_Weather_1_Form> baseLIst = new ArrayList<>();
    String maxTemp; // maximum temperature
    String minTemp; // minimum temperature

    public ArrayList<_2_Weather_1_Form> getBaseLIst() {
        return baseLIst;
    }
    public void setBaseLIst(ArrayList<_2_Weather_1_Form> baseLIst) {
        this.baseLIst = baseLIst;
    }
    public String getMaxTemp() {
        return maxTemp;
    }
    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }
    public String getMinTemp() {
        return minTemp;
    }
    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }



    // 미세먼지
    String dust10;
    String dust25;

    public String getDust10() {
        return dust10;
    }
    public void setDust10(String dust10) {
        this.dust10 = dust10;
    }
    public String getDust25() {
        return dust25;
    }
    public void setDust25(String dust25) {
        this.dust25 = dust25;
    }



    // 일몰일출
    String sunRise;
    String sunSet;

    public String getSunRise() {
        return sunRise;
    }
    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }
    public String getSunSet() {
        return sunSet;
    }
    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

}
