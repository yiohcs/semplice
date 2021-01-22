<template>
  <div>
    <v-alert id="alert_Message" v-text="$store.state.alertText" :type="$store.state.alertType" style="z-index: 999; position: absolute; right: 0; top: 0;" :value="$store.state.errorCheck">
    </v-alert>
    <router-view />
    <clipLoader :loading="$store.state.loader" size="100px" style="z-index: 999; position: absolute; left: 45%; top:40%;" />

  </div>
</template>

<script>
import clipLoader from 'vue-spinner/src/ClipLoader'

export default {
  name: 'App',
  components:{
    clipLoader,
  }
}
</script>