package com.mh.piety.mweather.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PIETY on 2017/5/1.
 */

public class DailyWeather {
    @SerializedName("astro")
    public DailyAstro astro;
    @SerializedName("condition")
    public DailyCond condition;
    @SerializedName("date")
    public String date;
    @SerializedName("damp")
    public String damp;
    @SerializedName("rainfall")
    public String rainfall;
    @SerializedName("pop")
    public String pop;
    @SerializedName("press")
    public String press;
    @SerializedName("temp")
    public DailyTmp temp;
    @SerializedName("uv")
    public String uv;
    @SerializedName("seeing")
    public String seeing;
    @SerializedName("wind")
    public Wind wind;
}
