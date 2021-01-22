package com.yschoi.catalog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortsVo {
    private int nodePort;
    private int port;
    private String protocol;
    private int targetPort;

}
