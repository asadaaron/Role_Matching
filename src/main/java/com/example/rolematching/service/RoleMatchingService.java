package com.example.rolematching.service;

import com.example.rolematching.Util.JSONUtil;
import com.example.rolematching.Util.JsonFileProcessing;
import com.example.rolematching.Util.RoleMatchingFileProcessing;
import com.example.rolematching.model.Menu;
import com.example.rolematching.model.RoleMatching;
import com.example.rolematching.model.TableDataOfRoleMatching;
import com.example.rolematching.model.TimeStampValue;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Service
public class RoleMatchingService {
    Map<String, List<TimeStampValue>> roleAndTimeStamp = new HashMap<String, List<TimeStampValue>>();
    Map<String, RoleMatching> roleMatchingMap = new HashMap<>();
    public String menuSample() throws FileNotFoundException, UnsupportedEncodingException {
        RoleMatchingFileProcessing roleMatchingFileProcessing = new RoleMatchingFileProcessing();
        roleMatchingMap = roleMatchingFileProcessing.processRoleMatchingFile();
        Map <String, Double> roleMatchingSortedMap = new HashMap<>();
        int count = 0;
        for (Map.Entry<String, RoleMatching> pair : roleMatchingMap.entrySet()){
            //iterate over the pairs
            roleMatchingSortedMap.put(pair.getValue().getRoleList().size()+"_RM_"+ pair.getKey(),Double.valueOf(pair.getValue().getCliqueScore()));
        }
        roleMatchingSortedMap = JSONUtil.sortByValue(roleMatchingSortedMap);
        Map <String, Double> roleMatchingRangeWise = new HashMap<>();
        for (Map.Entry<String, Double> pair : roleMatchingSortedMap.entrySet()){
            //iterate over the pairs
            if(count == 101)
                break;
            roleMatchingRangeWise.put(pair.getKey(),pair.getValue());
            count ++;
        }
        roleMatchingRangeWise = JSONUtil.sortByValue(roleMatchingRangeWise);
        Menu menus = new Menu();
        menus.setLowerRange(1);
        menus.setUpperRange(100);
        menus.setRoleMatchingRangeWise(roleMatchingRangeWise);
        menus.setTotalRoleMatching(roleMatchingSortedMap.size());
        return JSONUtil.getJsonObject(menus);
    }
    public String rangeWiseMenuList(String range){
        List<String> menuList = new ArrayList<String>();
        int lowerRange = Integer.valueOf(range);


        Map <String, Double> roleMatchingSortedMap = new HashMap<>();
        int count = 0;
        for (Map.Entry<String, RoleMatching> pair : roleMatchingMap.entrySet()){
            //iterate over the pairs
            roleMatchingSortedMap.put(pair.getValue().getRoleList().size()+"_RM_"+ pair.getKey(),Double.valueOf(pair.getValue().getCliqueScore()));
        }
        roleMatchingSortedMap = JSONUtil.sortByValue(roleMatchingSortedMap);
        Map <String, Double> roleMatchingRangeWise = new HashMap<>();
        for (Map.Entry<String, Double> pair : roleMatchingSortedMap.entrySet()){
            //iterate over the pairs
            if(count>= lowerRange && count <= lowerRange+100)
                roleMatchingRangeWise.put(pair.getKey(),pair.getValue());
            if(count> lowerRange +100)
                break;

            count ++;
        }
        roleMatchingRangeWise = JSONUtil.sortByValue(roleMatchingRangeWise);
        Menu menus = new Menu();
        if(lowerRange == 0)
            menus.setLowerRange(1);
        else
            menus.setLowerRange(lowerRange);
        menus.setUpperRange(lowerRange+100);
        menus.setRoleMatchingRangeWise(roleMatchingRangeWise);
        menus.setTotalRoleMatching(roleMatchingSortedMap.size());
        return JSONUtil.getJsonObject(menus);


    }
    public String searchVariable(String sizeMax,String sizeMin, String scoreMin,String scoreMax,String is_all,String searchString,String propertyOrRole,String percentageOfRole,String atleastOne) throws FileNotFoundException, UnsupportedEncodingException {
        Map <String, Double> roleMatchingRangeWise = new HashMap<>();
        Map<String, RoleMatching> roleMatchingBySize = new HashMap<>();
        Map<String, RoleMatching> roleMatchingByScore = new HashMap<>();
        JsonFileProcessing jsonFileProcessing = new JsonFileProcessing();

        if(roleAndTimeStamp.isEmpty()) // map which contains all of the table data for each role, key is wiki link and value is data
            roleAndTimeStamp = JsonFileProcessing.jsonFile(); // if the table data map is empty it will generate the data


        if(!propertyOrRole.isEmpty() && !propertyOrRole.equals("NAN")) {
            if (!sizeMax.isEmpty() && !sizeMin.isEmpty()) {
                for (Map.Entry<String, RoleMatching> roleMatchingEntry : roleMatchingMap.entrySet()) {
                    if (roleMatchingEntry.getValue().getRoleList().size() > Integer.valueOf(sizeMin) && roleMatchingEntry.getValue().getRoleList().size() < Integer.valueOf(sizeMax)) {
                        int roleMatchingRoleSize = roleMatchingEntry.getValue().getRoleList().size();
                        int counter = 0;
                        boolean flag = false;
                        for(Integer roleId: roleMatchingEntry.getValue().getRoleList()){ // for each role find the corresponding key name in roleAndTimeStamp
                            String wikiLink = jsonFileProcessing.roleRoleAndTimeStampMapping.get(roleId.toString()); // key name of the roleAndTimeStamp map
                            //List<TimeStampValue>  timeStampValues = roleAndTimeStamp.get(wikiLink); // find out the value for the role to fill the table
                            //randomlySelectedTimeAndValue.put(wikiLink,timeStampValues);
                            String property = wikiLink.split("____")[1];
                            if(property.split("\\.")[1].contains(propertyOrRole) || roleId.toString().contains(propertyOrRole)){// full text search 
                                counter ++;
                                flag = true;
                                //roleMatchingBySize.put(roleMatchingEntry.getKey(), roleMatchingEntry.getValue());
                            }


                        }
                        double percentageCounter = (double)counter/(double)roleMatchingRoleSize * 100;
                        if(is_all.equals("true") ){
                            if(roleMatchingRoleSize==counter){
                                roleMatchingBySize.put(roleMatchingEntry.getKey(), roleMatchingEntry.getValue());
                            }
                        }else if (flag && atleastOne.equals("true") ){
                            roleMatchingBySize.put(roleMatchingEntry.getKey(), roleMatchingEntry.getValue());
                        }else if(flag && percentageCounter  >=  Double.valueOf(percentageOfRole) ){
                            roleMatchingBySize.put(roleMatchingEntry.getKey(), roleMatchingEntry.getValue());
                        }

                    }
                }
            }
        }else{
            if (!sizeMax.isEmpty() && !sizeMin.isEmpty()) {
                for (Map.Entry<String, RoleMatching> roleMatchingEntry : roleMatchingMap.entrySet()) {
                    if (roleMatchingEntry.getValue().getRoleList().size() >= Integer.valueOf(sizeMin) && roleMatchingEntry.getValue().getRoleList().size() <= Integer.valueOf(sizeMax)) {
                        roleMatchingBySize.put(roleMatchingEntry.getKey(), roleMatchingEntry.getValue());
                    }
                }
            }
        }
        if(!scoreMin.isEmpty() && !scoreMax.isEmpty()) {
            for (Map.Entry<String, RoleMatching> roleMatchingEntry : roleMatchingBySize.entrySet()) {
                if (Double.valueOf(roleMatchingEntry.getValue().getCliqueScore()) >= Double.valueOf(scoreMin) && Double.valueOf(roleMatchingEntry.getValue().getCliqueScore()) <= Double.valueOf(scoreMax)) {
                    roleMatchingByScore.put(roleMatchingEntry.getKey(),roleMatchingEntry.getValue());
                }
            }
        }

        if(!Objects.isNull(roleMatchingByScore) && searchString.equals("NAN")) {
            for (Map.Entry<String, RoleMatching> roleMatchingMapForSearch : roleMatchingByScore.entrySet()) {
                roleMatchingRangeWise.put(roleMatchingMapForSearch.getValue().getRoleList().size() + "_RM_" + roleMatchingMapForSearch.getKey().toString(), Double.valueOf(roleMatchingMapForSearch.getValue().getCliqueScore()));
            }
        }
        if(!searchString.isEmpty() && !searchString.equals("NAN")) {
            RoleMatching roleMatchingMapForSearch = roleMatchingMap.get(searchString);
            //for (Map.Entry<String, RoleMatching> roleMatchingMapForSearch : roleMatchingMapSearchString.entrySet()) {
                roleMatchingRangeWise.put(roleMatchingMapForSearch.getRoleList().size() + "_RM_" + searchString, Double.valueOf(roleMatchingMapForSearch.getCliqueScore()));
           // }
        }
        // For pagination
        roleMatchingRangeWise = JSONUtil.sortByValue(roleMatchingRangeWise);
        Menu menus = new Menu();
        menus.setLowerRange(1);
        menus.setUpperRange(roleMatchingRangeWise.size());
        menus.setRoleMatchingRangeWise(roleMatchingRangeWise);
        menus.setTotalRoleMatching(roleMatchingRangeWise.size());

       // return JSONUtil.getJsonObject(JSONUtil.sortByValue(roleMatchingRangeWise));
        return JSONUtil.getJsonObject(menus);
    }


