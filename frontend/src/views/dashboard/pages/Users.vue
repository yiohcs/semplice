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
        <v-data-table
            :headers="headers"
            :items="information"
            :items-per-page="10"
            class="elevation-1"
        >
          <template v-slot:top >
            <v-divider
                class="mx-4"
                inset
                vertical
            ></v-divider>
            <v-spacer></v-spacer>
            <v-dialog
                v-model="dialog"
                max-width="500px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-row style="position: relative; width: 100%; height: 50px;">
                  <v-col style="position: absolute; width:105px; right: 25px; top:-10px;">
                    <v-btn
                        color="primary"
                        dark
                        class="mb-2"
                        v-bind="attrs"
                        v-on="on"
                    >
                      유저 추가
                    </v-btn>
                  </v-col>
                </v-row>

              </template>
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
                          md="12"
                      >
                        <div style="height: 90px;">
                          <v-text-field
                              v-model="editedItem.username"
                              label="로그인 ID"
                          >
                          </v-text-field>
                          <div>{{editSpaceUsername}}</div>
                        </div>
                        <div style="height: 90px;">
                          <v-text-field
                              v-model="editedItem.password"
                              label="비밀번호"
                              type="password"
                          ></v-text-field>
                          <div>{{editSpacePassword}}</div>
                        </div>
                        <div style="height: 90px;">
                          <v-text-field
                              v-model="editedItem.name"
                              label="이름"
                          ></v-text-field>
                          <div>{{editSpaceName}}</div>
                        </div>
                      </v-col>
<!--                      <v-col-->
<!--                          cols="12"-->
<!--                          sm="6"-->
<!--                          md="5"-->
<!--                      >-->
<!--                        <div style="margin-top:30px; height: 32px; color: red;">{{editSpaceUsername}}</div>-->
<!--                        <div style="margin-top:40px; height: 30px; color: red;">{{editSpacePassword}}</div>-->
<!--                        <div style="margin-top:40px; height: 30px; color: red;">{{editSpaceName}}</div>-->
<!--                      </v-col>-->
                    </v-row>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                      color="blue darken-1"
                      text
                      @click="save"
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
                <v-card-title class="headline">유저를 삭제하시겠습니까?</v-card-title>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="deleteItemConfirm">삭제</v-btn>
                  <v-btn color="blue darken-1" text @click="closeDelete">취소</v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
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
var item_id;

