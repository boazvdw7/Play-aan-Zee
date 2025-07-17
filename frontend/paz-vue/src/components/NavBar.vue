<template>
  <div class="navbar">
    <router-link to="/home">Home</router-link>
    <div class="dropdown">
      <button class="dropbtn">Cabins</button>
      <div class="dropdown-content">
        <routerLink to="/overview31">All Cabins Overview</routerLink>
        <router-link to="/overview32">Cabins edit(Components)</router-link>
        <router-link to="/overview33">Cabins edit(Router)</router-link>
        <router-link to="/overview34">Cabins edit(Guarded)</router-link>
        <router-link to="/overview37">Cabins edit(REST &#128585;)</router-link>
        <router-link to="/overview37c">Cabins edit(Cached &#128585;)</router-link>
        <router-link to="/overview45">Cabins edit(Rental Info)</router-link>
      </div>
    </div>

    <div class="dropdown ml-1">
      <button class="dropbtn">Rentals</button>
      <div class="dropdown-content">
        <router-link to="/request-rental">Request Rental</router-link>
      </div>
    </div>
    <router-link to="/my-account">My Account</router-link>
    <div class="login-register">
      <router-link to="/sign-up">Sign up</router-link>
      <router-link to="/sign-in">Login</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "NavBar"
}
</script>

<style scoped>
.navbar {
  overflow: hidden;
  background-color: #5b71f3;
}

.navbar a {
  float: left;
  font-size: 16px;
  color: #a94427;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  background-color: wheat;
  margin: 0 5px;
}

.dropdown .dropdown-content a {
  margin: 0;
}

.navbar a:active {
  background: #d78a16 !important;
}

.dropdown {
  float: left;
  overflow: hidden;
  background-color: wheat;
}

.dropdown .dropbtn {
  font-size: 16px;
  border: none;
  outline: none;
  padding: 14px 16px;
  background-color: inherit;
  font-family: inherit;
  color: #a94427;
}

.navbar a:hover, .dropdown:hover .dropbtn , .dropdown-content a:hover  {
  background-color: #cf8b0d;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content a {
  float: none;
  color: #a94427;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  background-color: wheat;
  text-align: left;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.login-register {
  float:right;
}

@media (max-width: 1250px) {
  .navbar a {
    margin: 0;
  }
}

@media (max-width: 500px) {
  .navbar, .login-register {
    display: flex;
    flex-direction: column;
  }

  .dropbtn {
    width: 100%;
  }
}
</style>