    public String searchContent(String content,String percentageOfRoles) throws FileNotFoundException, UnsupportedEncodingException {
        JsonFileProcessing jsonFileProcessing = new JsonFileProcessing();
        Map <String, Double> roleMatchingRangeWise = new HashMap<>();
        Map<String, RoleMatching> roleMatchingByContent = new HashMap<>();
        if(roleAndTimeStamp.isEmpty()) // map which contains all of the table data for each role, key is wiki link and value is data
            roleAndTimeStamp = JsonFileProcessing.jsonFile(); // if the table data map is empty it will generate the data
        if(!content.isEmpty() && !content.equals("NAN")) {
            int counter = 0;
            for (Map.Entry<String, RoleMatching> roleMatchingEntry : roleMatchingMap.entrySet()) {
                /*for(Integer roleId: roleMatchingEntry.getValue().getRoleList()) { // for each role find the corresponding key name in roleAndTimeStamp
                    String wikiLink = jsonFileProcessing.roleRoleAndTimeStampMapping.get(roleId.toString()); // key name of the roleAndTimeStamp map
                    for (Map.Entry<String,List<TimeStampValue>> iterRoleTimeStamp: roleAndTimeStamp.entrySet()){
                        for(TimeStampValue timeStampValue: iterRoleTimeStamp.getValue() ){
                            if(timeStampValue.getValue() )
                        }
                    }
                }*/
                double percentage = 0;

                Map<String, List<TableDataOfRoleMatching>> tableData = tableRoleData(roleMatchingEntry.getKey());
                int dataSize = tableData.size();
                int roleCounter = 0;
                for(Map.Entry<String,List<TableDataOfRoleMatching>> iterateTableData : tableData.entrySet()){
                    int dataCounter = 0;
                    for(TableDataOfRoleMatching tableDataOfRoleMatching: iterateTableData.getValue()){
                        if(tableDataOfRoleMatching.getValue().toLowerCase(Locale.ROOT).contains((content.toLowerCase(Locale.ROOT)))){
                            dataCounter++;
                            //System.out.println(tableDataOfRoleMatching.getValue());
                        }
                    }

                    if(dataCounter>0)
                        roleCounter++;//
                }
                if(dataSize > 0 ){
                    percentage = ((double)roleCounter/(double)dataSize) * 100;
                    if(roleCounter>0)
                        System.out.println("roleCounter: "+roleCounter+"data size: "+dataSize+" percentage of role matching: "+percentage);
                }
                if(percentage >= Double.valueOf(percentageOfRoles))
                    //roleMatchingByContent.put(roleMatchingEntry.getKey(),roleMatchingEntry.getValue());
                    roleMatchingByContent.put(roleMatchingEntry.getKey()+"( "+percentage + "%)",roleMatchingEntry.getValue());


            }
        }
        if(!Objects.isNull(roleMatchingByContent))
            for (Map.Entry<String, RoleMatching> roleMatchingMapForSearch : roleMatchingByContent.entrySet()) {
                roleMatchingRangeWise.put(roleMatchingMapForSearch.getValue().getRoleList().size()+"_RM_"+roleMatchingMapForSearch.getKey().toString(),Double.valueOf(roleMatchingMapForSearch.getValue().getCliqueScore()));
            }
        roleMatchingRangeWise = JSONUtil.sortByValue(roleMatchingRangeWise);
        Menu menus = new Menu();
        if(roleMatchingRangeWise.size()>0)
            menus.setLowerRange(1);
        else
            menus.setLowerRange(0);
        menus.setUpperRange(roleMatchingRangeWise.size());
        menus.setRoleMatchingRangeWise(roleMatchingRangeWise);
        menus.setTotalRoleMatching(roleMatchingRangeWise.size());

        // return JSONUtil.getJsonObject(JSONUtil.sortByValue(roleMatchingRangeWise));
        return JSONUtil.getJsonObject(menus);

    }
    public Map<String, List<TableDataOfRoleMatching>> tableRoleData(String roleMatchingName){
        JsonFileProcessing jsonFileProcessing = new JsonFileProcessing();
        List<Instant> totalTimeStampList = new ArrayList<>();
        Map<String, List<TableDataOfRoleMatching>> checkingValue = new HashMap<>();  //final output map
        Map<String, List<TimeStampValue>> randomlySelectedTimeAndValue = new HashMap<>(); // temporary table collection data
        try{
            if(roleAndTimeStamp.isEmpty()) // map which contains all of the table data for each role, key is wiki link and value is data
                roleAndTimeStamp = JsonFileProcessing.jsonFile(); // if the table data map is empty it will generate the data
            RoleMatching roleMatchingForSearch = roleMatchingMap.get(roleMatchingName); //roleMatchingMap contains the key as mathcing name and value as the list of role
            for(Integer roleId: roleMatchingForSearch.getRoleList()){ // for each role find the corresponding key name in roleAndTimeStamp
                String wikiLink = jsonFileProcessing.roleRoleAndTimeStampMapping.get(roleId.toString()); // key name of the roleAndTimeStamp map
                List<TimeStampValue>  timeStampValues = roleAndTimeStamp.get(wikiLink); // find out the value for the role to fill the table
                randomlySelectedTimeAndValue.put(wikiLink,timeStampValues);
            }

            for (Map.Entry<String,List<TimeStampValue>> tStamp : randomlySelectedTimeAndValue.entrySet()){
                //iterate over the pairs
                for (TimeStampValue tClassValue : tStamp.getValue()) {
                    if(!totalTimeStampList.contains(tClassValue.getTimeInstant())){
                        totalTimeStampList.add(tClassValue.getTimeInstant());
                    }

                }
                //System.out.println(pair.getKey()+" "+pair.getValue());
            }
            totalTimeStampList.sort(Comparator.naturalOrder());
            //timeStampValues.sort(Comparator.comparing(TimeStampValue::getTimeInstant));
// for each timestamp check whether the value exist or not. Work from here
            for(Instant timetracker: totalTimeStampList){

                for (Map.Entry<String,List<TimeStampValue>> tStamp : randomlySelectedTimeAndValue.entrySet()){
                    TimeStampValue backUp = null;
                    if(checkingValue.get(tStamp.getKey())== null){
                        List<TableDataOfRoleMatching> timeStampList = new ArrayList<>();
                        checkingValue.put(tStamp.getKey(),timeStampList);
                    }

                    TimeStampValue valueOnTimeStamp = getStateAt(timetracker,tStamp.getValue());
                    if(valueOnTimeStamp!= null){
                        if(valueOnTimeStamp.getValue().isEmpty()){
                            System.out.println(tStamp.getKey()+"-----"+valueOnTimeStamp.getTimeStamp());
                        }
                        TableDataOfRoleMatching tableDataOfRoleMatching = new TableDataOfRoleMatching();
                        //tableDataOfRoleMatching.setTimeStamp(valueOnTimeStamp.getTimeStamp());
                        tableDataOfRoleMatching.setTimeStamp(JSONUtil.convertToDateTime(timetracker));

                        if(valueOnTimeStamp.getValue().equals("\u200c⊥R\u200c")  || valueOnTimeStamp.getValue().equals("\u200c⊥C\u200c") ||
                                valueOnTimeStamp.getValue().equals("\u200c⊥D\u200c") || valueOnTimeStamp.getValue().equals("\u200c⊥CE\u200c")||
                                valueOnTimeStamp.getValue().equals("\u200c⊥V\u200c") || valueOnTimeStamp.getValue().trim().isEmpty())
                            tableDataOfRoleMatching.setValue("_");
                        else
                            tableDataOfRoleMatching.setValue(valueOnTimeStamp.getValue());

                        //System.out.println(valueOnTimeStamp.getValue());
                        List<TableDataOfRoleMatching> tempTimeStampList =  checkingValue.get(tStamp.getKey());
                        tempTimeStampList.add(tableDataOfRoleMatching);
                        checkingValue.put(tStamp.getKey(),tempTimeStampList);
                    }

                }
            }
            /*check column which does not match each other*/
            Map.Entry<String,List<TableDataOfRoleMatching>> entry = checkingValue.entrySet().iterator().next();
            int  mapListSize = entry.getValue().size();
            for(int i=0;i< mapListSize; i++){
                int counter = 0;
                boolean firstElement = true;
                String backupContent = "";
                List<TableDataOfRoleMatching> firstMapList = new ArrayList<>();
                String firstListKey = "";
                for (Map.Entry<String,List<TableDataOfRoleMatching>> tableColumnChecking : checkingValue.entrySet()){
                    if(counter == 0){
                        firstMapList = tableColumnChecking.getValue();
                        firstListKey =  tableColumnChecking.getKey();
                        counter++;

                    }
                    if(firstElement && !tableColumnChecking.getValue().get(i).getValue().equals("_")){
                        backupContent= tableColumnChecking.getValue().get(i).getValue();
                        firstElement = false;

                        continue;
                    }
                    if(!backupContent.equals(tableColumnChecking.getValue().get(i).getValue()) && !tableColumnChecking.getValue().get(i).getValue().equals("_") ){
                        //tableColumnChecking.getValue().get(0).setTimeStamp(tableColumnChecking.getValue().get(0).getTimeStamp()+"!");
                        //List <TableDataOfRoleMatching> updatedHeaderList = tableColumnChecking.getValue();
                        TableDataOfRoleMatching tableDataOfRoleMatching = new TableDataOfRoleMatching();
                        tableDataOfRoleMatching.setValue(firstMapList.get(i).getValue());
                        tableDataOfRoleMatching.setTimeStamp(firstMapList.get(i).getTimeStamp() + "!");
                        firstMapList.set(i,tableDataOfRoleMatching);
                        checkingValue.put(firstListKey, firstMapList);
                        //System.out.println(firstListKey + " -- " +  firstMapList.get(0).getTimeStamp() + "!");
                        break;
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        //return JSONUtil.getJsonObject(checkingValue);
        return checkingValue;

    }
    public String toJsonString(String roleMatchingName){

       return JSONUtil.getJsonObject(tableRoleData(roleMatchingName));
    }


    public static TimeStampValue getStateAt(Instant timeStamp,  List<TimeStampValue> timeStampValues) throws ParseException {
        TimeStampValue backupTimeStampValue= null;
        //Collections.sort(pairwiseColumnModelList);
        for(TimeStampValue timeStampValue :timeStampValues){
            if(timeStampValue.getTimeInstant().equals(timeStamp)){
                return timeStampValue;
            }else if(timeStampValue.getTimeInstant().isBefore(timeStamp)){
                backupTimeStampValue = timeStampValue;
            }
        }
        return backupTimeStampValue;
    }


    // find mismatch role matching Id

    public boolean findMismatchRoleMatchingId(String roleMatchingName){
        JsonFileProcessing jsonFileProcessing = new JsonFileProcessing();
        List<Instant> totalTimeStampList = new ArrayList<>();
        Map<String, List<TableDataOfRoleMatching>> checkingValue = new HashMap<>();  //final output map
        Map<String, List<TimeStampValue>> randomlySelectedTimeAndValue = new HashMap<>(); // temporary table collection data
        boolean misMatchRoleMatching = false;
        try{
            if(roleAndTimeStamp.isEmpty()) // map which contains all of the table data for each role, key is wiki link and value is data
                roleAndTimeStamp = JsonFileProcessing.jsonFile(); // if the table data map is empty it will generate the data
            RoleMatching roleMatchingForSearch = roleMatchingMap.get(roleMatchingName); //roleMatchingMap contains the key as mathcing name and value as the list of role
            for(Integer roleId: roleMatchingForSearch.getRoleList()){ // for each role find the corresponding key name in roleAndTimeStamp
                String wikiLink = jsonFileProcessing.roleRoleAndTimeStampMapping.get(roleId.toString()); // key name of the roleAndTimeStamp map
                List<TimeStampValue>  timeStampValues = roleAndTimeStamp.get(wikiLink); // find out the value for the role to fill the table
                randomlySelectedTimeAndValue.put(wikiLink,timeStampValues);
            }

            for (Map.Entry<String,List<TimeStampValue>> tStamp : randomlySelectedTimeAndValue.entrySet()){
                //iterate over the pairs
                for (TimeStampValue tClassValue : tStamp.getValue()) {
                    if(!totalTimeStampList.contains(tClassValue.getTimeInstant())){
                        totalTimeStampList.add(tClassValue.getTimeInstant());
                    }

                }
                //System.out.println(pair.getKey()+" "+pair.getValue());
            }
            totalTimeStampList.sort(Comparator.naturalOrder());
            //timeStampValues.sort(Comparator.comparing(TimeStampValue::getTimeInstant));
// for each timestamp check whether the value exist or not. Work from here
            for(Instant timetracker: totalTimeStampList){

                for (Map.Entry<String,List<TimeStampValue>> tStamp : randomlySelectedTimeAndValue.entrySet()){
                    TimeStampValue backUp = null;
                    if(checkingValue.get(tStamp.getKey())== null){
                        List<TableDataOfRoleMatching> timeStampList = new ArrayList<>();
                        checkingValue.put(tStamp.getKey(),timeStampList);
                    }

                    TimeStampValue valueOnTimeStamp = getStateAt(timetracker,tStamp.getValue());
                    if(valueOnTimeStamp!= null){
                        if(valueOnTimeStamp.getValue().isEmpty()){
                            System.out.println(tStamp.getKey()+"-----"+valueOnTimeStamp.getTimeStamp());
                        }
                        TableDataOfRoleMatching tableDataOfRoleMatching = new TableDataOfRoleMatching();
                        //tableDataOfRoleMatching.setTimeStamp(valueOnTimeStamp.getTimeStamp());
                        tableDataOfRoleMatching.setTimeStamp(JSONUtil.convertToDateTime(timetracker));

                        if(valueOnTimeStamp.getValue().equals("\u200c⊥R\u200c")  || valueOnTimeStamp.getValue().equals("\u200c⊥C\u200c") ||
                                valueOnTimeStamp.getValue().equals("\u200c⊥D\u200c") || valueOnTimeStamp.getValue().equals("\u200c⊥CE\u200c")||
                                valueOnTimeStamp.getValue().equals("\u200c⊥V\u200c") || valueOnTimeStamp.getValue().trim().isEmpty())
                            tableDataOfRoleMatching.setValue("_");
                        else
                            tableDataOfRoleMatching.setValue(valueOnTimeStamp.getValue());

                        //System.out.println(valueOnTimeStamp.getValue());
                        List<TableDataOfRoleMatching> tempTimeStampList =  checkingValue.get(tStamp.getKey());
                        tempTimeStampList.add(tableDataOfRoleMatching);
                        checkingValue.put(tStamp.getKey(),tempTimeStampList);
                    }

                }
            }
            /*check column which does not match each other*/
            Map.Entry<String,List<TableDataOfRoleMatching>> entry = checkingValue.entrySet().iterator().next();
            int  mapListSize = entry.getValue().size();
            for(int i=0;i< mapListSize; i++){
                int counter = 0;
                boolean firstElement = true;
                String backupContent = "";
                List<TableDataOfRoleMatching> firstMapList = new ArrayList<>();
                String firstListKey = "";
                for (Map.Entry<String,List<TableDataOfRoleMatching>> tableColumnChecking : checkingValue.entrySet()){
                    if(counter == 0){
                        firstMapList = tableColumnChecking.getValue();
                        firstListKey =  tableColumnChecking.getKey();
                        counter++;

                    }
                    if(firstElement && !tableColumnChecking.getValue().get(i).getValue().equals("_")){
                        backupContent= tableColumnChecking.getValue().get(i).getValue();
                        firstElement = false;

                        continue;
                    }
                    if(!backupContent.equals(tableColumnChecking.getValue().get(i).getValue()) && !tableColumnChecking.getValue().get(i).getValue().equals("_") ){
                        //tableColumnChecking.getValue().get(0).setTimeStamp(tableColumnChecking.getValue().get(0).getTimeStamp()+"!");
                        //List <TableDataOfRoleMatching> updatedHeaderList = tableColumnChecking.getValue();
                        TableDataOfRoleMatching tableDataOfRoleMatching = new TableDataOfRoleMatching();
                        tableDataOfRoleMatching.setValue(firstMapList.get(i).getValue());
                        tableDataOfRoleMatching.setTimeStamp(firstMapList.get(i).getTimeStamp() + "!");
                        firstMapList.set(i,tableDataOfRoleMatching);
                        checkingValue.put(firstListKey, firstMapList);
                        misMatchRoleMatching = true;
                        //System.out.println(firstListKey + " -- " +  firstMapList.get(0).getTimeStamp() + "!");
                        break;
                    }
                    if(misMatchRoleMatching)
                        break;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        //return JSONUtil.getJsonObject(checkingValue);
        return misMatchRoleMatching;

    }


    public String findMismatchRoleMatchigString(){
        Map <String, Double> roleMatchingRangeWise = new HashMap<>();
        for (Map.Entry<String, RoleMatching> roleMatchingEntry : roleMatchingMap.entrySet()) {
            if(findMismatchRoleMatchingId(roleMatchingEntry.getKey())){
                //if(!Objects.isNull(roleMatchingByContent))
                    //for (Map.Entry<String, RoleMatching> roleMatchingMapForSearch : roleMatchingByContent.entrySet()) {
                        roleMatchingRangeWise.put(roleMatchingEntry.getValue().getRoleList().size()+"_RM_"+roleMatchingEntry.getKey().toString(),Double.valueOf(roleMatchingEntry.getValue().getCliqueScore()));
                    //}


            }
        }

        roleMatchingRangeWise = JSONUtil.sortByValue(roleMatchingRangeWise);
        Menu menus = new Menu();
        if(roleMatchingRangeWise.size()>0)
            menus.setLowerRange(1);
        else
            menus.setLowerRange(0);
        menus.setUpperRange(roleMatchingRangeWise.size());
        menus.setRoleMatchingRangeWise(roleMatchingRangeWise);
        menus.setTotalRoleMatching(roleMatchingRangeWise.size());

        // return JSONUtil.getJsonObject(JSONUtil.sortByValue(roleMatchingRangeWise));
        return JSONUtil.getJsonObject(menus);
    }



}
