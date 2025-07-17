<script>
export default {
  name: "NavBarSb",
  inject: ['sessionService'],

  data() {
    return {
      email: "",
      hashedPassword: "",
      loginError: false,
    }
  },



  mounted() {
    const queryParams = this.$route.query;

    if (queryParams.signOff) {
      this.sessionService.signOut();
      // this.$router.replace(this.$route.matched[0].path);
      console.log("signed out");
    }
  },

  methods: {
    async signIn() {
      try {
        await this.sessionService.asyncSignIn(this.email, this.hashedPassword);
        console.log(this.sessionService.getCurrentUser());
        this.loginError = false;
      } catch (error) {
        console.error("Error during sign-in:", error);

        if (error.response && error.response.status === 406) {
          this.loginError = true;
          console.error("Login denied: Invalid credentials");
        } else {
          this.loginError = true;
        }
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <h2>Please provide your login credentials:</h2>
    <form>
      <table>
        <tbody>
          <tr>
            <td>User e-mail:</td>
            <td><input type="text" v-model="email" style="width: 90%"/></td>
          </tr>
          <tr>
            <td>Password:</td>
            <td><input type="password" v-model="hashedPassword" style="width: 90%"/></td>
          </tr>
        </tbody>
      </table>
      <button class="btn-new-cabin m-2" type="submit" style="float: right" @click="signIn">Sign in</button>
    </form>
    <br>
    <div v-if="this.loginError" style="color: red; font-family: 'Artifakt Element'; letter-spacing: 1px">Could not authenticate with provided credentials</div>
    <h3>Current token:</h3>
    <p style="max-width: 700px; word-wrap: break-word" v-if="sessionService.isAuthenticated()">{{ sessionService.getToken() }}</p>
  </div>
</template>

<style scoped>

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