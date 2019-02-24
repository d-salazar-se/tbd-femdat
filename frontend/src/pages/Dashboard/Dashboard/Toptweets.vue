<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Palabras o Temas más retwitteados</h4>
    </div>
    <loading :conditional="tableData == null"></loading>
    <div class="row" v-if="tableData != null">
      <div class="col-sm-12" id="temas">
        <el-table :data="tableData">
          <el-table-column min-width="50" type="index"></el-table-column>
          <el-table-column min-width="150" prop="name" label="Tópico" align="left"></el-table-column>
          <el-table-column min-width="150" prop="quantity" label="Cantidad de Tweets/Retweets" align="right"></el-table-column>
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
        words: [],
        progress: 0,
        tableData: null
      }
    },

    mounted(){
      this.getTableData();
    },
    methods: {
      getTableData: function(){
        this.tableData = null;
        axios.get(process.env.VUE_APP_API_ROOT+'tweets/top-topics')
          .then(response => {
            this.tableData = response.data;

            for(var i = 0; i < this.tableData.length; i++) {
              if (this.tableData[i].quantity > 999) {
                this.tableData[i].quantity = this.getFormattedNumber(this.tableData[i].quantity);
              }
           }
        });
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
