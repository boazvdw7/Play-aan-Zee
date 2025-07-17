<template>
  <br>
  <div class="error p-3">
    <h1>Oops, an error occurred...</h1>
    <h2> Request-url = {{ FetchInterceptor.href }}</h2>
    <h2 v-if="FetchInterceptor.responseStatus === 401"> Response status code: {{ FetchInterceptor.responseStatus }}</h2>
    <h2 v-else> Response status code: 404 </h2>
    <h2 v-if="FetchInterceptor.responseStatus === 401">Error Message = Unauthorized: No token provided. You need to logon first.</h2>
    <h2 v-else>Error Message = Not Found: Cabin-Id = {{ regexUrl() }}</h2>
  </div>
</template>

<script>
import {FetchInterceptor} from "@/services/FetchInterceptor";
import {defineComponent} from "vue";

export default defineComponent({
  computed: {
    FetchInterceptor() {
      return FetchInterceptor
    }
  },

  data() {
    return{
      href: "",
      match: ""
    }
  },

  methods: {
    regexUrl(){
      const regex = /\b\w+$/;
      return regex.exec(FetchInterceptor.href).toString();
    }
  },

  mounted() {
    this.href = window.location.href;
    setTimeout(() => {
      FetchInterceptor.theInstance.router.push({ name: "signOut" });
    }, 5000);
  }
})


</script>

<style scoped>
.error {
  text-align: center;
  background-color: #f36b7a;
}
</style>