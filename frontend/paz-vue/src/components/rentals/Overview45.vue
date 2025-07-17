<template>
  <div class="ml-3 mr-3">
    <h3>1. Select a cabin for your reservation:</h3>
      <Carousel :itemsToShow="4" :key="carouselKey">
        <Slide v-for="cabin in this.cabins" :key="cabin.id">
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
      <div v-if="selectedCabin">
        <router-view
            :selected-cabin="selectedCabin"
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
  name: "RentalOverview45",
  emits: ["refreshCabins"],
  inject: {
    cabinsService: {from: 'cabinsService'},
    locationService: {from: 'locationsService'}
  },
  computed: {
    Cabin() {
      return Cabin
    },
  },
  components: {
    Carousel,
    Slide,
  },
  async created() {
    if(this.cabins.length === 0){
      this.cabins = await this.cabinsService.asyncFindAll();
      this.selectedCabin = this.findSelectedFormRouteParam(this.$route);
    }

    this.$router.replace(this.$route.matched[0].path);
  },
  data: () => ({
    selectedCabin: null,
    cabins: [],
    lastId: 10000,
    carouselKey: 0 // Increment this value on each iteration when a save is called and using it as a :key forces the carousel to react to changes
  }),

  methods: {
    // Refresh the cabin list
    async onRefreshCabins() {
      this.cabins = await this.cabinsService.asyncFindAll();
    },

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
      return this.cabins.find(cabin => cabin.id === Number(route.params.id)) || null;
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
    async onSelect(cabin) {
      let foundCabin = await this.cabinsService.asyncFindById(cabin.id);

      if(foundCabin !== null && foundCabin.id !== this.selectedCabin?.id){
        this.$router.push({ path: `${this.$route.matched[0].path}/${foundCabin.id}` });
      } else if (this.selectedCabin !== null) {
        this.$router.replace({ path: this.$route.matched[0].path });
      }

      await this.onRefreshCabins();

      return this.selectedCabin;
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