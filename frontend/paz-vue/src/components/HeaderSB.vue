<template>
  <header class="header-container">
    <div class="row">
      <img src="@/assets/site/zee-golf-logo.png" alt="Left Logo" class="logo logo-left">
      <img src="@/assets/site/speelgoed-logo.png" alt="Right Logo" class="logo logo-right">
    </div>

    <div class="header-text">
      <h1 class="header-title">Play & Stay aan Zee</h1>
      <div class="row">
        <h2 class="header-date col"> Today is <br> {{getday()}}, {{currentDateTime()}}</h2>
        <div class="justify-content-right">
          <h2 class="header-subtitle col mt-3 mr-5">
            Chill out and feel good!<br>
            Welcome
            <span v-if="sessionService.isAuthenticated() != null">{{ getUsernameFromEmail() }}</span>
            <span v-else>Visitor</span>!
          </h2>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: "HeaderSbComponent",
  inject: ['sessionService'],

  methods: {
    getUsernameFromEmail() {
      const regex = /^[^@]+/;
      return regex.exec(this.sessionService._currentUser.email).toString();
    },

    currentDateTime() {
      const date = new Date()
      return date.toLocaleDateString('nl-be', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    },

    getday() {
      const weekday = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];

      const d = new Date();
      return weekday[d.getDay()];
    }

  }
}
</script>


<style scoped>
.header-date {
  text-align: left;
  float: left;
  color: yellow;
  margin: 0;
}

.header-container {
  position: relative;
  height: 25vh;
  background-image: url('@/assets/site/banner.png');
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40px 0 20px 40px;
  box-sizing: border-box;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.logo {
  width: auto;
  height: 85%;
}

.logo-left {
  position: absolute;
  left: 20px;
  top: 20px;
}

.logo-right {
  position: absolute;
  right: 20px;
  top: 20px;
}

.header-text {
  position: relative;
  text-align: center;
  width: 96%;
  padding: 0 80px;
  box-sizing: border-box;
}

.header-title {
  font-family: Arial, serif;
  font-size: 3vw;
  color: #fff400;
  margin: 0;
}

.header-subtitle {
  font-family: Arial, serif;
  color: #fff400;
  text-align: right;
  margin: 0;
}

@media (max-width: 768px) {
  .logo {
    width: 80px;
    height: 80px;
  }

  .header-title {
    font-size: 6vw;
  }

  .header-subtitle {
    font-size: 3vw;
  }
}

@media (max-width: 480px) {
  .logo {
    width: 25vw;

  }

  .header-title {
    font-size: 4vw;
    width: 100%;
  }

  .header-subtitle {
    font-size: 3vw;
  }
}
</style>