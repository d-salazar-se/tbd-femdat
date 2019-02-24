<template>
  <div class="row" v-if="tableData != null">
    <div class="col-12">
      <card card-body-classes="table-full-width" no-footer-line>
        <h4 slot="header" class="card-title">Administración de KeyWords</h4>
        <div>
          <div class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap">
            <el-select class="select-primary mb-3" style="width: 200px" v-model="pagination.perPage" placeholder="Per page">
              <el-option class="select-default" v-for="item in pagination.perPageOptions" :key="item" :label="item" :value="item"></el-option>
            </el-select>
            <n-button @click.native="createWord()" type="info" round>
              Agregar Keyword
              <i class="now-ui-icons ui-1_simple-add"></i>
            </n-button>
            <fg-input>
              <el-input type="search" class="mb-3" clearable prefix-icon="el-icon-search" style="width: 200px" placeholder="Buscar..." v-model="searchQuery" aria-controls="datatables"></el-input>
            </fg-input>
          </div>
          <el-table stripe style="width: 100%;" :data="queriedData">
            <el-table-column v-for="column in tableColumns" :key="column.label" :min-width="column.minWidth" :prop="column.prop" :label="column.label"></el-table-column>
            <el-table-column :min-width="135" fixed="right" label="Acciones">
              <div slot-scope="props" class="table-actions">
                <n-button @click.native="handleDelete(props.$index, props.row)" class="remove" type="danger" size="sm" round icon>
                  <i class="fa fa-times"></i>
                </n-button>
              </div>
            </el-table-column>
          </el-table>
        </div>
        <div slot="footer" class="col-12 d-flex justify-content-center justify-content-sm-between flex-wrap">
          <div class="">
            <p class="card-category">Showing {{from + 1}} to {{to}} of {{total}} entries</p>
          </div>
          <n-pagination class="pagination-no-border" v-model="pagination.currentPage" :per-page="pagination.perPage" :total="total">
          </n-pagination>
        </div>
      </card>
    </div>
  </div>
</template>
<script>
  import {Table, TableColumn, Select, Option} from 'element-ui'
  import {Pagination as NPagination} from 'src/components'
  import Fuse from 'fuse.js'
  import swal from 'sweetalert2'
  import axios from 'axios';
  import Loading from 'src/components/Loading'
  export default {
    components: {
      NPagination,
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
          perPage: 50,
          currentPage: 1,
          perPageOptions: [5, 10, 25, 50],
          total: 0
        },
        searchQuery: '',
        propsToSearch: ['name'],
        tableColumns: [
          {
            prop: 'name',
            label: 'KeyWords',
            minWidth: 200
          }
        ],
        searchedData: [],
        fuseSearch: null,
        tableData:null,
        words:[],
        message:null,
        keyword:null
      }
    },
    methods: {
      createWord () {
        swal({
            title: 'Ingrese nueva keyword',
            showCancelButton: true,
            confirmButtonClass: 'btn btn-success btn-fill',
            cancelButtonClass: 'btn btn-danger btn-fill',
            confirmButtonText: 'Agregar',  
            buttonsStyling: false,
            input: 'text'
          }).then((result) => {
             if (result.value) {
              this.addKey(result.value);
              this.tableData.push({
                id: this.tableData.length,
                name: result.value
              });
              swal({
                type: 'success',
                html:  `La keyword "${result.value}" ha sido agregada con éxito`,
                confirmButtonClass: 'btn btn-success btn-fill',
                buttonsStyling: false
              });
            } 
          }).catch(swal.noop)
      },
      handleDelete (index, row) {
        swal({
          title: `¿Desea eliminar la keyword  "${row.name}"?`,
          text: `No podrá revertirlo`,
          type: 'warning',
          showCancelButton: true,
          confirmButtonClass: 'btn btn-success btn-fill',
          cancelButtonClass: 'btn btn-danger btn-fill',
          confirmButtonText: 'Eliminar',
          buttonsStyling: false
        }).then((result) => {
          if(result.value){
            this.deleteRow(row);
            this.deleteWord(row.name)
            swal({
              title: 'Ha sido eliminada con éxito',
              text: `Eliminaste la keyword "${row.name}"`,
              type: 'success',
              confirmButtonClass: 'btn btn-success btn-fill',
              buttonsStyling: false
            })
          }
        });
      },
      deleteRow(row){
        let indexToDelete = this.tableData.findIndex((tableRow) => tableRow.id === row.id);
        if (indexToDelete >= 0) {
          this.tableData.splice(indexToDelete, 1)
        }
      },

      deleteWord(word){
        axios.delete(process.env.VUE_APP_API_ROOT+"keywords/"+word)
          .then(response => {
          }
        );
      },

      addKey(word){
        axios.post(process.env.VUE_APP_API_ROOT+"keywords/"+word)
        .then(response => {
                  
        });
      }
    },
    mounted () {
      axios.get(process.env.VUE_APP_API_ROOT+"keywords")
          .then(response => {
            this.words = response.data;
            this.tableData = [];
            for(var x=0;x<this.words.length;x++){
               this.tableData.push({
                "id":x+1,
                "name": this.words[x],
              });
            }
            this.fuseSearch = new Fuse(this.tableData, {keys: ['name'], threshold: 0.3})

            }
          );
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
