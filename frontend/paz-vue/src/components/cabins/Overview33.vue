<template>
  <div class="ml-3 mr-3">
    <h3>All Cabins Details (managed by component):</h3>
      <Carousel :itemsToShow="4" :key="carouselKey">
        <Slide v-for="cabin in Cabin.cabins" :key="cabin.id">
          <div
              class="cabin-item"
              :class="{ active: this.selectedCabin && this.selectedCabin.id === cabin.id }"
              @click="onSelect(cabin)"
          >
            <img class="image" :src="require(`@/assets/cabins/${cabin.image}`)" alt="Cabin Image" />
            <div class="mt-1">{{ cabin.type }}</div>
            <div class="mt-1">{{ cabin.location ? cabin.location.name : 'No location available' }}</div>
          </div>
        </Slide>
      </Carousel>
  </div>

  <div class="container">
    <button @click="onNewCabinButton();" class="btn-new-cabin">New Cabin</button>
      <div v-if="selectedCabin">
        <router-view
            :selected-cabin="selectedCabin"
            @deleted-cabin="onDeleteEvent"
            @update-cabin="onSaveEvent"
            @cancel-edit-cabin="onCancelEvent"
        />
      </div>
      <div v-else>
        <p class="mt-3">Please select a cabin from the list above</p>
      </div>
  </div>
</template>


<script>
import {Carousel, Slide} from 'vue3-carousel';
import 'vue3-carousel/dist/carousel.css';

import Cabin from "@/models/Cabin";

export default {
  name: "Overview33",
  computed: {
    Cabin() {
      return Cabin
    },
  },
  components: {
    Carousel,
    Slide,
  },
  created() {
    if(Cabin.cabins.length === 0){
      this.lastId = 10000;
      for (let i = 0; i < 8; i++) {
        this.lastId = Cabin.generateSampleCabins(this.lastId, true).id;
      }
    }

    this.$router.replace(this.$route.matched[0].path);
  },
  data: () => ({
    selectedCabin: null,
    cabins: Cabin.cabins,
    lastId: 10000,
    carouselKey: 0 // Increment this value on each iteration when a save is called and using it as a :key forces the carousel to react to changes
  }),

  methods: {
    /**
     * Finds a selected cabin based on the route parameter.
     *
     * Search through the list of cabins and returns the cabin
     * that matches the given ID from the route parameters. If no cabin is found,
     * it returns null.
     *
     * @param {Object} route - The Vue Router $route object containing route information.
     * @returns {Cabin|null} The cabin object that matches the ID, or null if not found.
     */
    findSelectedFormRouteParam(route) {
      return Cabin.cabins.find(cabin => cabin.id === Number(route.params.id)) || null;
    },

    /**
     * Handles the selection of a cabin and navigates to its detail view.
     *
     * Check if the selected cabin is different from the currently
     * selected cabin. If so, it navigates to the detail page of the newly selected
     * cabin. If the same cabin is selected again, it replaces the current route
     * with the overview path.
     *
     * @param {Cabin} cabin - The cabin object that has been selected.
     * @returns {Cabin|null} The currently selected cabin or null if none is selected.
     */
    onSelect(cabin) {
      if(cabin !== null && cabin.id !== this.selectedCabin?.id){
        this.$router.push({ path: `${this.$route.matched[0].path}/${cabin.id}` });
      } else if (this.selectedCabin !== null) {
        this.$router.replace({ path: this.$route.matched[0].path });
      }

      return this.selectedCabin;
    },

    /**
     * Deletes a cabin from the list and navigates back to the overview page.
     *
     * Remove the specified cabin from the `Cabin.cabins` array by filtering
     * out the cabin with a matching ID. After deletion, it replaces the current route
     * with an overview path to show the updated list.
     *
     * @param {Cabin} cabinToDelete - The cabin object to be deleted.
     */
    onDeleteEvent(cabinToDelete) {
      Cabin.cabins = Cabin.cabins.filter(cabin => cabin.id !== cabinToDelete.id);
      this.$router.replace({path: this.$route.matched[0].path});
    },

    /**
     * Cancels editing the cabin and navigates back to the overview page.
     *
     * Replace the current route with the overview path, effectively
     * canceling any ongoing operations and returning the user to the previous view.
     */
    onCancelEvent() {
      this.$router.replace({path: this.$route.matched[0].path});
    },

    /**
     * Saves the updated cabin information and navigates back to the overview page.
     *
     * Find the cabin to update in the list by its ID and replaces
     * the existing cabin with the updated information. After saving the update,
     * it increments the `carouselKey` to trigger any necessary UI updates and
     * then navigates back to the overview path.
     *
     * @param {Cabin} cabinToUpdate - The cabin object containing updated information.
     */
    onSaveEvent(cabinToUpdate) {
      const index = this.Cabin.cabins.findIndex(cabin => cabin.id === cabinToUpdate.id);
      if (index !== -1) {
        this.Cabin.cabins.splice(index, 1, cabinToUpdate);
      }
      this.carouselKey += 1;

      // this.$router.replace({path: this.$route.matched[0].path});
    },

    /**
     * Creates a new cabin and navigates to its detail page.
     *
     * Generate a new cabin using the provided last ID and a boolean
     * flag to determine if the cabin was created during the first run. After creating the cabin,
     * it updates the `selectedCabin` and navigates to the detail page of the newly created cabin.
     *
     */
    onNewCabinButton() {
      this.selectedCabin = Cabin.generateSampleCabins();
      this.$router.replace({path: this.$route.matched[0].path + "/" + this.selectedCabin.id});
    },
  },

  watch: {
    // Everytime the $route values get updated run the findSelectedFormRouteParam()
    // method to return a cabin object
    '$route'() {
      this.selectedCabin = this.findSelectedFormRouteParam(this.$route);
    }
  }
};
</script>


<style scoped>


.active {
  background-color: rgba(251, 112, 0, 0.35);
  border: 1px solid brown;
}
</style>