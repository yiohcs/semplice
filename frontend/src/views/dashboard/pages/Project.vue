<template>
  <v-container
    id="project"
    fluid
    tag="section"
  >
    <div style="position: relative; width: 100%; height: 30px;">
      <v-btn
          color="primary"
          dark
          class="mb-2"
          @click="projectDialogShow"
          style="position: absolute; right: 33px;"
      >
        프로젝트 추가
      </v-btn>
    </div>
    <v-dialog v-model="projectDialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">프로젝트 추가</span>
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
                      v-model="editTableProject.name"
                      label="프로젝트 이름"
                  ></v-text-field>
                  <div>{{editSpaceProject}}</div>
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
              @click="projectCreate"
          >
            생성
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="projectDialogClose"
          >
            취소
          </v-btn>
        </v-card-actions>
      </v-card>

    </v-dialog>

    <div v-for="(item,i) in projects">
      <v-card
          class="mx-3"
          outlined
      >
        <div style="position: relative; width: 100%; height: 60px;">
          <span style="position: absolute; left: 2%; top: 30%;">project : {{item.name}}</span>
          <div style="position: absolute; right: 0px; top:15%; white-space: nowrap;">
            <v-btn
                color="primary"
                dark
                class="mb-2"
                @click="catalogView(item)"
                style="margin-right: 20px;"
                v-if="item.name !='Default' && item.name !='System'"
            >
              앱 조회
            </v-btn>
            <v-btn
                color="primary"
                dark
                class="mb-2"
                @click="namespaceDialogShow(item)"
                style="margin-right: 20px;"
            >
              네임스페이스 추가
            </v-btn>
            <v-btn
                color="primary"
                dark
                class="mb-2"
                @click="projectDeleteShow(item)"
                style="margin-right: 20px;"
                v-if="item.name !='Default' && item.name !='System'"
            >
              프로젝트 삭제
            </v-btn>
          </div>
        </div>

        <v-data-table
            :headers="projectHeaders"
            :items="null"
            hide-default-header
            hide-default-footer
            class="elevation-1"
        >
        </v-data-table>
        <v-data-table
            v-if="cnt == projects.length"
            :headers="namespacesHeaders"
            :items="namespaces[i]"
            hide-default-footer
            class="elevation-1"
        >
          <template v-slot:item.actions="{ item }">
<!--            <v-icon-->
<!--                small-->
<!--                class="mr-2"-->
<!--                @click="namespaceModifyShow(item)"-->
<!--            >-->
<!--              mdi-pencil-->
<!--            </v-icon>-->
            <v-icon
                small
                @click="namespaceDeleteShow(item)"
            >
              mdi-delete
            </v-icon>

          </template>

        </v-data-table>
      </v-card>


      <br>
    </div>

    <v-dialog
        v-model="modifyDialog"
        max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="headline"> 네임스페이스 수정 </span>
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
                    label="네임스페이스 이름"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

