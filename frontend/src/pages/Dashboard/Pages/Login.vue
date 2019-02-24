<template>
  <div class="col-md-4 ml-auto mr-auto">
    <form @submit.prevent>
      <br/>
      <br/>
      <br/>
      <br/>
      <br/>
      <card class="card card-nav-tabs text-center">         
        <div slot="header">
          <div class="logo-container">
            <img src="@/assets/img/Icon.svg" alt=""  style="width:100px;height:100px;" align="center">
            <h4 class="card-title">FemDat</h4>  
          </div>
        </div>

        <div class="form-group">
          <label for="exampleInputEmail1">Username</label>
          <input type="username" class="form-control" id="exampleInputEmail1" aria-describedby="usernameHelp"  placeholder="Ingrese Nombre de usuario" v-model="user.username">
        </div>

        <div class="form-group">
          <label for="exampleInputPassword1">Password</label>
          <input type="password" class="form-control" id="exampleInputPassword1"  placeholder="Ingrese Password" v-model="user.password">
        </div>

        <button type="button"  @click="submit()" class="btn btn-primary">Entrar</button>

        <div class="alert alert-primary" v-if="this.show ==='true'">
          <span><b> ! </b>Recuerde ingresar username y/o contraseña</span>
        </div>
      </card>
    </form>
  </div>
</template>

<script>
  import axios from 'axios';
  import swal from 'sweetalert2'
  export default {
    data() {
      return {
        user: {
          username: '',
          password: ''
        },
        show:"false"
      }
    },
    methods:{
      submit(){
        if (this.user.username === "" || this.user.password === ""){
          this.show = "true";
          return this.show
        }
        axios.post(process.env.VUE_APP_API_ROOT+"login", this.user)
          .then(response => {
              this.show = "false";
              if (response.data == true) {
                window.location.href = "#/key-words";
              } else {
                swal({
                  title: `Error`,
                  text: `Las credenciales no coinciden con nuestros registros!`,
                  type: 'error',
                  confirmButtonText: 'Ok'
                })
              }
            });       
      }
    }
  }
</script>
<style>
  .navbar-nav .nav-item p {
    line-height: inherit;
    margin-left: 5px;
  }
</style>
