package com.yschoi.cluster.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClusterVo {
    private CommonVo requested;
    private String provider;
    private VersionVo version;
    private String created;
    private String name;
    private String id;
    private int nodeCount;
    private String state;
    private AppliedSpecVo appliedSpec;

}
