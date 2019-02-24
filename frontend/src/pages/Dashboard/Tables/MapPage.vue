<template>
  <div class="col-md-12">
      <async-world-map class="map map-big" v-if="mapData != null" :data="mapData"  ></async-world-map>
  </div>
</template>
<script>
  import AsyncWorldMap from 'src/components/WorldMap/AsyncWorldMap.vue';
  import axios from 'axios';
import { object } from '@amcharts/amcharts4/core';
  export default {
    components: {
      AsyncWorldMap
      
    },
    data() {
      return {
        countryMaps : [
          { code: "AR", nombre: "Argentina" },
          { code: "CL", nombre: "Chile" },
          { code: "CO", nombre: "Colombia" },
          { code: "CR", nombre: "CostaRica" },
          { code: "DO", nombre: "DominicanRepublic" },
          { code: "EC", nombre: "Ecuador" },
          { code: "SV", nombre: "ElSalvador" },
          { code: "HN", nombre: "Honduras" },
          { code: "MX", nombre: "Mexico" },
          { code: "ES", nombre: "España" },
          { code: "BO", nombre: "Bolivia" },
          { code: "CU", nombre: "Cuba" },
          { code: "GT", nombre: "Guatemala" },
          { code: "GQ", nombre: "EquatorialGuinea" },
          { code: "NI", nombre: "Nicaragua" },
          { code: "PA", nombre: "Panama" },
          { code: "PY", nombre: "Paraguay" },
          { code: "PE", nombre: "Perú" },
          { code: "UY", nombre: "Uruguay" },
          { code: "VE", nombre: "Venezuela" }
        ],
        arrayData:[],
        elements:[],
        map:{},
        mapData:null,
        prueba:{}
      }
    },
    mounted(){
      this.getData();
    
    },
 
    methods: {
      getData(){
        axios.get(process.env.VUE_APP_API_ROOT+'tweets/countries-afinity')
        .then(response => {
          // JSON responses are automatically parsed.
          
          this.elements = response.data;
          var datT = this.elements.map(function(item) {
            return{
                  pais: item.name,
                  cantidad: item.quantity              }
          });
          
         
        for(var i = 0;i<datT.length;i++){
           for(var x =0;x<20;x++){
              if(this.countryMaps[x].nombre === datT[i].pais ){
                  this.$set(this.map, this.countryMaps[x].code, datT[i].cantidad);
              }
            
           } 
        }       
        this.prueba = Object.assign({},this.map);
        this.coso();   
        })
      },
      
      coso(){
       this.mapData = this.prueba;
      }
        
    }
    
  }
</script>
<style>
  #worldMap {
    width: 100%;
  }
</style>