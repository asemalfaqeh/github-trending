package com.af.githubtrends.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatesFrames {

    private static DatesFrames datesFrame = null;

    public static DatesFrames getInstance(){
         if (datesFrame == null){
             datesFrame = new DatesFrames();
         }
         return datesFrame;
    }

    public DatesFrames(){}

    private String dateFormat(String inputDate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return outputFormat.format(date);
        }else {
            return "";
        }
    }

    public String getLastWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date lw = calendar.getTime();
        return dateFormat(lw.toString());
    }

    public String getLastMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date lastMonth = calendar.getTime();
        return dateFormat(lastMonth.toString());
    }

    public String getLastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date lastDay = calendar.getTime();
        return dateFormat(lastDay.toString());
    }

}
