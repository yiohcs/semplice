package com.yschoi.cluster.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleVo {
    private String etcd;
    private String controlplane;
    private String worker;
}
