<template>
  <header-sb-component></header-sb-component>
  <nav-bar-sb></nav-bar-sb>
  <router-view></router-view>
</template>

<script>
import HeaderComponent from "@/components/HeaderSB.vue";
import NavBarSb from "@/components/NavBarSB.vue";
import CONFIG from '../app-config.js'
import {RESTAdaptorWithFetch} from "@/services/RESTAdaptor";
import Cabin from "@/models/Cabin";
import {CachedRESTAdaptorWithFetch} from "@/services/CachedRESTAdaptor";
import {reactive, shallowReactive} from "vue";
import Location from "@/models/Location";
import {SessionSbService} from "@/services/SessionSbService";
import {FetchInterceptor} from "@/services/FetchInterceptor";

export default {
  name: "App",
  components: {'header-sb-component': HeaderComponent, 'nav-bar-sb': NavBarSb},
  provide() {
    this.theSessionService = shallowReactive(new SessionSbService(
        CONFIG.BACKEND_URL + "/authentication", CONFIG.JWT_STORAGE_ITEM));
    this.theFetchInterceptor =
        new FetchInterceptor(this.theSessionService, this.$router)
    return {
      // stateless data services adaptor singletons
      sessionService: this.theSessionService,
      cabinsService: new RESTAdaptorWithFetch(
          CONFIG.BACKEND_URL + "/cabins", Cabin.copyConstructor),
      cachedCabinService: reactive(
          new CachedRESTAdaptorWithFetch(
              CONFIG.BACKEND_URL + "/cabins", Cabin.copyConstructor)),
      locationsService: new RESTAdaptorWithFetch(
          CONFIG.BACKEND_URL + "/locations", Location.copyConstructor),
    }
  },

  unmounted() {
    console.log("App.unmounted() has been called.");
    this.theFetchInterceptor.unregister();
  }
}
</script>

<style scoped>

</style>