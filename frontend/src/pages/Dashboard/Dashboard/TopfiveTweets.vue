<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Tweets más populares</h4>
    </div>
    <loading :conditional="tableData == null"></loading>
    <div class="row" v-if="tableData != null">
      <div class="col-sm-12" id="temas">
        <el-table :data="tableData" v-if="tableData != null">
          <el-table-column width="70"
                           type="index"
                           align="right">
          </el-table-column>
          <el-table-column width="150"
                           label="Foto Perfil"
                           align="center">
            <div slot-scope="{row}" class="img-container">
              <img :src="row.user.profileImageUrlHttps" class="img-fluid" alt="Responsive image">
            </div>
          </el-table-column>
          <el-table-column width="250" prop="account"
                           label="Cuenta"
                           align="left">
            <div slot-scope="{row}">
              <a :href="row.user.url" target="_blank">@{{ row.user.screenName }}</a>
            </div>
          </el-table-column>
          <el-table-column min-width="350" prop="tweet"
                           label="Tweet"
                           align="left|justify">
          </el-table-column>
           <el-table-column min-width="100" prop="retweets"
                           label="Retweets"
                           align="right">
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
    AsyncWorldMap
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
        tweets: [],
        tableData: null
      }
    },

    mounted(){
      this.getTableData();
    },
    methods: {
      getTableData: function(){
        axios.get(process.env.VUE_APP_API_ROOT+"tweets/top-tweets")
          .then(response => {
            this.tweets = response.data;

            this.tableData = [];
            for(var x=0;x<this.tweets.length;x++){
              this.tweets[x].user.url = "https://twitter.com/"+this.tweets[x].user.screenName;
              this.tableData.push({
                user: this.tweets[x].user,
                tweet: this.tweets[x].text,
                retweets: this.tweets[x].retweetCount,
              });
            }

            var i;
            for(i=0;i<this.tweets.length;i++){
              if(this.tableData[i].retweets>999){ 
                this.tableData[i].retweets = this.getFormattedNumber(this.tableData[i].retweets);
              }
            }
          });
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
