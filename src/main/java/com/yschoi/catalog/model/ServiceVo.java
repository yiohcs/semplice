package com.yschoi.catalog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceVo {
    private String clusterIp;
    private String appId;
    private String name;
    private String namespaceId;
    private String projectId;
    private PortsVo ports;

}
