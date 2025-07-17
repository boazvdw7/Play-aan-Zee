<template>
  <div class="cabin-detail" v-bind="$attrs">
    <div v-if="selectedCabin">
      <div class="thead">
        <h3>Cabin Details (ID: {{ copyCabin.id }})</h3>
      </div>
      <form>
        <table>
          <tbody>
          <tr>
            <td><label for="type">Type:</label></td>
            <td>
              <select id="type" v-model="copyCabin.type">
                <option v-for="type in CabinType" :key="type" :value="type">{{ type }}</option>
              </select>
            </td>
          </tr>
          <tr>
            <td><label for="location">Location:</label></td>
            <td>
              <select id="location" v-model="copyCabin.location">
                <option v-for="loc of locations" :key="loc.id" :value="loc">{{ loc.name }}</option>
              </select>
              {{ copyCabin.location.country }}
            </td>
          </tr>
          <tr>
            <td><label for="description">Description:</label></td>
            <td><input type="text" id="description" v-model="copyCabin.description" /></td>
          </tr>
          <tr>
            <td><label for="image">Image:</label></td>
            <td>
              <select v-model="copyCabin.image">
                <option v-for="img in images" :key="img" :value="img">{{ img }}</option>
              </select>
            </td>
          </tr>
          <tr>
            <td><label for="price">Price per week:</label></td>
            <td><input type="text" id="price" v-model="copyCabin.pricePerWeek" /></td>
          </tr>
          <tr>
            <td><label for="availability">Total availability:</label></td>
            <td><input type="text" id="availability" v-model="copyCabin.numAvailable" /></td>
          </tr>
          </tbody>
        </table>
      </form>
      <button @click="onSaveCabinButton();" class="btn-new-cabin" :disabled="!isFormValid">Save</button>
      <button @click="onCancelButton();" class="btn-new-cabin">Cancel</button>
      <button @click="onResetButton();" class="btn-new-cabin" :disabled="!hasChanged">Reset</button>
      <button @click="onClearButton();" class="btn-new-cabin">Clear</button>
      <button @click="onDeleteButton(copyCabin);" class="btn-new-cabin" :disabled="hasChanged">Delete</button>
    </div>
  </div>
  <br>
</template>

<script>
import Location from "@/models/Location";
import Cabin from "@/models/Cabin";

/**
 * Represents the cabin detail component where users can view and edit cabin information.
 */
export default {
  name: "CabinsDetail34",
  inject: ['cabinsService', "locationsService"],

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

  data() {
    return {
      CabinType: Cabin.Type,
      images: Cabin.images,
      locations: Location.samples,
      originalCabin: null,
      copyCabin: {}
    }
  },

  computed: {
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

    /**
     * Computed property to check if the cabin details have changed.
     * @returns {boolean} True if the copyCabin differs from the originalCabin.
     */
    hasChanged() {
      return this.originalCabin && this.copyCabin && !this.copyCabin.equals(this.originalCabin);
    },

    /**
     * Computed property to check if the form is valid.
     * @returns {boolean} True if all required fields are filled and changes are made.
     */
    isFormValid() {
      const requiredFieldsValid =
          this.copyCabin.type &&
          this.copyCabin.location &&
          this.copyCabin.image &&
          this.copyCabin.pricePerWeek &&
          this.copyCabin.numAvailable;

      return requiredFieldsValid && this.hasChanged;
    }
  },

  async mounted() {
    this.locations = await this.locationsService.asyncFindAll();
    window.addEventListener('beforeunload', this.beforeRouteUpdate);
  },

  beforeUnmount() {
    window.removeEventListener('beforeunload', this.beforeRouteLeave);
  },

  /**
   * Navigation guard that prompts the user if there are unsaved changes when leaving the route.
   * @param {Object} to - The target route object.
   * @param {Object} from - The current route object.
   * @param {Function} next - Callback to resolve the navigation.
   */
  beforeRouteLeave(to, from, next) {
    if (this.hasChanged) {
      const confirmLeave = window.confirm("You have unsaved changes. Are you sure you want to continue?");
      if (confirmLeave) {
        next();
      } else {
        next(false);
      }
    } else {
      next();
    }
  },

  /**
   * Navigation guard that prompts the user if there are unsaved changes when updating the route.
   * @param {Object} to - The target route object.
   * @param {Object} from - The current route object.
   * @param {Function} next - Callback to resolve the navigation.
   */
  beforeRouteUpdate(to, from, next) {
    if (this.hasChanged) {
      const confirmLeave = window.confirm("You have unsaved changes. Are you sure you want to switch cabins?");
      if (confirmLeave) {
        next();
      } else {
        next(false);
      }
    } else {
      next();
    }
  },

  methods: {
    /**
     * Saves the current cabin details and emits an update event.
     */
    onSaveCabinButton() {
      this.$router.currentRoute.value.meta.hasUnsavedChanges = false;
      this.$emit('updateCabin', this.copyCabin);

      // This makes sure the objects are reset, if not it will cause hasChanged to bug
      this.originalCabin = Cabin.copyConstructor(this.copyCabin);
      this.copyCabin = Cabin.copyConstructor(this.originalCabin);

      this.$emit('refreshCabins');
    },

    /**
     * Cancels the editing of the cabin and reverts to the original details.
     */
    onCancelButton() {
      this.$emit('cancelEditCabin');
    },

    /**
     * Resets the cabin details to the original state.
     */
    onResetButton() {
      this.copyCabin = Cabin.copyConstructor(this.originalCabin);
    },

    /**
     * Clears the form fields and resets the copyCabin to a new default cabin object.
     */
    onClearButton() {
      this.copyCabin = new Cabin(
          this.copyCabin.id,
          "BeachGear",
          null,
          null,
          {id: null, country: null},
          null,
      );
    },

    /**
     * Emits a delete event for the selected cabin.
     * @param {Cabin} copyCabin - The cabin to be deleted.
     */
    onDeleteButton(copyCabin) {
      const confirmDelete = window.confirm("Are you sure you want to delete this cabin?");
      if (confirmDelete) {
        this.$emit('deletedCabin', copyCabin);
      }

      this.$emit("refreshCabin");
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
          this.copyCabin = Cabin.copyConstructor(newCabin);
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
