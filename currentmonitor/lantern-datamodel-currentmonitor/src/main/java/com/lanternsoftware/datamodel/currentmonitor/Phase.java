package com.lanternsoftware.datamodel.currentmonitor;

public class Phase {
    int id;
    String name;
    int phaseOffsetNs;

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public int getPhaseOffsetNs() {
        return phaseOffsetNs;
    }

    public void setPhaseOffsetNs(int _phaseOffsetNs) {
        phaseOffsetNs = _phaseOffsetNs;
    }
}
