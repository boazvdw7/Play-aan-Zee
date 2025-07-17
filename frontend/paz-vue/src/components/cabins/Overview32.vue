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
            <div class="mt-2">{{ cabin.type }}</div>
            <div class="mt-1">{{ cabin.location ? cabin.location.name : 'No location available' }}</div>
          </div>
        </Slide>
      </Carousel>
    </div>

  <div class="container">
    <button @click="onNewCabinButton(Cabin.lastId, false);" class="btn-new-cabin">New Cabin</button>
    <app-cabins-detail v-model:selected-cabin="selectedCabin" @deleted-cabin="onDeleteEvent"/>
  </div>
</template>

<script>
import {Carousel, Slide} from 'vue3-carousel';
import 'vue3-carousel/dist/carousel.css';

import Cabin from "@/models/Cabin";
import CabinsDetail from "@/components/cabins/CabinsDetail32.vue";

export default {
  name: "Overview32",
  computed: {
    Cabin() {
      return Cabin
    }
  },
  components: {
    'app-cabins-detail': CabinsDetail,
    Carousel,
    Slide,
  },
  created() {
    this.lastId = 10000;
    if(Cabin.cabins.length === 0){
      for (let i = 0; i < 8; i++) {
        this.lastId = Cabin.generateSampleCabins(this.lastId, true).id;
      }
    }
  },
  data: () => ({
      selectedCabin: null,
      cabins: Cabin.cabins,
      lastId: 10000,
      carouselKey: 0 // Increment this value on each iteration when a save is called and using it as a :key forces the carousel to react to changes
  }),
  methods: {
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

      this.selectedCabin = null;
    },

    /**
     * Handles the selection of a cabin and navigates to its detail view.
     *
     * Check if the selected cabin is different from the currently
     * selected cabin. If so, it navigates to the detail page of the newly selected
     * cabin. If the same cabin is selected again, it replaces the current route
     * with the overview path.
     *
     * @param {Cabin} selectedCabin - The cabin object that has been selected.
     * @returns {Cabin|null} The currently selected cabin or null if none is selected.
     */
    onSelect(selectedCabin) {
      if (this.selectedCabin && this.selectedCabin.id === selectedCabin.id) {
        this.selectedCabin = null;
      } else {
        this.selectedCabin = selectedCabin;
      }
    },

    /**
     * Creates a new cabin and navigates to its detail page.
     *
     * Generate a new cabin using the provided last ID and a boolean
     * flag to determine if the cabin was created during the first run. After creating the cabin,
     * it updates the `selectedCabin` and navigates to the detail page of the newly created cabin.
     *
     * @param {number} lastId - The last used ID to generate a new cabin ID.
     * @param {boolean} bool - A flag that determines if the cabin is generated when the view was created.
     */
    onNewCabinButton(lastId, bool) {
      this.selectedCabin = Cabin.generateSampleCabins(lastId, bool);
    },
  },
  watch: {
    // 'selectedCabin.type'() {
    //   this.carouselKey++;
    // },
    // 'selectedCabin.location'() {
    //   this.carouselKey++;
    // }
  }
};
</script>

<style scoped>
.cabin-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}



.active {
  background-color: rgba(251, 112, 0, 0.35);
  border: 1px solid brown;
}
</style>