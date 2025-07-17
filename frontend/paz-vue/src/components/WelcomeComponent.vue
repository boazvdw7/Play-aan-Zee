<template>
  <div class="row">
    <div class="col-1">
      <div class="text">
        <h3>Pick a date:</h3>
        <input type="date">
        <h3>Pick a location:</h3>
        <ul>
          <li><a href="#">Egmond aan Zee</a></li>
          <li><a href="#">Katwijk</a></li>
          <li><a href="#">Renesse</a></li>
          <li><a href="#">Zoutelande</a></li>
          <li><a href="#">De Panne</a></li>
        </ul>
      </div>
    </div>

    <div class="col-4">
      <div class="text">
        <h3>Photo gallery:</h3>
        <div class="image-row">
          <img src="../assets/cabins/cabin-1.jpg" alt="Image 1" class="huisje">
          <img src="../assets/cabins/cabin-8.jpg" alt="Image 2" class="huisje">
          <img src="../assets/cabins/cabin-3.jpg" alt="Image 3" class="huisje">
          <img src="../assets/cabins/cabin-4.jpg" alt="Image 4" class="huisje">
          <img src="../assets/cabins/cabin-5.jpg" alt="Image 5" class="huisje">
          <img src="../assets/cabins/cabin-6.jpg" alt="Image 5" class="huisje">
          <img src="../assets/cabins/cabin-7.jpg" alt="Image 5" class="huisje">
        </div>
      </div>
    </div>

    <div class="col-2">
      <div class="text">
        <h3>Beach activities:</h3>

        <div class="description">
          Book now for the next month of June at Knokke-Heist, and get a free pass-par-toutes for the beach-volleyball tournament of the open Belgium championships!
        </div>

        <div class="il-text mt-4">
          <div class="label">Period:</div>
          <div class="content">6-19 June 2024</div>
        </div>

        <div class="il-text">
          <div class="label">Location:</div>
          <div class="content">Knokke beach</div>
        </div>

        <div class="il-text mt-4">
          <div class="label">Size:</div>
          <div class="content">Max 6 guests per cabin</div>
        </div>

        <div class="il-text">
          <div class="label">Rent:</div>
          <div class="content">1300 p/w</div>
        </div>

        <div class="text-bold">Only 4 cabins remaining!</div>
      </div>
    </div>
  </div>
  <br>
</template>

<script>

</script>

<style scoped>
.image-row {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding: 0 20px;
}

.huisje {
  object-fit: cover;
  max-height: 150px;
}

@media (max-width: 1186px) {
  .row {
    flex-direction: row;
  }

  .col-4 {
    max-width: none;
  }

  .text-bold {
    margin-bottom: 20px;
  }
}
</style>