<!--        <v-card-actions>-->
<!--          <v-spacer></v-spacer>-->
<!--          <v-btn-->
<!--              color="blue darken-1"-->
<!--              text-->
<!--              @click="modifyNamespaceName"-->
<!--          >-->
<!--            저장-->
<!--          </v-btn>-->
<!--          <v-btn-->
<!--              color="blue darken-1"-->
<!--              text-->
<!--              @click="closeModify"-->
<!--          >-->
<!--            취소-->
<!--          </v-btn>-->
<!--        </v-card-actions>-->
      </v-card>
    </v-dialog>
    <v-dialog v-model="deleteDialog" max-width="500px">
      <v-card>
        <v-card-title class="headline">네임스페이스를 삭제하시겠습니까?</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="deleteNamespace">삭제</v-btn>
          <v-btn color="blue darken-1" text @click="closeDelete">취소</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="projectDeleteDialog" max-width="600px">
      <v-card>
        <v-card-title class="headline">프로젝트를 삭제하시겠습니까?</v-card-title>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="projectDelete"
          >
            삭제
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="projectDeleteDialog=false"
          >
            취소
          </v-btn>
        </v-card-actions>
      </v-card>

    </v-dialog>
    <v-dialog v-model="namespaceDialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="headline">네임스페이스 추가</span>
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
                      v-model="editTable.name"
                      label="네임스페이스 이름"
                  ></v-text-field>
                  <div>{{editSpaceNamespace}}</div>
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
              @click="namespaceCreate"
          >
            생성
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="namespaceDialogClose"
          >
            취소
          </v-btn>
        </v-card-actions>
      </v-card>

    </v-dialog>
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
      deleteDialog: false,
      modifyDialog: false,
      namespaceDialog: false,
      projectDialog: false,
      projectDeleteDialog: false,
      selectId: '',
      projectHeaders: [
        {text: 'Project Name', value: 'name'},
      ],
      namespacesHeaders: [
        {text: '상태', value: 'state'},
        {text: '네임스페이스 이름', value: 'name'},
        {text: '생성시간', value: 'created'},
        { text: '삭제', value: 'actions', sortable: false},
      ],
      projects: [],
      namespaces: [],
      editTable:{
        name: '',
        projectId: '',
      },
      editTableProject:{
        name: '',
        clusterId: '',
      },
      cnt: 0,
    }
  },
  created(){
    this.getProjects()
  },
  computed:{
    editSpaceNamespace(){
      return this.editTable.name == '' || this.editTable.name == undefined ? '빈 칸을 입력해 주세요' : ''
    },
    editSpaceProject(){
      return this.editTableProject.name == '' || this.editTableProject.name == undefined ? '빈 칸을 입력해 주세요' : ''
    },
  },
  methods:{
    namespaceDeleteShow(item){
      this.deleteDialog = true
      console.log(item)
      this.delNamespaceItem = item
    },
    // namespaceModifyShow(item){
    //   this.modifyDialog = true
    // },
    deleteNamespace(){
      let _self = this
      console.log(_self.delItem)
      _self.deleteDialog = false
      _self.$store.commit("SET_LOADING", true)

      let namespaceData = {
        name: _self.delNamespaceItem.name,
        clusterId: _self.$store.state.clusterDetailInfo.id,
      }

      axios.post('/api/projects/namespaceDelete', namespaceData
      ).then(function (response){
        console.log(response.data)
        _self.getProjects()
        _self.$store.commit("SET_LOADING", false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '네임스페이스 삭제 완료')

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
    // modifyNamespaceName(){
    //
    // },
    // closeModify(){
    //   this.modifyDialog = false
    // },

    projectDialogShow(){
      this.projectDialog = true
      this.editTableProject.name = ''
    },
    projectDialogClose(){
      this.editTableProject.name = ''
      this.projectDialog = false
    },
    projectDeleteShow(item){
      this.projectDeleteDialog = true
      this.delItem = item
    },
    projectDelete(){
      let _self = this
      console.log(_self.delItem)
      _self.projectDeleteDialog = false
      _self.$store.commit("SET_LOADING", true)

      let projectData = {
        name: _self.delItem.name,
        projectId: _self.delItem.id,
        clusterId: _self.delItem.clusterId,
      }

      axios.post('/api/projects/projectDelete', projectData
      ).then(function (response){
        console.log(response.data)
        _self.getProjects()
        _self.$store.commit("SET_LOADING", false)
        _self.$store.commit('SET_ALERT_TYPE', 'success')
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '프로젝트 삭제 완료')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    projectCreate(){
      let _self = this
      if(_self.editTableProject.name == '' || _self.editTableProject.name == undefined){
        return
      }
      this.projectDialog = false
      _self.$store.commit("SET_LOADING", true)

      let projectData = {
        name: _self.editTableProject.name,
        clusterId: _self.$store.state.clusterDetailInfo.id,
      }

      axios.post('/api/projects/projectCreate', projectData
      ).then(function (response){
        console.log(response.data)
        _self.getProjects()
        _self.$store.commit("SET_LOADING", false)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    catalogView(item){
      this.$store.commit('SET_PROJECT_NAME', item.name)
      this.$store.commit('SET_CPID',item.id)
      this.$router.push('/c/'+item.id+'/apps')
    },
    namespaceDialogShow(data){
      console.log(data)
      this.editTable.name = ''
      this.editTable.projectId = data.id
      this.namespaceDialog = true
    },
    namespaceCreate(){
      let _self = this
      if(_self.editTable.name == '' || _self.editTable.name == undefined){
        return
      }
      this.namespaceDialog = false
      _self.$store.commit("SET_LOADING", true)

      let namespaceData = {
        name: _self.editTable.name,
        projectId: _self.editTable.projectId,
        clusterId: _self.$store.state.clusterDetailInfo.id,
      }

      axios.post('/api/projects/namespaceCreate', namespaceData
      ).then(function (response){
        console.log(response.data)
        _self.getProjects()
        _self.$store.commit("SET_LOADING", false)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    namespaceDialogClose(){
      this.editTable.name = ''
      this.namespaceDialog = false
    },
    getProjects(){
      let _self = this;
      _self.$store.commit('SET_LOADING', true)

      _self.cnt = 0
      axios.get('/api/projects/'+_self.$store.state.clusterDetailInfo.id,{
      }).then(function (response){

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        _self.projects = json;

        for (let i=0;i<json.length;i++){
          // _self.namespaces.push([])
        }

        for (let obj of _self.projects) {
          _self.namespaces.push([obj])
        }

        for (let [index, obj] of _self.projects.entries()) {
          console.log(obj);
          console.log(index);
          _self.getNamespaces(obj.id, index)
        }
        _self.$store.commit('SET_LOADING', false)

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    getNamespaces(projectId, index){
      let _self = this
      axios.get('/api/projects/namespaces/'+_self.$store.state.clusterDetailInfo.id+'/'+projectId,{
      }).then(function (response){

        let jsonStr = JSON.stringify(response.data)
        let json = JSON.parse(jsonStr)

        console.log(json)
        console.log(index)
        _self.namespaces[index] = json
        for (let obj of _self.namespaces) {
          console.log(obj);
        }
        _self.cnt++

        if(_self.cnt == _self.projects.length){
          _self.$store.commit('SET_LOADING', false)
        }

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    }
  }
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