<template>
  <div class="ml-3 mr-3">
    <h3>All Cabins Details (Rental Info-backend):</h3>
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
    <button @click="onNewCabinButton(this.lastId, false);" class="btn-new-cabin">New Cabin</button>
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
import Location from "@/models/Location";
import {RESTAdaptorInterface} from "@/interfaces/RestAdaptorInterface";

export default {
  name: "Overview45",
  emits: ["refreshCabins"],
  inject: {
    cachedCabinsService: {from: 'cachedCabinService'},
    cachedLocationService: {from: 'locationsService'}
  },
  computed: {
    Cabin() {
      return this.cachedCabinsService.entities;
    },
  },
  components: {
    Carousel,
    Slide,
  },
  async created() {
    if(this.cabins.length === 0){
      this.cabins = await this.cachedCabinsService.asyncFindAll();
    }
    this.selectedCabin = this.findSelectedFormRouteParam(this.$route);
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
      this.cabins = await this.cachedCabinsService.asyncFindAll();
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
      // let foundCabin = await this.cachedCabinsService.asyncFindById(cabin.id);
      console.log(cabin)
      if(cabin !== null && cabin.id !== this.selectedCabin?.id){
        this.$router.push({ path: `${this.$route.matched[0].path}/${cabin.id}` });
      } else {
        this.$router.replace({ path: this.$route.matched[0].path });
      }

      await this.onRefreshCabins();

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
    async onDeleteEvent(cabinToDelete) {
      await this.cachedCabinsService.asyncDeleteById(cabinToDelete.id);

      this.$router.replace({path: this.$route.matched[0].path});

      await this.onRefreshCabins();
    },

    /**
     * Cancels the current operation and navigates back to the overview page.
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
     * it increments the `carouselKey` to trigger UI updates and
     * then navigates back to the overview path.
     *
     * @param {Cabin} cabinToUpdate - The cabin object containing updated information.
     */
    async onSaveEvent(cabinToUpdate) {
      const index = this.cabins.findIndex(foundCabin => foundCabin.id === cabinToUpdate.id);

      if (index !== -1) {
        await this.cachedCabinsService.asyncSave(cabinToUpdate);
      }

      this.carouselKey += 1;

      await this.onRefreshCabins();
    },

    /**
     * Creates a new cabin and navigates to its detail page.
     *
     * Generate a new cabin using the provided last ID and a boolean
     * flag to determine if the cabin was created during the first run. After creating the cabin,
     * it updates the `selectedCabin` and navigates to the detail page of the newly created cabin.
     *
     */
    async onNewCabinButton() {
      let loc = await this.cachedLocationService.asyncFindAll();
      let locOne =  loc[0]

      let newCabin = Cabin.createRandomCabin(0, locOne);

      this.selectedCabin = await this.cachedCabinsService.asyncSave(newCabin);

      await this.onRefreshCabins();

      if(this.selectedCabin !== null) {
        this.$router.replace({path: this.$route.matched[0].path + "/" + this.selectedCabin.id});
      }
    },
  },

  watch: {
    // Everytime the $route values get updated run the findSelectedFormRouteParam()
    // method to return a cabin object
    '$route'() {
      this.selectedCabin = this.findSelectedFormRouteParam(this.$route);

      // Super lelijke fix om responses op te vangen,
      // omdat wij cabins selecteren op basis van een
      // @click event en niet de route, ik probeerde
      // de route te fiksen maar dat lukte niet.
      if(this.$route.params.id) {
        this.cachedCabinsService.asyncFindById(this.$route.params.id)
            .then((cabin) => {

            })
            .catch((error) => {
              this.$router.replace({path: "/error"});

            });
        this.onRefreshCabins();
      }
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