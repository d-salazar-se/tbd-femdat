<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Relación #hastags y @twitteros </h4>
    </div>
    <loading :conditional="loading"></loading>
    <d3-network ref='net' :net-nodes="nodes" :net-links="links" :options="options" />
  </card>
</template>

<script>
import Loading from 'src/components/Loading';
import D3Network from 'vue-d3-network';
import axios from 'axios';

export default {
  components: {
    Loading,
    D3Network
  },
  data(){
    return {
      loading: true,
      words: [],
      nodes: [],
      links: [],
      options: {
        canvas: false,
        force: 3000,
        nodeSize: 20,
        linkWidth: 1,
        fontSize: 16,
        strLinks: true,
        nodeLabels: true,
        size: {
          w: 1000, h: 500,
        }
      }
    }
  },
  
  mounted (){
    this.loadData();
  },

  methods: {
    loadData() {
      axios.get(process.env.VUE_APP_API_ROOT+'graph/')
        .then(response => {
          this.nodes = response.data.nodes
          this.links = response.data.links
        })
        .catch(() => {
          
        })
        .then(() => {
          this.loading = false;
        });
    },
  }
};
</script>

<style src="vue-d3-network/dist/vue-d3-network.css"></style>
