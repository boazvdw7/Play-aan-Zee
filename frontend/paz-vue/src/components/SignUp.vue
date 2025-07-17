<script>
export default {
  name: "SignUp",
  inject: ["sessionService"],

  data() {
    return {
      email: "",
      callname: "",
      password: ""
    }
  },

  methods: {
    signUp() {
      this.sessionService.asyncSignUp(this.email, this.password, this.callname)
    }
  }
}

</script>

<template>
  <div class="container">
    <br>
    <h2>Create account</h2>
    <form>
      <table>
        <tbody>
          <tr>
            <td>E-mail: </td>
            <td><input v-model="email" type="text"/></td>
          </tr>
          <tr>
            <td>Call name: </td>
            <td><input v-model="callname" type="text"/></td>
          </tr>
          <tr>
            <td>Password: </td>
            <td><input v-model="password" type="password"/></td>
          </tr>
        </tbody>
      </table>
      <button class="btn-new-cabin m-2" @click="signUp">Sign Up</button>
    </form>
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