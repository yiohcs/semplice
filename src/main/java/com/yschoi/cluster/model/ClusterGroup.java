package com.yschoi.cluster.model;

import com.yschoi.demo.model.EmpInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClusterGroup {
    List<ClusterVo> clusterVos;
}
