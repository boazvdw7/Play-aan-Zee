<template>
  <div class="cabin-detail" v-bind="$attrs">
    <div v-if="selectedCabin && selectedCabin.location">
      <div class="thead"><h3>Cabin Details (ID: {{ selectedCabin.id }})</h3></div>
      <form>
        <table>
          <tbody>
          <tr>
            <td><label for="type">Type:</label></td>
            <td>
              <select id="type" v-model="selectedCabin.type">
                <option v-for="(type) of Object.values(CabinType)" :key="type" :value="type">{{ type }}</option>
              </select>
            </td>
          </tr>
          <tr>
            <td><label for="location">Location:</label></td>
            <td>
              <select id="location" v-model="locationUpdater">
                <option v-for="loc of locations" :key="loc.id" :value="loc.id">{{ loc.name }}</option>
              </select>
              {{ selectedCabin.location.country }}
            </td>
          </tr>
          <tr>
            <td><label for="image">Description:</label></td>
            <td><input type="text" id="description" v-model="selectedCabin.description" /></td>
          </tr>
          <tr>
            <td><label for="image">Image:</label></td>
            <td>
              <select v-model="selectedCabin.image">
                <option v-for="img in images" :key="img" :value="img" >
                  {{ img }}
                </option>
              </select>
            </td>
          </tr>
          <tr>
            <td><label for="image">Price per week:</label></td>
            <td><input type="text" id="image" v-model="selectedCabin.pricePerWeek" /></td>
          </tr>
          <tr>
            <td><label for="image">Total availability:</label></td>
            <td><input type="text" id="image" v-model="selectedCabin.numAvailable" /></td>
          </tr>
          </tbody>
        </table>
      </form>
      <button @click="onDeleteButton(selectedCabin);" class="btn-new-cabin">Delete</button>
    </div>
    <div v-else>
      <p>Please select a cabin from the list above</p>
    </div>
  </div>
  <br>
</template>

<script>
import Location from "@/models/Location";
import Cabin from "@/models/Cabin";

export default {
  name: "CabinsDetail32",
  props: ['selectedCabin'],
  emits: ['deletedCabin'],
  data() {
    return {
      CabinType: Cabin.Type,
      images: Cabin.images,
      locations: Location.samples,
    }
  },
  computed: {
    locationUpdater: {
      get() {
        return this.selectedCabin.location.id;
      },
      set(id) {
        this.selectedCabin.location = this.locations.find(loc => loc.id === id);
      }
    }
  },
  methods: {
    onDeleteButton(selectedCabin) {
      this.$emit('deletedCabin', selectedCabin);
    },
  }
};
</script>

<style scoped>
.thead {
  background-color: orange;
  width: 100%;
  text-align: center;
  margin: 0;
}

h3 {
  margin: 0;
  padding: 1%;
}

.cabin-detail {
  margin-top: 20px;
}

input {
  width: 80%;
}

form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

input, select {
  padding: 5px;
  font-size: 14px;
}

table thead th {
  background-color: orange;
  color: white;
  padding: 10px;
  text-align: left;
}

table, th, td {
  border: 1px solid black;
  border-collapse: collapse;
}

table td:first-child {
  background-color: #f8d7da;
  padding: 10px;
}

table td {
  padding: 10px;
  text-align: left;
}

table {
  width: 100%;
  border-spacing: 0;
}
</style>