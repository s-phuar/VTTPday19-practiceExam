package VTTPday19.practiceExam.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class dateMethods {

    public static Date strToDate(String date) throws ParseException{
        // Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        Date convertedDate = new SimpleDateFormat("EEE, MM/dd/yyyy").parse(date);    
        return convertedDate;
    }

    public static Date strToDateNewTask(String date) throws ParseException{
        Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        // Date convertedDate = new SimpleDateFormat("EEE, MM/dd/yyyy").parse(date);    
        return convertedDate;
    }

    public static String dateToEpochStr(Date date) throws ParseException{
        if(date != null){
        long epochMilli = date.getTime();
        String epochMilliStr = String.valueOf(epochMilli);
        return epochMilliStr;
        }
        return ""; //allows me to add task without dates set

    }

    public static Date epochStrToDate(String epochMilliStr){
        if(!epochMilliStr.equals("")){
        long epochMilli = Long.valueOf(epochMilliStr);
        Date date = new Date(epochMilli); //date class has a constrcutor that accepts a long to create a Date object         
        return date;
        }
        return null; //allows me to display tasks with no dates set
    }



}