export default {
  name: "Users",
  // data: function (){
  //   return{
  //     list:['a','b'],
  //   };
  // }
  methods: {
    editItem(item) {
      this.editedIndex = this.information.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.editedItem.password = ''
      this.dialog = true
    },

    deleteItem(item) {
      if (item.name != 'Default Admin') {
        console.log(item.id)
        item_id = item.id
        this.editedIndex = this.information.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      }
    },

    deleteItemConfirm() {
      this.information.splice(this.editedIndex, 1)
      this.closeDelete()
      this.user_Delete(item_id);
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    setPassword(){
      let _self = this;

      console.log('setPassword>')

      let data = {
        id: _self.editedItem.id,
        password: _self.editedItem.password,
        name: _self.editedItem.name,
        username: _self.editedItem.username,
      }

      axios.post('/api/users/setPassword', data
      ).then(function (response) {
        console.log(response.data)
      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    },
    modifyInfo(){
      let _self = this;
      console.log('modifyInfo>')
      let data = {
        id: _self.editedItem.id,
        password: _self.editedItem.password,
        name: _self.editedItem.name,
        username: _self.editedItem.username,
      }
      console.log(data)

      axios.post('/api/users/modifyInfo', data
      ).then(function (response) {
        console.log(response.data)
      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    },
    save() {
      let _self = this

      if(_self.editedItem.username == '' || _self.editedItem.password == '' || _self.editedItem.name == ''){
        return
      }

      if (_self.editedIndex > -1) {
        Object.assign(_self.information[_self.editedIndex], _self.editedItem)
        console.log(_self.editedItem)
        _self.modifyInfo()
        if(_self.editedItem.password != ''){
          _self.setPassword()
        }
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '사용자 정보를 수정 하였습니다.')


        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)

      } else {
        // this.information.push(this.editedItem)
        _self.user_Create(_self.editedItem.name, _self.editedItem.password, _self.editedItem.username)
      }
      _self.editCheck = false
      _self.close()
    },
    user_Delete(id) {
      let _self = this;
      console.log('delete');
      axios.get('/api/users/delete/' + id, {}).then(function (response) {
        const _sleep = (delay) => new Promise((resolve) => setTimeout(resolve, delay));

        const timer = async () => {
          await _sleep(1000);
          console.log('First');
          _self.user_GetAll()
          // await _sleep(1000);
          // console.log('Second');
          _self.$store.commit('SET_ALERT_TYPE', 'success')
          _self.$store.commit('SET_ERROR_CHECK', true)
          _self.$store.commit('SET_ALERT_TEXT', '사용자 삭제 완료')

          setTimeout(function (){
            _self.$store.commit('SET_ERROR_CHECK', false)
          },3000)
        };
        timer();

      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    },

    user_Create(name, password, username) {
      let _self = this;
      console.log('delete');
      let data = {
        name: name,
        password: password,
        username: username,
      }
      axios.post('/api/users/create/user', data
      ).then(function (response) {
        let id = response.data.id

        const _sleep = (delay) => new Promise((resolve) => setTimeout(resolve, delay));

        const timer = async () => {
          await _sleep(1000);
          console.log('First');
          _self.user_DefaultRoleBindings(id);
          _self.user_GetAll()
          // await _sleep(1000);
          // console.log('Second');
          _self.$store.commit('SET_ALERT_TYPE', 'success')
          _self.$store.commit('SET_ERROR_CHECK', true)
          _self.$store.commit('SET_ALERT_TEXT', '사용자 등록 완료')

          setTimeout(function (){
            _self.$store.commit('SET_ERROR_CHECK', false)
          },3000)
        };
        timer();
      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    },

    user_DefaultRoleBindings(id) {
      let _self = this;
      console.log('GlobalRoleBindings ' + id);
      axios.get('/api/users/setGlobalRoleBindings/' + id)
          .then(function (response) {

      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    },

    user_GetOne(userName) {
      let _self = this;
      axios.get('/api/users', {}).then(function (response) {

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        _self.information = json;
        for (let obj of _self.information) {
          console.log(obj);
        }
        // json_list = json;
        // for(var i=0;i<json.length;i++){
        //   console.log(json[i].memory);
        // };

      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    }, // end of methods

    user_GetAll() {
      let _self = this;
      axios.get('/api/users', {}).then(function (response) {

        let jsonStr = JSON.stringify(response.data);
        let json = JSON.parse(jsonStr);

        _self.information = json;
        for (let obj of _self.information) {
          console.log(obj);
        }
        // json_list = json;
        // for(var i=0;i<json.length;i++){
        //   console.log(json[i].memory);
        // };


      }).catch(function (error) {
        console.log(error);
      }).finally(function () {

      });
    }, // end of methods
  },
  data () {
    return {
      dialog: false,
      dialogDelete: false,
      headers: [
        { text: '상태', value: 'state',},
        { text: '이름', value: 'name' },
        { text: '고유ID', value: 'id' },
        { text: '로그인이름', value: 'username' },
        { text: '수정/삭제', value: 'actions', sortable: false },
      ],
      information: [
      ],
      editedIndex: -1,
      editedItem: {
        state: '',
        name: '',
        password: '',
        id: '',
        username: '',
      },
      defaultItem: {
        state: '',
        name: '',
        password: '',
        id: '',
        username: '',
      },
    }
  },
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? '사용자 추가' : '사용자 정보 수정'
    },
    editSpaceUsername(){
      return this.editedItem.username == ''? '빈 칸을 입력해 주세요' : ''
    },
    editSpacePassword(){
      return this.editedItem.password == ''? '빈 칸을 입력해 주세요' : ''
    },
    editSpaceName(){
      return this.editedItem.name == ''? '빈 칸을 입력해 주세요' : ''
    },
  },
  created(){
    let _self = this;
    if(_self.$store.state.userTimer == '') {
      _self.$store.commit('SET_USER_TIMER',setInterval(function (){
        _self.user_GetAll()
      },5000))
    }

    this.$store.commit('SET_LOADING', true)

    // if('' == this.$store.state.bearerToken || '' == this.$store.state.userId){
    //   this.$router.push('/login')
    //   return;
    // }

    axios.get('/api/users',{
    }).then(function (response){

      let jsonStr = JSON.stringify(response.data);
      let json = JSON.parse(jsonStr);

      _self.information = json;
      for (let obj of _self.information) {
        console.log(obj);
      }
      // json_list = json;
      // for(var i=0;i<json.length;i++){
      //   console.log(json[i].memory);
      // };
      _self.$store.commit('SET_LOADING', false)


    }).catch(function (error){
      console.log(error);
    }).finally(function (){

    });
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