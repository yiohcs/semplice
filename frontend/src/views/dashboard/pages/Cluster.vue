<template>
  <v-container
    id="user-profile"
    fluid
    tag="section"
  >
    <v-row justify="center">
      <v-col
        cols="12"
        md="10"
      >
<!--          <v-data-table-->
<!--              :headers="headers"-->
<!--              :items="information"-->
<!--              :items-per-page="10"-->
<!--              class="elevation-1"-->
<!--              @click:row="dataClick"-->
<!--          >-->
          <v-data-table
              :headers="headers"
              :items="information"
              :items-per-page="10"
              class="elevation-1"
          >
            <template v-slot:top>
                <v-divider
                    class="mx-4"
                    inset
                    vertical
                ></v-divider>
                <v-spacer></v-spacer>
              <div style="position: relative; width: 100%; height: 50px;">
                <div style="position: absolute; right: 7%; white-space: nowrap;">
                  <v-btn
                      color="primary"
                      dark
                      class="mb-2"
                      @click="createCluster"
                      style="display: inline-block;"
                  >
                    새 클러스터 생성
                  </v-btn>
                  <v-btn
                      color="primary"
                      dark
                      class="mb-2"
                      @click="importCluster"
                      style="margin-left: 10%; display: inline-block;"
                  >
                    기존 클러스터 추가
                  </v-btn>
                </div>
              </div>

                <v-dialog v-model="dialogCreate" @close="cluster_Get" max-width="700px">
                  <v-card>
                    <v-card-title>
                      <span class="headline">{{ clusterFormTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                      <v-container>
                        <v-row>
                          <v-col
                              cols="12"
                              sm="6"
                              md="4"
                          >
                            <div style="height: 90px;">
                              <v-text-field
                                  v-model="editedItem.name"
                                  label="클러스터 이름"
                              ></v-text-field>
                              <div>{{editSpaceClusterName}}</div>
                            </div>
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-card-text>

                    <div v-show="showTokenCommand" style="margin-left:5%; margin-right:5%; ">
                      <div v-if="newCluster == -1" style="text-align: center;">
                        <v-checkbox v-model="etcd" @change="checkRole" label="etcd" style="display: inline-block;"></v-checkbox>
                        <v-checkbox v-model="controlPlane" @change="checkRole" label="controlPlane" style="display: inline-block; margin-left: 10%;"></v-checkbox>
                        <v-checkbox v-model="worker" @change="checkRole" label="worker" style="display: inline-block; margin-left: 10%;"></v-checkbox>
                      </div>
                      <span>{{newCluster == -1 ? '이미 실행중인 하나 이상의 기존 머신에서 이 Docker 명령을 실행하세요.' : '지원되는 Kubernetes 버전을 실행하는 기존 Kubernetes 클러스터에서 아래 kubectl 명령을 실행하여 Rancher로 가져옵니다.'}}<br><br></span>
                      <span>{{newCluster == -1 ? nodeCommand+nodeCommandRole : command }}</span>
                      <span><br><br>{{newCluster == -1 ? '' : '\'알 수없는 기관에서 서명 한 인증서\'에 대한 오류가 발생하는 경우 대신 아래 명령을 실행하여 인증서 검사를 우회하세요.'}}<br></span>
                      <span>{{newCluster == -1 ? '' : insecureCommand}}</span>
                    </div>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                          v-show="createBtnShow"
                          color="blue darken-1"
                          text
                          @click="clusterCreateImport"
                      >
                        생성
                      </v-btn>
                      <v-btn
                          color="blue darken-1"
                          text
                          @click="dialogCreateClose"
                      >
                        취소
                      </v-btn>
                    </v-card-actions>
                  </v-card>

                </v-dialog>

                <v-dialog
                    v-model="dialog"
                    max-width="600px"
                >
                  <v-card>
                    <v-card-title>
                      <span class="headline">{{ formTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                      <v-container>
                        <v-row>
                          <v-col
                              cols="12"
                              sm="6"
                              md="4"
                          >
                            <div style="height: 90px;">
                              <v-text-field
                                  v-model="editedItem.name"
                                  label="클러스터 이름"
                              ></v-text-field>
                              <div>{{editSpaceClusterName}}</div>
                            </div>
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-card-text>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                          color="blue darken-1"
                          text
                          @click="modifyClusterName"
                      >
                        저장
                      </v-btn>
                      <v-btn
                          color="blue darken-1"
                          text
                          @click="close"
                      >
                        취소
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
                <v-dialog v-model="dialogDelete" max-width="500px">
                  <v-card>
                    <v-card-title class="headline">클러스터를 삭제하시겠습니까?</v-card-title>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="blue darken-1" text @click="deleteItemConfirm">삭제</v-btn>
                      <v-btn color="blue darken-1" text @click="closeDelete">취소</v-btn>
                      <v-spacer></v-spacer>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
            </template>

            <template v-slot:item.name="{ item }">
              <a @click="dataClick(item)">{{ item.name }}</a>
            </template>

            <template v-slot:item.actions="{ item }">
              <v-icon
                  small
                  class="mr-2"
                  @click="editItem(item)"
              >
                mdi-pencil
              </v-icon>
              <v-icon
                  small
                  @click="deleteItem(item)"
              >
                mdi-delete
              </v-icon>
            </template>
<!--            <template v-slot:no-data>-->
<!--              <v-btn-->
<!--                  color="primary"-->
<!--                  @click="initialize"-->
<!--              >-->
<!--                Reset-->
<!--              </v-btn>-->
<!--            </template>-->
          </v-data-table>
<!--          <v-btn color="primary" @click="cluster_Delete()">Delete</v-btn>-->


      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const axios = require('axios');
const Vue = require('vue');
import drawer from "../components/core/Drawer";




export default {
  name: "Clusters",
  // data: function (){
  //   return{
  //     list:['a','b'],
  //   };
  // }
  methods:{
    modifyClusterName(){
      let _self = this;

      if(this.editedItem.name == '' || this.editedItem.name == undefined){
        return
      }
      _self.dialog = false
      _self.$store.commit('SET_LOADING', true)
      console.log('modify');
      let clusterData = {
        name: _self.editedItem.name,
        id: _self.editedItem.id
      }
      axios.post('/api/clusters/modifyClusterName',clusterData
      ).then(function (response){
        // console.log(response.data)

        _self.cluster_Get()
        _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '클러스터 이름 수정 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    checkRole(){
      this.nodeCommandRole = ''

      if(this.etcd){
        this.nodeCommandRole += ' --etcd'
      }
      if(this.controlPlane){
        this.nodeCommandRole += ' --controlplane'
      }
      if(this.worker){
        this.nodeCommandRole += ' --worker'
      }

    },
    editItem (item) {
      this.editedIndex = this.information.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    createCluster (item) {
      this.newCluster = -1
      this.showTokenCommand = false
      this.editedItem = Object.assign({}, item)
      this.dialogCreate = true
      this.createBtnShow = true
    },

    importCluster (item) {
      this.newCluster = 1
      this.showTokenCommand = false
      this.editedItem = Object.assign({}, item)
      this.dialogCreate = true
      this.createBtnShow = true
    },

    clusterCreateImport (item) {
      Object.assign({}, item)
      console.log(this.editedItem.name)
      // let showTokenCommand = document.getElementById('showTokenCommand')
      // showTokenCommand.style.visibility = 'visible'

      if(this.editedItem.name == '' || this.editedItem.name == undefined ){
        return
      }

      this.cluster_Create(this.editedItem.name)


      // await _sleep(1000);
      // console.log('Second');

      // this.close()
    },

    deleteItem (item) {
      if(item.name != 'local') {
        console.log(item.id)
        this.item_id = item.id
        this.editedIndex = this.information.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      }
    },

    deleteItemConfirm () {
      this.information.splice(this.editedIndex, 1)
      this.closeDelete()
      this.cluster_Delete(this.item_id);
    },

    dialogCreateClose(){
      this.dialogCreate = false
      this.cluster_Get()
    },

    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save () {
      if(this.editedItem.name == ''){
        return
      }

      if (this.editedIndex > -1) {
        Object.assign(this.information[this.editedIndex], this.editedItem)
      } else {
        // this.information.push(this.editedItem)
        this.cluster_Create(this.editedItem.name)
      }
      this.close()
    },

    cluster_Delete(id){
      let _self = this;
      console.log('delete');
      axios.get('/api/clusters/delete/'+id,{
      }).then(function (response){

        _self.cluster_Get()
        // await _sleep(1000);
        // console.log('Second');
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '클러스터 삭제 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    cluster_Create(name){
      let _self = this;
      _self.$store.commit('SET_LOADING', true)
      console.log('Create');
      axios.get('/api/clusters/create/'+name,{
      }).then(function (response){
        // console.log(response.data)
          _self.resData = response.data

          console.log('tokenData >>>> ')
          console.log(_self.resData)
          if (_self.newCluster == -1) {
            _self.nodeCommand = _self.resData.nodeCommand
          } else {
            _self.command = _self.resData.command
            _self.insecureCommand = _self.resData.insecureCommand
          }
          _self.showTokenCommand = true
          _self.createBtnShow = false
          _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '클러스터 등록 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    // cluster_Create(name){
    //   let _self = this;
    //   let returnToken
    //   console.log('Create');
    //   axios.get('/api/clusters/create/'+name,{
    //   }).then(function (response){
    //     // console.log(response.data)
    //
    //     const _sleep = (delay) => new Promise((resolve) => setTimeout(resolve, delay));
    //
    //     const timer = async () => {
    //
    //       _self.cluster_generateToken(response.data.id)
    //
    //       await _sleep(1500);
    //       console.log('First');
    //       _self.cluster_Get()
    //
    //       // await _sleep(1000);
    //       // console.log('Second');
    //     };
    //     timer();
    //   }).catch(function (error){
    //     console.log(error);
    //   }).finally(function (){
    //
    //   });
    // },
    //
    // cluster_generateToken(name){
    //   let _self = this;
    //   console.log('generateToken');
    //   axios.get('/api/clusters/generateToken/'+name,{
    //   }).then(function (response){
    //     console.log(response.data)
    //     console.log(response.data.nodeCommand)
    //
    //     _self.resData = response.data
    //       // await _sleep(1000);
    //       // console.log('Second');
    //
    //   }).catch(function (error){
    //     console.log(error);
    //   }).finally(function (){
    //
    //   });
    // },

    cluster_Get(){
      let _self = this;

      console.log('cluster_Get')

      axios.get('/api/clusters',{
      }).then(function (response){

        if(response.data =='' || response.data == null){
          _self.$store.commit('SET_TOKEN', response.data.token)
          _self.$store.commit('SET_USERID', response.data.userId)
          _self.$store.commit('SET_NAME', response.data.name)
          return
        }

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        let cpuList = [];
        let memoryList = [];

        _self.information = json;

        for(let i=0;i<json.length;i++){
          cpuList[i] = json[i].requested.cpu + '/' + json[i].capacity.cpu + ' cores';
          memoryList[i] = json[i].requested.memory + '/' + json[i].capacity.memory;
          _self.information[i].cpuList = cpuList[i];
          _self.information[i].memoryList = memoryList[i];
        }

        //drawer.items.childItem = titleList

        // json_list = json;
        // for(var i=0;i<json.length;i++){
        //   console.log(json[i].memory);
        // };


      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    dataClick(row){
      let _self = this
      this.information.map((item, index) =>{
        console.log(row)
        item.selected = item === row

        this.$store.commit('SET_CLUSTER_DETAIL_INFO', row)
        this.$store.commit('SET_CPU', row.cpuList)
        this.$store.commit('SET_MEMORY', row.memoryList)
        this.$store.commit('SET_PODS', row.podsList)

        this.$store.commit('SET_LOADING', true)
        this.$router.push('/c/'+row.id)
      });
    },
    // dataClick(row){
    //   this.information.map((item, index) =>{
    //     item.selected = item === row
    //
    //     let clusterDetailInfo = {}
    //     clusterDetailInfo = row
    //
    //     this.$store.commit('SET_CLUSTER_DETAIL_INFO', clusterDetailInfo)
    //     this.$store.commit('SET_CPU', row.cpuList)
    //     this.$store.commit('SET_MEMORY', row.memoryList)
    //     this.$store.commit('SET_PODS', row.podsList)
    //     console.log(row)
    //     if(row.appliedSpec.rancherKubernetesEngineConfig != null) {
    //       this.$store.commit('SET_NODES', row.appliedSpec.rancherKubernetesEngineConfig.nodes)
    //       this.$store.commit('SET_HOSTNAME_OVERRIDE', row.appliedSpec.rancherKubernetesEngineConfig.nodes[0].hostnameOverride)
    //       this.$store.commit('SET_ROLE', row.appliedSpec.rancherKubernetesEngineConfig.nodes[0].role)
    //     }else{
    //       this.$store.commit('SET_HOSTNAME_OVERRIDE', row.id)
    //     }
    //     this.$store.commit('SET_GIT_VERSION', row.version.gitVersion)
    //
    //     this.$router.push('/c/'+row.id)
    //   });
    // },
  }, // end of methods

  data () {
    return{
      dialog: false,
      dialogDelete: false,
      dialogCreate: false,
      showTokenCommand: false,
      createBtnShow: true,
      resData: '',
      item_id: '',
      etcd: false,
      controlPlane: false,
      worker: false,
      headers: [
        { text: '상태', value: 'state'},
        { text: '클러스터 이름', value: 'name' },
        { text: '제공자', value: 'provider' },
        { text: '노드', value: 'nodeCount' },
        { text: 'CPU', value: 'cpuList'},
        { text: '메모리', value: 'memoryList'},
        { text: '수정/삭제', value: 'actions', sortable: false },
      ],
      information: [
      ],
      editedIndex: -1,
      newCluster: -1,
      command: '',
      insecureCommand: '',
      nodeCommand: '',
      nodeCommandRole: '',
      editedItem: {
        state: '',
        name: 'Cluster Name',
        provider: '',
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
      },
      defaultItem: {
        state: '',
        name: 'Cluster Name',
        provider: '',
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
        version:{
          gitVersion: '',
        },
        id: '',
      },
    }
  }, // end of data
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? '클러스터 생성' : '클러스터 수정'
    },

    clusterFormTitle () {
      return this.newCluster === -1 ? '클러스터 생성' : '기존 클러스터 추가'
    },

    editSpaceClusterName(){
      return this.editedItem.name == '' || this.editedItem.name == undefined ? '빈 칸을 입력해 주세요' : ''
    },
  },
  created(){
    let _self = this;

    if(_self.$store.state.clusterTimer == '') {
      _self.$store.commit('SET_CLUSTER_TIMER', setInterval(function () {
        _self.cluster_Get()
      }, 5000))
    }

    _self.$store.commit('SET_LOADING', true)

    console.log(this.$store.state.bearerToken)
    console.log(this.$store.state.userId)

    // if('' == this.$store.state.bearerToken || '' == this.$store.state.userId){
    //   this.$router.push('/login')
    //   return;
    // }


    axios.get('/api/clusters',{
    }).then(function (response){

      let jsonStr = JSON.stringify(response.data);
      let json = JSON.parse(jsonStr);

      let cpuList = [];
      let memoryList = [];
      let podsList = [];

      _self.information = json;

      for(let i=0;i<json.length;i++){
        cpuList[i] = json[i].requested.cpu + '/' + json[i].capacity.cpu + ' cores';
        memoryList[i] = json[i].requested.memory + '/' + json[i].capacity.memory;
        podsList[i] = json[i].requested.pods + '/' + json[i].capacity.pods;
        _self.information[i].cpuList = cpuList[i];
        _self.information[i].memoryList = memoryList[i];
        _self.information[i].podsList = podsList[i];
      }
      _self.$store.commit('SET_LOADING', false)

      // console.log('cluseter : ' + _self.$store.state.bearerToken)
      // console.log(_self.information);
      // for (var obj of _self.information) {
      //   console.log(obj);
      // }
      // json_list = json;
      // for(var i=0;i<json.length;i++){
      //   console.log(json[i].memory);
      // };


    }).catch(function (error){
      console.log(error);
    }).finally(function (){

    });
  }, // end of created
}
</script>

// const getProject = async () => {
//   try {
//     console.log('gg');
//     return await axios.get('/api/cluster');
//   }catch(error){
//     console.error(error);
//     console.log('error');
//   }
// };
//
// const projectList = async () => {
//   const project = await getProject();
//   console.log(project);
//   if(project.data.message){
//     console.log('');
//   }
//   return project;
// };
//
//   export default {
//     name: "Cluster",
//     created(){
//       var json = projectList();
//       console.log(json);
//     }
//   }