package com.yschoi.cluster.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeVo {
    private String address;
    private String hostnameOverride;
    private String port;
    private RoleVo role;

}
