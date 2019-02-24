<template>
  <card card-body-classes="table-full-width" no-footer-line>
    <h4 slot="header" class="card-title">Tweets por País</h4>
    
    <loading :conditional="tableData == null"></loading>
    <div v-if="tableData != null">
      <div class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap">
        <fg-input>
          <el-input type="search"
                    class="mb-3"
                    clearable
                    prefix-icon="el-icon-search"
                    style="width: 200px"
                    placeholder="Search"
                    v-model="searchQuery"
                    aria-controls="datatables">
          </el-input>
        </fg-input>
      </div>
      <div class="row">
        <div class="col-md-6">
          <el-table stripe style="width: 100%;" :data="queriedData">
            <el-table-column v-for="column in tableColumns"
                             :key="column.label"
                             :min-width="column.minWidth"
                             :prop="column.prop"
                             :label="column.label"
                             :align="column.align"></el-table-column>
          </el-table>
        </div>
        <div class="col-md-6 ml-auto mr-auto">
          <div class="jvectormap-container" style="background-color: transparent;">
            <map-page></map-page>
          </div>
        </div> 
      </div>
      <div slot="footer" class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap">
        <div class="">
          <p class="card-category">Showing {{from + 1}} to {{to}} of {{total}} entries</p>
        </div>
        <n-pagination class="pagination-no-border"
                      v-model="pagination.currentPage"
                      :per-page="pagination.perPage"
                      :total="total">
        </n-pagination>
      </div>
    </div>
  </card>
</template>

<script>
  import {Table, TableColumn, Select, Option} from 'element-ui'
  import {Pagination as NPagination,   AsyncWorldMap} from 'src/components'
  import Fuse from 'fuse.js'
  import MapPage from 'src/pages/Dashboard/Tables/MapPage.vue'
  import Loading from 'src/components/Loading';
  import axios from 'axios';
  
  export default {
    components: {
      NPagination,
      MapPage,
       AsyncWorldMap,
      [Select.name]: Select,
      [Option.name]: Option,
      [Table.name]: Table,
      [TableColumn.name]: TableColumn,
      Loading
    },
    computed: {
      queriedData () {
        let result = this.tableData;
        if(this.searchedData.length > 0){
          result = this.searchedData;
        }
        return result.slice(this.from, this.to)
      },
      to () {
        let highBound = this.from + this.pagination.perPage
        if (this.total < highBound) {
          highBound = this.total
        }
        return highBound
      },
      from () {
        return this.pagination.perPage * (this.pagination.currentPage - 1)
      },
      total () {
        return this.searchedData.length > 0 ? this.searchedData.length : this.tableData.length;
      }
    },
    data () {
      return {
        pagination: {
          perPage: 15,
          currentPage: 1,
          perPageOptions: [5, 10, 25, 50],
          total: 0
        },
        searchQuery: '',
        propsToSearch: ['name'],
        tableColumns: [
          {
            prop: 'name',
            label: 'País',
            minWidth: 200,
            align: 'left'
          },
          {
            prop: 'quantity',
            label: 'Cantidad Tweets',
            minWidth: 250,
            align: 'right'
          }
        ],
        tableData: null,
        searchedData: [],
        fuseSearch: null,
        array:[]
      }
      
    },
    methods: {
       getData(){
         axios.get(process.env.VUE_APP_API_ROOT+'tweets/countries-activity')
          .then(response => {
            var i;
            this.array = response.data;
            this.tableData = [];
            for(i = 0; i < this.array.length; i++){
              if (this.array[i].quantity > 999) {
                this.array[i].quantity = this.getFormattedNumber(this.array[i].quantity);
              }
              this.tableData.push({
                name: this.array[i].name,
                quantity: this.array[i].quantity
              });
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
      
      } ,
    mounted () {
      this.getData();
      this.fuseSearch = new Fuse(this.tableData, {keys: ['name'], threshold: 0.3})
    },
    watch: {
      searchQuery(value){
        let result = this.tableData;
        if (value !== '') {
          result = this.fuseSearch.search(this.searchQuery)
        }
        this.searchedData = result;
      }
    }
  }
</script>
<style>
</style>
