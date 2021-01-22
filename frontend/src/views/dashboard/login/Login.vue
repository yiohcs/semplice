<template>
  <v-container
    id="user-login"
    fluid
    tag="section"
  >
    <v-card style="width: 50%;">

    <v-row style="padding-left: 1%; padding-right: 1%;" justify="center">
      <v-col
          cols="12"
          md="12"
      >

        <validation-observer
        ref="observer"
        v-slot="{ invalid }"
        >
        <form @submit.prevent="submit">
          <validation-provider
              name="UserName"
              rules="required|max:10"
          >
            <v-text-field
                v-model="username"
                label="로그인 ID"
                required
            ></v-text-field>
          </validation-provider>
          <validation-provider
              name="Password"
          >
            <v-text-field
                v-model="password"
                type="password"
                label="비밀번호"
                required
            ></v-text-field>
          </validation-provider>

          <v-btn
              class="mr-4"
              type="submit"
              width="100%"
          >
            로그인
          </v-btn>
        </form>
        </validation-observer>
      </v-col>
    </v-row>
<!--      <v-alert-->
<!--          dense-->
<!--          outlined-->
<!--          v-if="errorCheck"-->
<!--          type="error"-->
<!--          style="padding-left: 10px;"-->
<!--      >-->
<!--        ID 또는 비밀번호를 확인해 주세요.-->
<!--      </v-alert>-->
    </v-card>


  </v-container>
</template>

<script>
const axios = require('axios');

import { required, digits, email, max, regex } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate'

setInteractionMode('eager')

extend('required', {
  ...required,
  message: '{_field_} can not be empty',
})

export default {
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  data: () => ({
    username: '',
    password: '',
    errorCheck: false,

  }),
  created() {
    this.$store.commit('SET_LOADING', false)
  },
  methods: {


    setUserTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/users/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    setClusterTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/clusters/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },
    setCatalogTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/catalog/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    setLoginTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/login/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    setProjectTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/projects/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    setNodeTokenAndId(){
      let userData = {
        bearerToken: this.$store.state.bearerToken,
        userId: this.$store.state.userId,
        name: this.$store.state.name,
      }
      axios.post('/api/nodes/setTokenAndId/',userData
      ).then(function (response){

      }).catch(function (error){
        console.log(error);
      }).finally(function (){

      });
    },

    setTokenAndId(){
      this.setUserTokenAndId()
      this.setClusterTokenAndId()
      this.setLoginTokenAndId()
      this.setCatalogTokenAndId()
      this.setProjectTokenAndId()
      this.setNodeTokenAndId()
    },

    submit (res) {
      let _self = this
      _self.$store.commit('SET_LOADING', true)
      // this.$refs.observer.validate()
      console.log(res);
      console.log(res.target[0].value);
      console.log(res.target[1].value);
      let data = {
        username: res.target[0].value,
        password: res.target[1].value,
      }
      axios.post('/api/users/loginUser',data
      ).then(function (response){
        console.log(response);
        console.log(response.data.token);
        console.log(response.data.userId);
        console.log(response.data.name);

        if(response.data == '' || response.data == undefined){
          _self.$store.commit('SET_LOADING', false)
          // _self.errorCheck = true

          _self.$store.commit('SET_ALERT_TYPE', 'error')
          _self.$store.commit('SET_ERROR_CHECK', true)
          _self.$store.commit('SET_ALERT_TEXT', 'ID 또는 비밀번호가 틀렸습니다.')

          setTimeout(function (){
            _self.$store.commit('SET_ERROR_CHECK', false)
          },3000)
          return
        }

        _self.$store.commit('SET_TOKEN', response.data.token)
        _self.$store.commit('SET_USERID', response.data.userId)
        _self.$store.commit('SET_NAME', response.data.name)


        let today = new Date()
        let loginTime = today.getTime()
        console.log('time: ' + today.getTime())

        _self.$store.commit('SET_TIME', loginTime)

        console.log(_self.$store.state.bearerToken)
        console.log(_self.$store.state.userId)
        console.log(_self.$store.state.name)

        _self.setTokenAndId()

        if(_self.$store.state.bearerToken != null && _self.$store.state.bearerToken != ''){
          _self.$store.commit('SET_LOADING', false)
          _self.$router.push('/')
        }

      }).catch(function (error){
        _self.$store.commit('SET_LOADING', false)
        _self.$store.commit('SET_ERROR_CHECK', true)
        _self.$store.commit('SET_ALERT_TEXT', '네트워크를 확인해주세요.')

        setTimeout(function (){
          _self.$store.commit('SET_ERROR_CHECK', false)
        },3000)
        // _self.errorCheck = true
        console.log(error);
      }).finally(function (){

      });
    },
    clear () {
      this.username = ''
      this.password = ''
      this.$refs.observer.reset()
    },
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