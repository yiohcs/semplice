<template>
  <v-container
    id="node"
    fluid
    tag="section"
  >
    <div style="position: relative; width: 100%; height: 60px;">
      <v-btn
          color="primary"
          dark
          class="mb-2"
          @click="createNodeShow"
          style="position: absolute; right: 4.5%;">

        노드 추가
      </v-btn>
    </div>

    <v-data-table
        :headers="headers"
        :items="information"
        :items-per-page="10"
        class="elevation-1"
    >
      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="nodeModifyShow(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="nodeDeleteShow"
        >
          mdi-delete
        </v-icon>

        <v-dialog v-model="createNodeDialog" @close="createNodeDialogClose" max-width="700px">
          <v-card>
            <v-card-title>
              <span class="headline">노드 추가</span>
            </v-card-title>

            <div style="margin-left:5%; margin-right:5%; ">
              <div style="text-align: center;">
                <v-checkbox v-model="etcd" @change="checkRole" label="etcd" style="display: inline-block;"></v-checkbox>
                <v-checkbox v-model="controlPlane" @change="checkRole" label="controlPlane" style="display: inline-block; margin-left: 10%;"></v-checkbox>
                <v-checkbox v-model="worker" @change="checkRole" label="worker" style="display: inline-block; margin-left: 10%;"></v-checkbox>
              </div>
              <span>이미 실행중인 하나 이상의 기존 머신에서 이 Docker 명령을 실행하세요.<br><br></span>
              <span>{{ nodeCommand+nodeCommandRole }}</span>
            </div>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="createNodeDialogClose"
              >
                취소
              </v-btn>
            </v-card-actions>
          </v-card>

        </v-dialog>


        <v-dialog
            v-model="modifyDialog"
            max-width="600px"
        >
          <v-card>
            <v-card-title>
              <span class="headline"> 노드 수정 </span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <v-text-field
                        v-model="editTable.name"
                        label="노드 이름"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="modifyNodeName"
              >
                저장
              </v-btn>
              <v-btn
                  color="blue darken-1"
                  text
                  @click="closeModify"
              >
                취소
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="deleteDialog" max-width="500px">
          <v-card>
            <v-card-title class="headline">노드를 삭제하시겠습니까?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="deleteNode(item)">삭제</v-btn>
              <v-btn color="blue darken-1" text @click="closeDelete">취소</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
const axios = require('axios');
const Vue = require('vue');
var json_list;

