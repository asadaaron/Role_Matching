package com.example.rolematching.Util;

import com.example.rolematching.model.TimeStampValue;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Processing of json file
 *
 */
public class JsonFileProcessing {
    public static Map<String, String> roleRoleAndTimeStampMapping = new HashMap<>();
    public static Map<String, List<TimeStampValue>> jsonFile() throws FileNotFoundException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(new File("/Users/potsdum digital Health Program/politics.json"));// reading json file
        Reader r = new InputStreamReader(is, "UTF-8");
        Gson gson = new GsonBuilder().create();
        JsonStreamParser p = new JsonStreamParser(r);
        Map<String, List<TimeStampValue>> roleAndTimeStamp = new HashMap<>();

        while (p.hasNext()) {
            JsonElement e = p.next();
            if (e.isJsonObject()) {
                Map m = gson.fromJson(e, Map.class);
                LinkedTreeMap<String, Object> roleList  = (LinkedTreeMap<String, Object>) m.get("positionToRoleLineage");
                roleList.entrySet().stream().forEach( tstampValue -> {
                    Object roleLineage = tstampValue.getValue();
                    String infoboxId = String.valueOf(((LinkedTreeMap) roleLineage).get("id"));
                    String[] splitInfobox = infoboxId.split("\\|\\|");
                    String displayInformation = splitInfobox[0];
                    //String pageId = splitInfobox[1].split("\\|\\|")[0];
                    String pageId = splitInfobox[1];
                    String wikiLink ="";
                    if(splitInfobox.length>3)
                        wikiLink = "https://en.wikipedia.org/?curid=" + pageId+"____"+ splitInfobox[1]+"."+splitInfobox[3];
                    else
                        wikiLink = "https://en.wikipedia.org/?curid=" + pageId+"____"+ splitInfobox[1]+". No Property";
                    LinkedTreeMap<String, Object> lineage  = (LinkedTreeMap<String, Object>) ((LinkedTreeMap) roleLineage).get("roleLineage");
                    //System.out.println("In a single role");
                    LinkedTreeMap<String, Object> timeStampList = (LinkedTreeMap<String, Object>) lineage.get("lineage");
                    List<TimeStampValue> timeStampValues = new ArrayList<TimeStampValue>();
                    timeStampList.entrySet().stream().forEach( tvalue -> {
                        TimeStampValue   timeStampValue = new TimeStampValue();
                        timeStampValue.setTimeStamp(tvalue.getKey().toString());
                        timeStampValue.setValue(tvalue.getValue().toString().trim());
                        try {
                            timeStampValue.setTimeInstant(JSONUtil.convertToInstant(tvalue.getKey().toString()));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        timeStampValues.add(timeStampValue);
                    });
                    timeStampValues.sort(Comparator.comparing(TimeStampValue::getTimeInstant));
                    //roleAndTimeStamp.put(tstampValue.getKey().toString(),timeStampValues);
                    if(displayInformation.contains("infobox")){
                        roleAndTimeStamp.put(wikiLink.toString(),timeStampValues);
                        roleRoleAndTimeStampMapping.put(tstampValue.getKey().toString(),wikiLink);

                    }

                    else{
                        roleAndTimeStamp.put(tstampValue.getKey().toString(),timeStampValues);
                        roleRoleAndTimeStampMapping.put(tstampValue.getKey().toString(),wikiLink);
                    }


                    });
            }
        }
        System.out.println("out of loop");
        return roleAndTimeStamp;

    }
}
