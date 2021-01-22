<template>
  <v-navigation-drawer
    id="core-navigation-drawer"
    v-model="drawer"
    :dark="barColor !== 'rgba(228, 226, 226, 1), rgba(255, 255, 255, 0.7)'"
    :expand-on-hover="expandOnHover"
    :right="$vuetify.rtl"
    :src="barImage"
    mobile-break-point="960"
    app
    width="260"
    v-bind="$attrs"
  >
    <template v-slot:img="props">
      <v-img
        :gradient="`to bottom, ${barColor}`"
        v-bind="props"
      />
    </template>

    <v-divider class="mb-1" />

    <v-list
      dense
      nav
    >
      <v-list-item>
        <v-list-item-avatar
          class="align-self-center"
          color="white"
          contain
        >
          <!-- https://demos.creative-tim.com/vuetify-material-dashboard/favicon.ico -->
          <v-img
            src="https://89.98.90.174/assets/images/logos/favicon.ico"
            contain
            height="30px"
          />
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title
            class="display-1"
            v-text="profile.title"
          />
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <v-divider class="mb-2" />

    <v-list
      expand
      nav
    >
      <!-- Style cascading bug  -->
      <!-- https://github.com/vuetifyjs/vuetify/pull/8574 -->
      <div />

      <template v-for="(item, i) in computedItems">
        <base-item-group
          v-if="item.children"
          :key="`group-${i}`"
          :item="item"
        >
          <!--  -->
        </base-item-group>

        <base-item
          v-else
          :key="`item-${i}`"
          :item="item"
        />

      </template>

      <!-- Style cascading bug  -->
      <!-- https://github.com/vuetifyjs/vuetify/pull/8574 -->
      <div />

<!--      <template v-for="(item, i) in $data.items">-->
<!--        <ul style="padding-left: 5%; margin-bottom: 5%; width: 100%; list-style: none;">-->

<!--          <li style="display: inline-block;">-->
<!--            <a @click="expand_contract(item)">-->
<!--              <v-icon v-if="item.childItem!=null && item.childItem.length > 0">{{item.icon}}</v-icon>-->
<!--            </a>-->
<!--          </li>-->

<!--          <li-->
<!--              style="display: inline-block; margin-left: 5%"-->
<!--              v-if="item.childItem!=null && item.childItem.length > 0"-->
<!--          >-->
<!--            <a class="draw_a" :href="item.to">{{item.title}}</a>-->
<!--            <template v-for="(childItem, j) in item.childItem" v-if="item.icon=='mdi-minus'">-->
<!--              <ul style="padding-left: 30%; margin-top: 15%; width: 100%;">-->
<!--                <li style="display:table-cell;">-->
<!--                  <a class="draw_a" :href="childItem.to">{{childItem.title}}</a>-->
<!--                </li>-->
<!--              </ul>-->
<!--            </template>-->
<!--          </li>-->

<!--          <li-->
<!--              style="display: inline-block; margin-left: 16%"-->
<!--              v-else-->
<!--          >-->
<!--            <a class="draw_a" :href="item.to">{{item.title}}</a>-->
<!--            <template v-for="(childItem, j) in item.childItem" v-if="item.icon=='mdi-minus'">-->
<!--              <ul style="padding-left: 30%; margin-top: 15%; width: 100%;">-->
<!--                <li style="display:table-cell;">-->
<!--                  <a class="draw_a" :href="childItem.to">{{childItem.title}}</a>-->
<!--                </li>-->
<!--              </ul>-->
<!--            </template>-->
<!--          </li>-->
<!--        </ul>-->
<!--      </template>-->

    </v-list>

<!--    <template v-slot:append>-->
<!--      <base-item-->
<!--        :item="{-->
<!--          title: $t('upgrade'),-->
<!--          icon: 'mdi-package-up',-->
<!--          to: '/upgrade',-->
<!--        }"-->
<!--      />-->
<!--    </template>-->
  </v-navigation-drawer>
</template>

<script>
  // Utilities
  import {
    mapState,
  } from 'vuex'

  export default {
    name: 'DashboardCoreDrawer',

    props: {
      expandOnHover: {
        type: Boolean,
        default: false,
      },
    },

    data: () => ({
      items: [
        {
          icon: 'mdi-animation',
          // icon: 'mdi-plus',
          title: '클러스터',
          to: '/',
        },
        {
          icon: 'mdi-account',
          // icon: 'mdi-plus',
          title: '유저',
          to: '/pages/users',
        },
        // {
        //   icon: 'mdi-account',
        //   // icon: 'mdi-plus',
        //   title: '카탈로그test',
        //   to: '/pages/catalog',
        // },
        // {
        //   icon: 'mdi-view-dashboard',
        //   title: 'dashboard',
        //   to: '/',
        // },
        // {
        //   icon: 'mdi-account',
        //   title: 'user',
        //   to: '/pages/user',
        // },
        // {
        //   title: 'rtables',
        //   icon: 'mdi-clipboard-outline',
        //   to: '/tables/regular-tables',
        // },
        // {
        //   title: 'typography',
        //   icon: 'mdi-format-font',
        //   to: '/components/typography',
        // },
        // {
        //   title: 'icons',
        //   icon: 'mdi-chart-bubble',
        //   to: '/components/icons',
        // },
        // {
        //   title: 'google',
        //   icon: 'mdi-map-marker',
        //   to: '/maps/google-maps',
        // },
        // {
        //   title: 'notifications',
        //   icon: 'mdi-bell',
        //   to: '/components/notifications',
        // },
      ],
    }),

    computed: {
      ...mapState(['barColor', 'barImage']),
      drawer: {
        get () {
          return this.$store.state.drawer
        },
        set (val) {
          this.$store.commit('SET_DRAWER', val)
        },
      },
      computedItems () {
        return this.items.map(this.mapItem)
      },
      profile () {
        return {
          avatar: true,
          title: this.$t('avatar'),
        }
      },
    },

    methods: {
      mapItem (item) {
        return {
          ...item,
          children: item.children ? item.children.map(this.mapItem) : undefined,
          title: this.$t(item.title),
        }
      },
      // expand_contract(item){
      //   console.log(item.icon)
      //   if(item.icon == 'mdi-plus'){
      //     item.icon = 'mdi-minus'
      //     console.log(item.icon)
      //   }
      //   else if(item.icon == 'mdi-minus'){
      //     item.icon = 'mdi-plus'
      //     console.log(item.icon)
      //   }
      // },
    },
  }
</script>

<style lang="css">
  .draw_a{
    text-decoration: none;
    color: white;
  }

  .draw_a:hover{
    color: white;
  }
</style>

<style lang="sass">
  @import '~vuetify/src/styles/tools/_rtl.sass'


  #core-navigation-drawer
    .v-list-group__header.v-list-item--active:before
      opacity: .24

    .v-list-item
      &__icon--text,
      &__icon:first-child
        justify-content: center
        text-align: center
        width: 20px

        +ltr()
          margin-right: 24px
          margin-left: 12px !important

        +rtl()
          margin-left: 24px
          margin-right: 12px !important

    .v-list--dense
      .v-list-item
        &__icon--text,
        &__icon:first-child
          margin-top: 10px

    .v-list-group--sub-group
      .v-list-item
        +ltr()
          padding-left: 8px

        +rtl()
          padding-right: 8px

      .v-list-group__header
        +ltr()
          padding-right: 0

        +rtl()
          padding-right: 0

        .v-list-item__icon--text
          margin-top: 19px
          order: 0

        .v-list-group__header__prepend-icon
          order: 2

          +ltr()
            margin-right: 8px

          +rtl()
            margin-left: 8px
</style>
