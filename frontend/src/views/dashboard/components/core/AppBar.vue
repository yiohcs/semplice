<template>
  <v-app-bar
    id="app-bar"
    absolute
    app
    color="transparent"
    flat
    height="75"
  >
    <v-btn
      class="mr-3"
      elevation="1"
      fab
      small
      @click="setDrawer(!drawer)"
    >
      <v-icon v-if="value">
        mdi-view-quilt
      </v-icon>

      <v-icon v-else>
        mdi-dots-vertical
      </v-icon>
    </v-btn>

    <v-toolbar-title
      class="hidden-sm-and-down font-weight-light"
      v-text="$route.name"
    />

    <v-spacer />

<!--    <v-text-field-->
<!--      :label="$t('search')"-->
<!--      color="secondary"-->
<!--      hide-details-->
<!--      style="max-width: 165px;"-->
<!--    >-->
<!--      <template-->
<!--        v-if="$vuetify.breakpoint.mdAndUp"-->
<!--        v-slot:append-outer-->
<!--      >-->
<!--        <v-btn-->
<!--          class="mt-n2"-->
<!--          elevation="1"-->
<!--          fab-->
<!--          small-->
<!--        >-->
<!--          <v-icon>mdi-magnify</v-icon>-->
<!--        </v-btn>-->
<!--      </template>-->
<!--    </v-text-field>-->

    <div class="mx-3" />

<!--    <v-btn-->
<!--      class="ml-2"-->
<!--      min-width="0"-->
<!--      text-->
<!--      to="/"-->
<!--    >-->
<!--      <v-icon>mdi-view-dashboard</v-icon>-->
<!--    </v-btn>-->

    <v-menu
      bottom
      left
      offset-y
      origin="top right"
      transition="scale-transition"
    >
<!--      <template v-slot:activator="{ attrs, on }">-->
<!--        <v-btn-->
<!--          class="ml-2"-->
<!--          min-width="0"-->
<!--          text-->
<!--          v-bind="attrs"-->
<!--          v-on="on"-->
<!--        >-->
<!--          <v-badge-->
<!--            color="red"-->
<!--            overlap-->
<!--            bordered-->
<!--          >-->
<!--            <template v-slot:badge>-->
<!--              <span>5</span>-->
<!--            </template>-->

<!--            <v-icon>mdi-bell</v-icon>-->
<!--          </v-badge>-->
<!--        </v-btn>-->
<!--      </template>-->

      <v-list
        :tile="false"
        nav
      >
        <div>
          <v-list-item-content>
            <v-list-item-title align="center">Login User</v-list-item-title>
            <v-list-item-subtitle align="center">Admin</v-list-item-subtitle>
          </v-list-item-content>
          <v-divider></v-divider>

          <app-bar-item
            v-for="(n, i) in notifications"
            :key="`item-${i}`"
          >
            <v-list-item-title v-text="n" @click="logout"/>
          </app-bar-item>
        </div>
      </v-list>

      <template v-slot:activator="{ attrs, on }">
        <v-btn
            class="ml-2"
            min-width="0"
            text
            v-bind="attrs"
            v-on="on"
        >
          <v-icon>mdi-account</v-icon>
        </v-btn>
      </template>
    </v-menu>


  </v-app-bar>
</template>

<script>
  // Components
  import { VHover, VListItem } from 'vuetify/lib'
  const axios = require('axios')

  // Utilities
  import { mapState, mapMutations } from 'vuex'

  export default {
    name: 'DashboardCoreAppBar',
    components: {
      AppBarItem: {
        render (h) {
          return h(VHover, {
            scopedSlots: {
              default: ({ hover }) => {
                return h(VListItem, {
                  attrs: this.$attrs,
                  class: {
                    'black--text': !hover,
                    'white--text secondary elevation-12': hover,
                  },
                  props: {
                    activeClass: '',
                    dark: hover,
                    link: true,
                    ...this.$attrs,
                  },
                }, this.$slots.default)
              },
            },
          })
        },
      },
    },

    props: {
      value: {
        type: Boolean,
        default: false,
      },
    },

    data: () => ({
      notifications: [
        'Logout',
      ],
    }),

    computed: {
      ...mapState(['drawer']),
    },

    methods: {
      ...mapMutations({
        setDrawer: 'SET_DRAWER',
      }),

      getToken(){
        let _self = this
        let check = false

        if(_self.$store.state.bearerToken == '' || _self.$store.state.bearerToken == null){
          _self.$router.push('/login')
          return
        }

        axios.get('/api/login/getBearerToken',{}
        ).then(function (response){
          console.log(response.data)
          if(response.data == null){
            check = true
            return
          }
        }).catch(function (error){
        }).finally(function (){

        });
        if(check)
          return

        axios.get('/api/login/getToken',{}
        ).then(function (response){
          console.log("getToken")
          console.log(response.data)

          for(let i=0;i<response.data.length;i++){
            console.log(response.data[i].userId)
            console.log(_self.$store.state.userId)
            if(response.data[i].description == 'LoginSession' && response.data[i].userId == _self.$store.state.userId){
              console.log("remove..." + i)
              if(response.data[i].name != _self.$store.state.name){
                _self.removeToken(response.data[i].name)
              }
            }else{
              console.log("pass..." + i)
            }

            if(i == response.data.length-1){
              _self.$store.commit('SET_TOKEN', '')
              _self.$store.commit('SET_USERID', '')
              _self.$store.commit('SET_NAME', '')

              _self.$router.push('/login')
            }
          }

        }).catch(function (error){
          _self.$store.commit('SET_TOKEN', '')
          _self.$store.commit('SET_USERID', '')
          _self.$store.commit('SET_NAME', '')
          _self.$router.push('/login')
          console.log(error);
        }).finally(function (){

        });
      },

      removeToken(name){
        let _self = this
        console.log("removeToken >>>> " + name)
        axios.get('/api/login/removeToken/'+ name,{}
        ).then(function (response){
          console.log("removeToken response >>>>> ")
          console.log(response.data)

        }).catch(function (error){
          console.log(error);
        }).finally(function (){

        });
      },

      logout(){
        this.getToken()


      },
    },
  }
</script>
