import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    barColor: 'rgba(0, 0, 0, .8), rgba(0, 0, 0, .8)',
    barImage: 'https://demos.creative-tim.com/material-dashboard/assets/img/sidebar-1.jpg',
    loader: false,
    drawer: null,
    clusterTimer: '',
    userTimer: '',
    nodeTimer: '',
    projectTimer: '',
    appTimer: '',
    bearerToken: '',
    userId: '',
    name: '',
    loginTime: '',
    loadingStartTime: '',
    errorCheck: false,
    alertText: '',
    alertType: 'error',
    clusterDetailInfo: {
      cpid: '',
      projectName: '',
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
      hostnameOverride: '',
      role: '',
      gitVersion: '',
      nodes: [],
    },
  },
  getters: {

  },
  mutations: {
    SET_BAR_IMAGE (state, payload) {
      state.barImage = payload
    },
    SET_DRAWER (state, payload) {
      state.drawer = payload
    },

    SET_TOKEN(state, bearerToken){
      state.bearerToken = bearerToken
    },
    SET_USERID(state, userId){
      state.userId = userId
    },
    SET_NAME(state, name){
      state.name = name
    },
    SET_TIME(state, loginTime){
      state.loginTime = loginTime
    },
    SET_CLUSTER_DETAIL_INFO(state, clusterDetailInfo){
      state.clusterDetailInfo = clusterDetailInfo
    },
    SET_CPU(state, cpu){
      state.clusterDetailInfo.cpu = cpu
    },
    SET_MEMORY(state, memory){
      state.clusterDetailInfo.memory = memory
    },
    SET_PODS(state, pods){
      state.clusterDetailInfo.pods = pods
    },
    SET_HOSTNAME_OVERRIDE(state, hostnameOverride){
      state.clusterDetailInfo.hostnameOverride = hostnameOverride
    },
    SET_ROLE(state, role){
      state.clusterDetailInfo.role = role
    },
    SET_GIT_VERSION(state, gitVersion){
      state.clusterDetailInfo.gitVersion = gitVersion
    },
    SET_NODES(state, nodes){
      state.clusterDetailInfo.nodes = nodes
    },
    SET_LOADING(state, loading){
      state.loader = loading
    },
    SET_CPID(state, cpid){
      state.clusterDetailInfo.cpid = cpid
    },
    SET_PROJECT_NAME(state, projectName){
      state.clusterDetailInfo.projectName = projectName
    },
    SET_CLUSTER_TIMER(state, timer){
      state.clusterTimer = timer
    },
    SET_USER_TIMER(state, timer){
      state.userTimer = timer
    },
    SET_NODE_TIMER(state, timer){
      state.nodeTimer = timer
    },
    SET_PROJECT_TIMER(state, timer){
      state.projectTimer = timer
    },
    SET_APP_TIMER(state, timer){
      state.appTimer = timer
    },
    SET_ERROR_CHECK(state, errorCheck){
      state.errorCheck = errorCheck
    },
    SET_ALERT_TEXT(state, alertText){
      state.alertText = alertText
    },
    SET_ALERT_TYPE(state, alertType){
      state.alertType = alertType
    },
  },
  actions: {
    set_token_and_id({commit, state}, $data){
      commit('SET_TOKEN_AND_ID', $data)
    }
  },
  modules:{

  },

  plugins:[
      createPersistedState(

      ),
  ],
})
