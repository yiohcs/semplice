package com.yschoi.demo.model;

import org.json.JSONObject;

import java.util.Arrays;

public class EmpInfo {
    private requestedVo requested;
    private Long provider;
    private String name;
    private int nodeCount;
    private String state;

    public requestedVo getRequested() {
        return requested;
    }

    public void setRequested(requestedVo requested) {
        this.requested = requested;
    }

    public Long getProvider() {
        return provider;
    }

    public void setProvider(Long provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "EmpInfo{" +
                "requested=" + requested +
                ", provider=" + provider +
                ", name='" + name + '\'' +
                ", nodeCount=" + nodeCount +
                ", state='" + state + '\'' +
                '}';
    }
}
