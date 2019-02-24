<template>
  <card>
    <div slot="header">
      <h4 class="card-title"> Cantidad Tweets semanales por día</h4>
      <select class="form-control" v-model="selectedWord" @change="loadTweetsPerDayGraph()">
        <option value="">Sin filtro</option>
        <option v-for="word in words" :key="word" :value="word">{{ word }}</option>
      </select>
    </div>
    <div class="col-sm-12">
      <loading :conditional="chartData == null"></loading>
      <header-chart :height="255" :data="chartData" :labels="chartLabels" v-if="chartData != null"></header-chart>
    </div>
  </card>
</template>

<script>
import HeaderChart from './HeaderChart'
import Loading from 'src/components/Loading'
import axios from 'axios';

export default {
  name: 'overview-header',
  components: {
    HeaderChart,
    Loading,
  },
  data(){
    return {
      selectedWord: '',
      words: [],
      chartData: null,
      chartLabels: null
    }
  },
  
  mounted (){
    this.loadWords();
  },

  methods: {
    loadWords() {
      axios.get(process.env.VUE_APP_API_ROOT+'keywords/')
        .then(response => {
          this.words = response.data
          this.loadTweetsPerDayGraph()
        });
    },

    loadTweetsPerDayGraph() {
      this.chartData = null;
      this.chartLabels = null;
      axios.get(process.env.VUE_APP_API_ROOT+'tweets/summary/'+this.selectedWord)
        .then(response => {
          this.chartData = Object.values(response.data)
          this.chartLabels = Object.keys(response.data)
        });
    }
  }
};
</script>
