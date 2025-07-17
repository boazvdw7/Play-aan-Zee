<template>
  <header-component></header-component>
  <nav-bar></nav-bar>
  <router-view></router-view>
</template>

<script>
import HeaderComponent from "@/components/HeaderComponent.vue";
import NavBar from "@/components/NavBar.vue";
import CONFIG from '../app-config.js'
import {RESTAdaptorWithFetch} from "@/services/RESTAdaptor";
import Cabin from "@/models/Cabin";
import {CachedRESTAdaptorWithFetch} from "@/services/CachedRESTAdaptor";
import {reactive} from "vue";
import Location from "@/models/Location";
import {SessionSbService} from "@/services/SessionSbService";

export default {
  name: "App",
  components: {'header-component': HeaderComponent, 'nav-bar': NavBar},
  provide() {
    return {
      // stateless data services adaptor singletons
      cabinsService: new RESTAdaptorWithFetch(CONFIG.BACKEND_URL + "/cabins", Cabin.copyConstructor),
      cachedCabinService: reactive(
          new CachedRESTAdaptorWithFetch(CONFIG.BACKEND_URL + "/cabins", Cabin.copyConstructor)),
      locationsService: new RESTAdaptorWithFetch(CONFIG.BACKEND_URL + "/locations", Location.copyConstructor)
      // usersService: new RESTAdaptorWithFetch(CONFIG.BACKEND_URL+"/users", User.copyConstructor),    }
    }
  },
}
</script>

<style scoped>

</style>