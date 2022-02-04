package com.example.rolematching.model;

import java.util.List;
import java.util.Map;

public class Menu {
    private Map<String, Double> roleMatchingRangeWise;
    private int upperRange=100;
    private int lowerRange=1;
    private int totalRoleMatching;


    public Map<String, Double> getRoleMatchingRangeWise() {
        return roleMatchingRangeWise;
    }

    public void setRoleMatchingRangeWise(Map<String, Double> roleMatchingRangeWise) {
        this.roleMatchingRangeWise = roleMatchingRangeWise;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(int upperRange) {
        this.upperRange = upperRange;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(int lowerRange) {
        this.lowerRange = lowerRange;
    }

    public int getTotalRoleMatching() {
        return totalRoleMatching;
    }

    public void setTotalRoleMatching(int totalRoleMatching) {
        this.totalRoleMatching = totalRoleMatching;
    }
}
