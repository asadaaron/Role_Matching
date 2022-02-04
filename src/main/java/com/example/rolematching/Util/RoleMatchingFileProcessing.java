package com.example.rolematching.Util;

import com.example.rolematching.model.RoleMatching;
import com.example.rolematching.model.TimeStampValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RoleMatchingFileProcessing {
    Map<String,RoleMatching> roleMatchingMap = new HashMap<>();
    public Map<String, RoleMatching> processRoleMatchingFile() throws FileNotFoundException, UnsupportedEncodingException {
        File directoryPath = new File("/Users/potsdum digital Health Program/HPI JOBS/12_11_2021/san2/data/change-exploration/roleMerging/optimization/GreedyMerges/politics");
        String files[] = directoryPath.list();
        for(String file:files){
            InputStream is = new FileInputStream(new File("/Users/potsdum digital Health Program/HPI JOBS/12_11_2021/san2/data/change-exploration/roleMerging/optimization/GreedyMerges/politics/"+file));
            Reader r = new InputStreamReader(is, "UTF-8");
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(r);
            Map<String, List<TimeStampValue>> roleAndTimeStamp = new HashMap<>();
            while (p.hasNext()) {
                JsonElement e = p.next();
                if (e.isJsonObject()) {
                        Map m = gson.fromJson(e, Map.class);
                   List<Object> roleList = (List<Object> ) m.get("clique");
                    String cliqueScore =  m.get("cliqueScore").toString();
                    RoleMatching  roleMatching = new RoleMatching();
                    roleMatching.setCliqueScore(cliqueScore.toString());
                    List<Integer> roleListTemp = new ArrayList<>();
                    roleList.stream().forEach(role -> {
                        Double roleTemp = (Double) role;
                        roleListTemp.add(roleTemp.intValue());
                    });

                    roleMatching.setRoleList(roleListTemp);
                    //roleMatching.getRoleList().stream().map(Integer::parseInt).collect(Collectors.toList()).sort(Comparator.naturalOrder());
                    roleMatching.getRoleList().sort(Comparator.naturalOrder());
                    //roleMatching.getRoleList().stream().map(Integer::parseInt).collect(Collectors.toList());
                    if(!roleMatching.getRoleList().isEmpty())
                        roleMatching.setClique(roleMatching.getRoleList().get(0).toString());
                    roleMatchingMap.put(roleMatching.getClique(),roleMatching);
                }

            }
        }
        return roleMatchingMap;

    }
}
