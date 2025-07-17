<template>
  <div class="cabin-detail" v-bind="$attrs">
    <div v-if="selectedCabin">
      <div class="row">
        <div class="col">
          <img width="500" height="250" :src="require(`@/assets/cabins/${this.copyCabin.image}`)" alt="Cabin Image" />
        </div>
        <div class="col">
          <div>
            <span class="text-bold">Cabin type: </span><span>{{ copyCabin.type }}</span>
          </div>
          <div>
            <span class="text-bold">Location: </span><span>{{ copyCabin.location.name }}</span>
          </div>
          <div>
            <span class="text-bold">Description: </span><span>{{ copyCabin.description }}</span>
          </div>
          <div>
            <span class="text-bold">Price: </span><span>&#8364;{{ copyCabin.pricePerWeek }}</span><span class="text-bold"> per week</span>
          </div>
          <div>
            <span class="text-bold">Max available: </span><span>{{ copyCabin.numAvailable }}</span>
          </div>
        </div>
      </div>
        <h3 style="padding: 15px 0 15px 0">2. Check availability: </h3>
        <div>and click or enter the dates of your requested rental period</div>

      <div class="availability">
        <div class="row">
          <div class="col" style="padding: 0;">
            <img @click="previousDates" width="100" height="100" src="@/assets/site/arrow-left.png" alt="Previous">
          </div>
          <div class="col" style="padding: 0;">
            <table>
              <tbody>
              <tr>
                <td style="max-width: 5px">Arrival Dates: </td>
                <td style="max-width: 70px" v-for="date in dates" :key="date">{{ date }}</td>
              </tr>
              <tr>
                <td>Availabilities: </td>
                <td v-for="(availability, index) in availabilities" :key="index">{{ availability }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="col" style="padding: 0;">
            <img @click="nextDates" width="100" height="100" src="@/assets/site/arrow-right.png" alt="Next" />
          </div>
        </div>
        <div class="row">
          <h3>3. Submit your request for reservation:</h3>
          <table>
            <tbody>
              <tr>
                <td>Start date:</td>
                <td><select></select></td>
              </tr>
              <tr>
                <td>End date:</td>
                <td><select></select></td>
              </tr>
              <tr>
                <td>Rental days:</td>
                <td>0</td>
              </tr>
              <tr>
                <td>Rental cost:</td>
                <td>0</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import Cabin from "@/models/Cabin";
import {reactive} from "vue";

/**
 * Represents the cabin detail component where users can view and edit cabin information.
 */
export default {
  name: "RentalsDetail45",
  inject: ['cachedCabinService', 'locationsService'],

  /**
   * Props passed to the component.
   * @property {Cabin} selectedCabin - The cabin object selected for editing.
   */
  props: {
    selectedCabin: {
      type: Object,
      default: null
    }
  },

  emits: ['cancelEditCabin', 'updateCabin', 'deletedCabin', 'refreshCabin'],

  created() {
    this.availabilitiesStartDate = this.getLastSaturday(new Date());
  },

  data() {
    return {
      CabinType: Cabin.Type,
      images: Cabin.images,
      locations: [],
      rentals: [],
      originalCabin: null,
      copyCabin: {},
      availabilitiesStartDate: this.getLastSaturday(new Date()),
    }
  },

  computed: {
    dates() {
      const options = {
        weekday: "short",
        day: "2-digit",
        month: "short",
        year: "numeric",
      };

      const dates = [];
      const startDate = new Date(this.availabilitiesStartDate);

      for (let i = 0; i < 7; i++) {
        const date = new Date(startDate);
        date.setDate(startDate.getDate() + i * 7);

        const formattedDate = date
            .toLocaleDateString("en-US", options)
            .replace(/(\w+), (\w+) (\d+), (\d+)/, "$1, $3 $2 $4");
        dates.push(formattedDate);
      }
      return dates;
    },

    availabilities() {
      return this.dates.map(() => {
        if (!this.copyCabin || !this.copyCabin.rentals) {
          return this.copyCabin.numAvailable || 0;
        }

        const activeRentals = this.copyCabin.rentals.filter(rental =>
            ['PAID', 'FULFILLED', 'REQUESTED'].includes(rental.status)
        ).length;

        return Math.max(0, this.copyCabin.numAvailable - activeRentals);
      });
    },

    /**
     * Computed property for updating the location based on selection.
     * @returns {number} The ID of the selected location.
     */
    locationUpdater: {
      get() {
        return this.copyCabin.location.id;
      },
      set(id) {
        this.copyCabin.location = this.locations.find(loc => loc.id === id);
      }
    },
  },


  methods: {
    nextDates() {
      this.availabilitiesStartDate = new Date(
          this.availabilitiesStartDate.setDate(this.availabilitiesStartDate.getDate() + 7)
      );
      this.fetchCabinDetails();
    },
    previousDates() {
      this.availabilitiesStartDate = new Date(
          this.availabilitiesStartDate.setDate(this.availabilitiesStartDate.getDate() - 7)
      );
      this.fetchCabinDetails();
    },
    getLastSaturday(date) {
      const day = date.getDay();
      const diff = (day === 6 ? 0 : day + 1);
      return new Date(date.setDate(date.getDate() - diff));
    },
    async fetchCabinDetails() {
      if (this.selectedCabin) {
        const startDate = new Date(this.availabilitiesStartDate);
        try {
          this.copyCabin = await this.getSelectedCabin(this.selectedCabin.id, startDate);
          console.log(this.copyCabin);
        } catch (error) {
          console.error('Error fetching cabin details:', error);
        }
      }
    },
    async getSelectedCabin(cabinId, startDate) {
      try {
        const response = await this.cachedCabinService.asyncFindById(cabinId, {
          availabilitiesFrom: startDate.toISOString().substr(0, 10)
        });

        console.log(response)

        if (!response.availabilities) {
          console.warn("Backend did not provide availabilities.");
        }

        return response;
      } catch (error) {
        console.error('Error fetching cabin details:', error);
        throw error;
      }
    }

  },

  watch: {
    /**
     * Watcher for the selectedCabin prop. Updates originalCabin and copyCabin when changed.
     * @param {Cabin} newCabin - The new cabin object.
     */
    selectedCabin: {
      immediate: true,
      handler(newCabin) {
        if (newCabin) {
          this.originalCabin = Cabin.copyConstructor(newCabin);
          this.copyCabin = reactive(Cabin.copyConstructor(newCabin));
        }
      }
    }
  }
};
</script>

<style scoped>
:disabled {
  opacity: 0.5;
}

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