export default {
  name: "Projects",
  // data: function (){
  //   return{
  //     list:['a','b'],
  //   };
  // }
  data () {
    return {
      modifyDialog: false,
      deleteDialog: false,
      createNodeDialog: false,
      createNode: true,
      etcd: false,
      controlPlane: false,
      worker: false,
      resData: '',
      nodeCommand: '',
      nodeCommandRole: '',
      headers: [
        { text: '상태', value: 'state'},
        { text: '노드 이름', value: 'newNodeName' },
        { text: '역할', value: 'role' },
        { text: '버전', value: 'info.kubernetes.kubeletVersion' },
        { text: 'CPU', value: 'cpu'},
        { text: '메모리', value: 'memory'},
        { text: '파드', value: 'pods' },
        { text: '수정/삭제', value: 'actions', sortable: false}
      ],
      information: [
      ],
      editTable:{
        name: '',
        id: '',
      },
    }
  },
  methods:{
    createNodeDialogClose(){
      this.createNodeDialog = false
      this.etcd = false
      this.controlPlane = false
      this.worker = false
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
    createNodeShow(){
      this.checkRole()
      console.log(this.createNodeDialog)
      this.getClusterToken()
      this.createNodeDialog = true
      console.log(this.createNodeDialog)
    },
    getClusterToken(){
      let _self = this

      let nodeData = {
        id: _self.$store.state.clusterDetailInfo.id,
      }
      console.log(nodeData)

      axios.post('/api/nodes/getClusterToken',nodeData
      ).then(function (response){
        console.log(response.data)
        _self.resData = response.data[0]

        _self.nodeCommand = _self.resData.nodeCommand
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    nodeModifyShow(item){
      this.modifyDialog = true
      this.editTable.name = item.newNodeName
      this.editTable.id = item.id
    },
    nodeDeleteShow(){
      this.deleteDialog = true
    },
    deleteNode(item){
      console.log(item)
      let _self = this;
      _self.deleteDialog = false
      _self.$store.commit('SET_LOADING', true)

      let nodeData = {
        id: item.id,
      }

      axios.post('/api/nodes/delete',nodeData
      ).then(function (response){
        _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '노드 삭제 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    closeDelete(){
      this.deleteDialog = false
    },
    closeModify(){
      this.modifyDialog = false
    },

    modifyNodeName(){
      let _self = this
      _self.modifyDialog = false
      _self.$store.commit('SET_LOADING', true)

      let nodeData = {
        id: _self.editTable.id,
        name: _self.editTable.name,
      }
      console.log(nodeData)

      axios.post('/api/nodes/modifyNodeName',nodeData
      ).then(function (response){

        _self.node_Get()
        _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '노드 이름 수정 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    node_Get(){
      let _self = this

      axios.get('/api/nodes/'+_self.$store.state.clusterDetailInfo.id,{
      }).then(function (response){

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        _self.information = json;

        for(let i=0;i<_self.information.length;i++){
          if(_self.information[i].name=='' || _self.information[i].name==undefined){
            console.log(_self.information)
            console.log(_self.information.nodeName)
            _self.information[i].newNodeName = _self.information[i].nodeName
          }else{
            _self.information[i].newNodeName = _self.information[i].name
          }
        }

        console.log(_self.information)

        for(let i=0;i<json.length;i++){
          let role = ''
          if(json[i].etcd || json[i].controlPlane || json[i].worker)
          {
            if(json[i].etcd){role += ',etcd'}
            if(json[i].controlPlane){role += ',controlPlane'}
            if(json[i].worker){role += ',worker'}
            role = role.substring(1,role.length)
            console.log(role)
          }
          _self.information[i].role = role

          let cpu = json[i].requested.cpu + '/' + json[i].capacity.cpu
          let memory = json[i].requested.memory + '/' + json[i].capacity.memory
          let pods = json[i].requested.pods + '/' + json[i].capacity.pods
          console.log('cpu >>>>> '+cpu)
          if(cpu == undefined || cpu == '' || cpu == '0'){
            return
          }

          _self.information[i].cpu = cpu
          _self.information[i].memory = memory
          _self.information[i].pods = pods

        }

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
  },
  created(){
    let _self = this

    if(_self.$store.state.nodeTimer == '') {
      _self.$store.commit('SET_NODE_TIMER', setInterval(function () {
        _self.node_Get()
      }, 5000))
    }

    _self.$store.commit('SET_LOADING', true)
    axios.get('/api/nodes/'+_self.$store.state.clusterDetailInfo.id,{
    }).then(function (response){

      let jsonStr = JSON.stringify(response.data);
      let json = JSON.parse(jsonStr);

      _self.information = json;
      for(let i=0;i<_self.information.length;i++){
        if(_self.information[i].name=='' || _self.information[i].name==undefined){
          _self.information[i].newNodeName = _self.information[i].nodeName
        }else{
          _self.information[i].newNodeName = _self.information[i].name
        }
      }
      console.log(_self.information)

      for(let i=0;i<json.length;i++){
        let role = ''
        if(json[i].etcd || json[i].controlPlane || json[i].worker)
        {
          if(json[i].etcd){role += ',etcd'}
          if(json[i].controlPlane){role += ',controlPlane'}
          if(json[i].worker){role += ',worker'}
          role = role.substring(1,role.length)
          console.log(role)
        }
        _self.information[i].role = role

        let cpu = json[i].requested.cpu + '/' + json[i].capacity.cpu
        let memory = json[i].requested.memory + '/' + json[i].capacity.memory
        let pods = json[i].requested.pods + '/' + json[i].capacity.pods

        _self.information[i].cpu = cpu
        _self.information[i].memory = memory
        _self.information[i].pods = pods

      }



      _self.$store.commit('SET_LOADING', false)

    }).catch(function (error){
      console.log(error);
    }).finally(function (){

    });

    // this.clusterDetailInfo = this.$store.state.clusterDetailInfo
    // if(this.clusterDetailInfo.appliedSpec.rancherKubernetesEngineConfig != null){
    //   this.clusterDetailInfo.hostnameOverride = this.clusterDetailInfo.appliedSpec.rancherKubernetesEngineConfig.nodes[0].hostnameOverride
    //   this.clusterDetailInfo.role = this.clusterDetailInfo.appliedSpec.rancherKubernetesEngineConfig.nodes[0].role
    // }else{
    //   this.clusterDetailInfo.hostname = this.clusterDetailInfo.id
    //   this.clusterDetailInfo.role = 'controlplane'
    // }
    // this.information.push(this.clusterDetailInfo)
  },
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