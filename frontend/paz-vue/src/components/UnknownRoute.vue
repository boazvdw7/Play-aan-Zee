<template>
  <br>
  <div class="error p-3">
    <h1>Page not found</h1>
    <h2> There is no component available at this route: {{ $route.path }}</h2>
    <h3> Error: {{ $route.name }} (Error Code: 404)</h3>
  </div>
</template>

<script setup>

</script>

<style scoped>
.error {
  text-align: center;
  background-color: #f36b7a;
}
</style>