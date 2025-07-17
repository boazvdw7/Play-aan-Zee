<template>
  <div class="container">
    <div class="justify-content-center">
      <h3>All cabins overview:</h3>
      <div>
        <table :key="tableValue">
          <thead>
          <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Location</th>
            <th>Price per Week</th>
            <th>Description</th>
            <th>Total Available</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="cabin in Cabin.cabins" :key="cabin.id">
            <td>{{ cabin.id }}</td>
            <td>{{ cabin.type }}</td>
            <td>{{ cabin.location.name }}</td>
            <td>€ {{ cabin.pricePerWeek }}</td>
            <td v-if="cabin.type !== 'BeachGear'">{{ cabin.description }}</td>
            <td v-else>No description available</td>
            <td>{{ cabin.numAvailable }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="new-cabin_div mt-3">
      <button class="btn-new-cabin" @click="onNewCabinButton">New Cabin</button>
    </div>
  </div>
</template>

<script>
import Cabin from "@/models/Cabin";

export default {
  name: "CabinsOverview31",

  computed: {
    Cabin() {
      return Cabin
    }
  },

  created() {
    if(Cabin.cabins.length === 0){
      this.lastId = 10000;
      for (let i = 0; i < 8; i++) {
        this.lastId = Cabin.generateSampleCabins(this.lastId, true).id;
      }
    }
  },
  data: () => ({
      cabins: Cabin.cabins,
      tableValue: 0,
      lastId: Cabin.lastId,
  }),
  methods: {
    onNewCabinButton() {
      this.lastId = Cabin.generateSampleCabins(this.lastId, false).id;
      this.tableValue += 1;
    },
  }
}
</script>

<style scoped>
.justify-content-center {
  justify-content: center;
}

.btn-new-cabin {
  background-color: orange;
  color: red;
}

table {
  width: 100%;
}

table, th, td {
  font-family: Arial, serif;
  border: 1px solid black;
  border-collapse: collapse;
}

th {
  background-color: orange;
}

.new-cabin_div {
  float: right;
  background-color: yellow;
  color: red;
}
</style>