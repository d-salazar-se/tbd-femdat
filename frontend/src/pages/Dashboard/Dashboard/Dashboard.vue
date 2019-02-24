<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Personas Influyentes</h4>
    </div>
    <loading :conditional="tableData == null"></loading>
    <div class="row" v-if="tableData != null">
      <div class="col-sm-12" id="personasInfluyentes">
        <el-table :data="tableData">
          <el-table-column min-width="50" type="index"></el-table-column>
          <el-table-column width="200"
                           label="Foto de Perfil"
                           align="center">

            <div slot-scope="{row}"  class="img-container">
              <img  :src="row.image" class="img-fluid" alt="Responsive image">
            </div>
          </el-table-column>
          <el-table-column min-width="120" prop="name"
                           label="Nombre"
                           align="left">
          </el-table-column>
          <el-table-column width="300" prop="visualizations"
                           label="Cantidad de Seguidores"
                           align="right"
                           sortable>
          </el-table-column>
          <el-table-column width="265" prop="retweets"
                           label="Cantidad de Amigos" align="right"
                           sortable>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </card>
</template>

<script>
  import {
    StatsCard,
    Card,
    Table as NTable,
    Checkbox,
    AnimatedNumber,
    Progress as NProgress,
    LineChart,
    AsyncWorldMap,
  } from 'src/components'

  import Loading from 'src/components/Loading'
  import StackedColumn from 'src/pages/Dashboard/Dashboard/StackedColumn.vue'
  import {Table, TableColumn} from 'element-ui'
  import axios from 'axios';

  export default {
    components: {
      Checkbox,
      Card,
      NTable,
      StatsCard,
      AnimatedNumber,
      LineChart,
      NProgress,
      AsyncWorldMap,
      StackedColumn,
      [Table.name]: Table,
      [TableColumn.name]: TableColumn,
      Loading,
    },
    data() {
      return {
        figures: [],
        progress: 0,
        tableData: null
      }
    },

    mounted(){
      this.getTableData();
    },
    methods: {
      getTableData: function(){
        axios.get(process.env.VUE_APP_API_ROOT+'tweets/top-figures')
          .then(response => {
            this.figures = response.data;
            var datT = this.figures.map(function(item) {
              return {
                    id: item.id,
                    name: item.name,
                    visualizations: item.followersCount,
                    retweets: item.friendsCount,
                    show: true,
                    image:item.profileImageUrlHttps
                }
            });

            this.tableData = [];
            var i;
            for(i=0;i<datT.length;i++){
              if (datT[i].visualizations>999){  
                datT[i].visualizations = this.getFormattedNumber(datT[i].visualizations);
              }
             
              if (datT[i].retweets>999){  
                datT[i].retweets = this.getFormattedNumber(datT[i].retweets);
              }
              this.tableData.push(datT[i]);
             }
            }
          );
      },
      formatPrice(value) {
        let val = (value/1).toFixed(2).replace('.', ',')
        return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")
      },
      getFormattedNumber: function(number){
        if(number > 1000 && number < 1000000){
          number = number/1000;
          return number.toLocaleString('en', {maximumSignificantDigits: 3, minimumIntegerDigits: 1}) + ' K';
        }
        else{
          number = number/1000000;
          return number.toLocaleString('en', {maximumSignificantDigits: 3, minimumIntegerDigits: 1}) + ' M'; 
        }
      }
    }
  };
</script>

<style scoped>
  el-tooltip {
    text-align: center;
  }
</style>
