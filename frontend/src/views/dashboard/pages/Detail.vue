<template>
  <v-container>
      <v-row justify="center">
      <v-col
        cols="11"
      >
        <v-card>
          <v-toolbar flat>
<!--            <template v-slot:extension>-->
              <v-tabs
                  v-model="currentItem"
                  fixed-tabs
                  slider-color="black"
              >
                <v-tab
                    v-for="item in items"
                    :key="item"
                    :href="'#tab-' + item"
                >
                  {{ item }}
                </v-tab>

              </v-tabs>
<!--            </template>-->
          </v-toolbar>

          <v-tabs-items v-model="currentItem">
            <v-tab-item
                v-for="item in items"
                :key="item"
                :value="'tab-' + item"
            >
              <v-card flat v-if="item=='대시보드'">
                <v-card-text>
                  <v-card
                      class="mx-3"
                      outlined
                  >
                    <v-toolbar
                    >
                      <span class="subheading" style="width: 30%; text-align: center;">공급자:{{$store.state.clusterDetailInfo.provider}}</span>
                      <v-divider
                          class="mx-4"
                          vertical
                      ></v-divider><span class="subheading" style="width: 30%; text-align: center;">쿠버네티스 버전:{{$store.state.clusterDetailInfo.version.gitVersion}}</span>
                      <v-divider
                          class="mx-4"
                          vertical
                      ></v-divider><span class="subheading" style="width: 30%; text-align: center;">생성일:{{$store.state.clusterDetailInfo.created}}</span>
                    </v-toolbar>

                  </v-card>

                  <v-card
                      class="mx-3"
                      outlined
                  >
                    <div style="display: flex; width: 100%; height: 200px;">
                      <div style="width: 33.33%;">
                        <div class="column">
                          <donut :percent="clusterDetailInfo.cpuPercent"
                                 :visibleValue="true"/>
                        </div>
                      </div>
                      <div style="width: 33.33%;">
                        <div class="column">
                          <donut :percent="clusterDetailInfo.memPercent"
                                 :visibleValue="true"/>
                        </div>
                      </div>
                      <div style="width: 33.33%;">
                        <div class="column">
                          <donut :percent="clusterDetailInfo.podsPercent"
                                 :visibleValue="true"/>
                        </div>
                      </div>
                    </div>
                    <v-toolbar
                    >
                      <span class="subheading" style="width: 33.33%; text-align: center;">cpu:{{$store.state.clusterDetailInfo.requested.cpu}}/{{$store.state.clusterDetailInfo.capacity.cpu}}</span>
                      <v-divider
                          class="mx-4"
                          vertical
                      ></v-divider><span class="subheading" style="width: 33.33%; text-align: center;">memory:{{$store.state.clusterDetailInfo.requested.memory}}/{{$store.state.clusterDetailInfo.capacity.memory}}</span>
                      <v-divider
                          class="mx-4"
                          vertical
                      ></v-divider><span class="subheading" style="width: 33.33%; text-align: center;">pods:{{$store.state.clusterDetailInfo.requested.pods}}/{{$store.state.clusterDetailInfo.capacity.pods}}</span>
                    </v-toolbar>

                  </v-card>
                </v-card-text>
              </v-card>

              <v-card flat v-if="item=='노드'">
                <v-card-text>
                  <node></node>
<!--                  <v-data-table-->
<!--                      :headers="headers"-->
<!--                      :items="information"-->
<!--                      :items-per-page="10"-->
<!--                      class="elevation-1"-->
<!--                  >-->

<!--                  </v-data-table>-->
                </v-card-text>
              </v-card>

              <v-card flat v-if="item=='프로젝트/네임스페이스'">
                <v-card-text>
                  <projects></projects>
                </v-card-text>
              </v-card>


            </v-tab-item>
          </v-tabs-items>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const axios = require('axios');
const Vue = require('vue');
import drawer from "../components/core/Drawer";
import projects from "./Project";
import node from "./Node";
// import donut from 'vue-doughnut-chart'
// import donut from '../component/DonutChart'
import donut from '../component/DoughnutChart'

