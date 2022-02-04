package com.example.rolematching.model;

import java.util.List;

public class RoleMatching {
    private String clique;
    private List<Integer> roleList;
    private String cliqueScore;

    public String getClique() {
        return clique;
    }

    public void setClique(String clique) {
        this.clique = clique;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }

    public String getCliqueScore() {
        return cliqueScore;
    }

    public void setCliqueScore(String cliqueScore) {
        this.cliqueScore = cliqueScore;
    }

    @Override
    public String toString() {
        return "RoleMatching{" +
                "clique='" + clique + '\'' +
                ", roleId=" + roleList +
                ", cliqueScore='" + cliqueScore + '\'' +
                '}';
    }
}
