import Vue from 'vue'
import Router from 'vue-router'
import store from "./store";
import cluster from "./views/dashboard/pages/Cluster";

Vue.use(Router)

// export default new Router({
//   mode: 'history',
//   base: process.env.BASE_URL,
//   routes: [
//     {
//       path: '/',
//       component: () => import('@/views/dashboard/Index'),
//       children: [
//         // Cluster
//         {
//           name: 'Cluster',
//           path: '',
//           component: () => import('@/views/dashboard/pages/Cluster'),
//         },
//         {
//           name: 'Users',
//           path: '/pages/users',
//           component: () => import('@/views/dashboard/pages/Users'),
//         },
//         // // Dashboard
//         // {
//         //   name: 'Dashboard',
//         //   path: '',
//         //   component: () => import('@/views/dashboard/Dashboard'),
//         // },
//         // // Pages
//         // {
//         //   name: 'User Profile',
//         //   path: 'pages/user',
//         //   component: () => import('@/views/dashboard/pages/UserProfile'),
//         // },
//         // {
//         //   name: 'Notifications',
//         //   path: 'components/notifications',
//         //   component: () => import('@/views/dashboard/component/Notifications'),
//         // },
//         // {
//         //   name: 'Icons',
//         //   path: 'components/icons',
//         //   component: () => import('@/views/dashboard/component/Icons'),
//         // },
//         // {
//         //   name: 'Typography',
//         //   path: 'components/typography',
//         //   component: () => import('@/views/dashboard/component/Typography'),
//         // },
//         // // Tables
//         // {
//         //   name: 'Regular Tables',
//         //   path: 'tables/regular-tables',
//         //   component: () => import('@/views/dashboard/tables/RegularTables'),
//         // },
//         // // Maps
//         // {
//         //   name: 'Google Maps',
//         //   path: 'maps/google-maps',
//         //   component: () => import('@/views/dashboard/maps/GoogleMaps'),
//         // },
//         // // Upgrade
//         // {
//         //   name: 'Upgrade',
//         //   path: 'upgrade',
//         //   component: () => import('@/views/dashboard/Upgrade'),
//         // },
//       ],
//     },
//     {
//       path: '/login',
//       component: () => import('@/views/dashboard/login/Login'),
//     },
//   ],
//
// })

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/login',
      component: () => import('@/views/dashboard/login/Login'),
    },
    {
      path: '/',
      component: () => import('@/views/dashboard/Index'),
      children: [
        // Cluster
        {
          name: '클러스터',
          path: '',
          component: () => import('@/views/dashboard/pages/Cluster'),
        },
        {
          name: '유저',
          path: '/pages/users',
          component: () => import('@/views/dashboard/pages/Users'),
        },
        {
          name: '카탈로그test',
          path: '/pages/catalog',
          component: () => import('@/views/dashboard/pages/Catalog'),
        },
        {
          path: '/c/:cpid',
          component: () => import('@/views/dashboard/pages/Detail'),
        },
        {
          path: '/c/:cpid/apps',
          component: () => import('@/views/dashboard/pages/Catalog'),
        },
      ],
    },
  ],
  create(){
    this.cpid = this.$route.params.cpid;
  },
})


router.beforeEach(function (to, from, next) {

  console.log('router >>>> ' + store.state.bearerToken)
  console.log('router >>>> ' + store.state.userId)

  clearInterval(store.state.clusterTimer)
  clearInterval(store.state.userTimer)
  clearInterval(store.state.nodeTimer)
  clearInterval(store.state.appTimer)
  store.state.clusterTimer = ''
  store.state.userTimer = ''
  store.state.nodeTimer = ''
  store.state.appTimer = ''

  console.log(store.state.clusterTimer)
  console.log(store.state.userTimer)
  console.log(store.state.nodeTimer)
  console.log(store.state.appTimer)

  let time = new Date()
  let nowTime = time.getTime()

  if(store.state.loginTime != '' && nowTime - store.state.loginTime >= 7200000){
    store.commit('SET_TOKEN', '')
    store.commit('SET_USERID', '')
    store.commit('SET_NAME', '')
    store.commit('SET_TIME', '')
    next('/login')
  }
  else if (to.path=='/login'){
    if(store.state.bearerToken != '' && store.state.bearerToken != null){
      next('/')
    }else{
      next()
    }

  }
  else{
    if(store.state.bearerToken == '' || store.state.bearerToken == null){
      next('/login')
    }else if(to.path=='/'){
      next()
    }else{
      next()
    }
  }

})



export default router
