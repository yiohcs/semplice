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
        <v-card>
          <div style="position: relative; width: 100%; height: 60px;">
            <div style="position: absolute; right: 3%; top: 15%; white-space: nowrap;">
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  @click="catalogCreateShow"
                  style="display: inline-block;"
              >
                앱 추가
              </v-btn>
            </div>
          </div>

          <v-data-table
              :headers="headers"
              :items="information"
              :items-per-page="10"
              class="elevation-1"
          >
          </v-data-table>
        </v-card>


        <v-dialog v-model="dialogCatalog" max-width="700px" v-if="dialogCatalog">
          <v-card>
            <v-card-title >
              <span class="headline">앱 추가</span>
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
                          v-model="selName"
                          label="앱 이름"
                      ></v-text-field>
                      <div>{{editSpaceAppName}}</div>
                    </div>

                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                >
                    <div style="height: 90px;">
                      <v-select
                          :items="$store.state.clusterDetailInfo" @change="projectIdChange" item-text="projectName" item-value="cpid" label="프로젝트">
                      </v-select>
                      <div>{{editSpaceProject}}</div>
                    </div>
                </v-col>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <div style="height: 90px;">
                      <v-select
                          :items="namespaces" @change="namespaceChange" item-text="name" item-value="id" label="네임스페이스">
                      </v-select>
                      <div>{{editSpaceNamespace}}</div>
                    </div>
                  </v-col>
                  <v-col
                      cols="12"
                      sm="6"
                      md="4"
                  >
                    <div style="height: 90px;">
                      <v-select
                      :items="selCatalogs" @change="catalogChange" item-text="name" item-value="id" label="카탈로그 종류">
                      </v-select>
                      <div>{{editSpaceCatalog}}</div>
                    </div>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
            <v-col style="padding-left: 70%;">
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  @click="createCatalog"
                  style=""
              >
                앱 추가
              </v-btn>
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  @click="closeCatalog"
                  style="margin-left: 5%;"
              >
                취소
              </v-btn>
            </v-col>
          </v-card>
        </v-dialog>
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
    projectIdChange(item){
      console.log(item)
      this.selProject = item
      this.getNamespaces(this.$store.state.clusterDetailInfo.cpid)
    },
    namespaceChange(name){
      console.log(name)
      this.selNamespace = name
    },
    catalogChange(id){
      console.log(id)
      this.selCatalog = id
    },
    closeCatalog(){
      this.dialogCatalog = false
    },
    catalogCreateShow(){
      this.selName = ''
      this.selProject = ''
      this.selNamespace = ''
      this.selCatalog = ''
      this.dialogCatalog = true
    },
    createCatalog(){
      let _self = this
      if(_self.selName == '' || _self.selProject == '' || _self.selNamespace == '' || _self.selCatalog == ''){
        return
      }
      _self.$store.commit('SET_LOADING', true)
      _self.dialogCatalog = false
      let catalogData = {
        name: _self.selName,
        namespaceId: _self.selNamespace,
        projectId: _self.$store.state.clusterDetailInfo.cpid,
        externalId: _self.selCatalog,
      }
      axios.post('/api/catalog/createCatalog',catalogData
      ).then(function (response){
        console.log(response.data)

        _self.getCatalog()
        _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '애플리케이션 등록 완료')
        _self.selName = ''

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
        // this.getServices(response.data.projectId)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    getNamespaces(cpid){
      let _self = this
      axios.get('/api/projects/namespaces/'+_self.$store.state.clusterDetailInfo.id+'/'+cpid,{
      }).then(function (response){

        let jsonStr = JSON.stringify(response.data)
        let json = JSON.parse(jsonStr)

        console.log(json)
        _self.namespaces = json

        console.log(_self.namespaces)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    getCatalog(){
      let _self = this
      let catalogData = {
        projectId: this.$store.state.clusterDetailInfo.cpid,
      }
      axios.post('/api/catalog/getApps',catalogData
      ).then(function (response){
        console.log(response.data)

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        _self.information = json;


        for(let i=0;i<json.length;i++){
          _self.information[i].port = ''
          for(let j=0;j<json[i].ports.length;j++){
            if(json[i].ports[j].nodePort == '0'){
              break
            }
            _self.information[i].port += ' ' + json[i].ports[j].nodePort + '/' + json[i].ports[j].protocol
          }
          _self.information[i].port = _self.information[i].port.substring(1)
        }
        console.log(_self.information)



        // this.getServices(response.data.projectId)
      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    }
  }, // end of methods

  data () {
    return {
      dialogCatalog:false,
      projects:[],
      namespaces:[],
      catalogs:[{name:"grafana",id:'catalog://?catalog=library&template=grafana&version=3.8.6'},],
      headers:[
        {text:'상태',value:'state'},
        {text:'앱 이름',value:'selector.release'},
        {text:'네임스페이스',value:'namespaceId'},
        {text:'카탈로그 종류',value:'selector.app'},
        {text:'서비스 포트',value:'port'},
      ],
      information:[],
      selName: '',
      selNamespace: '',
      selCatalog: '',
      selCatalogs: '',
      selProject: '',
    }
  }, // end of data
  computed: {
    editSpaceAppName(){
      return this.selName == '' || this.selName == undefined ? '빈 칸을 입력해 주세요' : ''
    },
    editSpaceProject(){
      return this.selProject == '' || this.selProject == undefined ? '빈 칸을 입력해 주세요' : ''
    },
    editSpaceNamespace(){
      return this.selNamespace == '' || this.selNamespace == undefined ? '빈 칸을 입력해 주세요' : ''
    },
    editSpaceCatalog(){
      return this.selCatalog == '' || this.selCatalog == undefined ? '빈 칸을 입력해 주세요' : ''
    },
  },
  created(){
    let _self = this
    _self.selCatalogs = _self.catalogs
    _self.selProject = _self.$store.state.clusterDetailInfo
    _self.$store.commit('SET_LOADING', true)
    _self.getCatalog()
    _self.$store.commit('SET_LOADING', false)
    if(_self.$store.state.appTimer == '') {
      _self.$store.commit('SET_APP_TIMER', setInterval(function () {
        _self.getCatalog()
      }, 5000))
    }
  }, // end of created
}
</script>