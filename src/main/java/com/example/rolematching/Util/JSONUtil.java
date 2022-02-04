package com.example.rolematching.Util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class JSONUtil {
	
	public static <T> String getJsonObject(T object) {
        String resultJson = "";
        try {
            Gson gson = new Gson();
            resultJson = gson.toJson(object );
        }catch (JsonIOException e){
            System.out.println(e.getMessage());
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return resultJson;
    }
    public static Instant convertToInstant(String revisionDate) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        //DateFormat outputFormat = new SimpleDateFormat(finalFormat,Locale.US);
        Date date = inputFormat.parse(revisionDate);
        Instant timeStamp = date.toInstant();
        return timeStamp;
    }
    public static String convertToDateTime(Instant timeInstant) throws ParseException {
        Date myDate = Date.from(timeInstant);
        //DateFormat outputFormat = new SimpleDateFormat(finalFormat,Locale.US);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }




}