export default {
  name: "Detail",
  components:{
    projects,
    node,
    donut,
  },
  data: () => ({
    headers: [
      { text: '상태', value: 'state'},
      { text: '노드 이름', value: 'appliedSpec.rancherKubernetesEngineConfig.nodes[0].hostnameOverride' },
      { text: '역할', value: 'appliedSpec.rancherKubernetesEngineConfig.nodes[0].role' },
      { text: '버전', value: 'version.gitVersion' },
      { text: 'CPU', value: 'cpu'},
      { text: '메모리', value: 'memory'},
      { text: '파드', value: 'pods' },
      { text: '수정/삭제', value: 'actions', sortable: false}
    ],
    tasksDone: 125,
    currentItem: 'tab-Web',
    items: [
      '대시보드', '노드', '프로젝트/네임스페이스',
    ],
    text: 'Test Text',
    information: [
    ],
    provider: '',
    KubeVersion: '',
    Created: '',
    requested:{
      cpu: '',
      memory: '',
      pods: '',
    },
    capacity:{
      cpu: '',
      memory: '',
      pods: '',
    },
    id: '',
    clusterDetailInfo: {
      provider:'',
      created:'',
      version:{
        gitVersion: '',
      },
      name: 'Cluster Name',
      nodeCount: '',
      requested:{
        cpu: '',
        memory: '',
        pods: '',
      },
      capacity:{
        cpu: '',
        memory: '',
        pods: '',
      },
      id: '',
      cpu: '',
      memory: '',
      pods: '',
      appliedSpec: {
        rancherKubernetesEngineConfig:{
          nodes: {},
        },
      },
      cpuPercent: '',
      memPercent: '',
      podsPercent: '',
    },
    nodes: [
    ],
  }),

  created(){
    let _self = this

    this.clusterDetailInfo = this.$store.state.clusterDetailInfo
    this.information.push(this.clusterDetailInfo)
    console.log(this.information)
    console.log(this.clusterDetailInfo)


    ///////////////// cpu
    let reqCPU = this.clusterDetailInfo.requested.cpu
    let capCPU = this.clusterDetailInfo.capacity.cpu

    if(reqCPU == undefined || reqCPU == '' || reqCPU == '0'){
      return
    }

    if(reqCPU.indexOf('m') != -1){
      reqCPU = reqCPU.substring(0,reqCPU.indexOf('m'))
      // console.log(reqCPU)
    }else{
      reqCPU = reqCPU*1000
      // console.log(reqCPU)
    }

    if(capCPU.indexOf('m') != -1){
      capCPU = capCPU.substring(0,capCPU.indexOf('m'))
      // console.log(capCPU)
    }else{
      capCPU = capCPU*1000
    }

    let cpuPercent = reqCPU/capCPU*100
    cpuPercent = cpuPercent.toFixed(1)
    console.log(cpuPercent)

    //////////////// memory
    let reqMemory = this.clusterDetailInfo.requested.memory
    let capMemory = this.clusterDetailInfo.capacity.memory

    if(reqMemory.indexOf('Ki') != -1){
      reqMemory = reqMemory.substring(0,reqMemory.indexOf('Ki'))
      // console.log(reqCPU)
    }else{
      reqMemory = reqMemory.substring(0,reqMemory.indexOf('Mi'))*1024
      // console.log(reqCPU)
    }

    if(capMemory.indexOf('Ki') != -1){
      capMemory = capMemory.substring(0,capMemory.indexOf('Ki'))
      // console.log(capCPU)
    }else{
      capMemory = reqMemory.substring(0,capMemory.indexOf('Mi'))*1024
    }

    let memPercent = reqMemory/capMemory*100
    memPercent = memPercent.toFixed(1)
    console.log(memPercent)

    /////////////// pods
    let reqPods = this.clusterDetailInfo.requested.pods
    let capPods = this.clusterDetailInfo.capacity.pods

    let podsPercent = reqPods/capPods*100
    podsPercent = podsPercent.toFixed(1)
    console.log(podsPercent)

    this.clusterDetailInfo.cpuPercent = cpuPercent
    this.clusterDetailInfo.memPercent = memPercent
    this.clusterDetailInfo.podsPercent = podsPercent
    this.$store.commit('SET_LOADING', false)

  },

  methods: {

  },

}
</script>
<style scoped>
.column {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
}
</style>