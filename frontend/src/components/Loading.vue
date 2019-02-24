<template>
  <div v-if="conditional" class="py-5 text-center">
    <div class="d-block">
      <circle-spin></circle-spin>
    </div>
    <h2>Cargando...</h2>
  </div>
</template>

<script>
import CircleSpin from 'vue-loading-spinner/src/components/Circle'  

export default {
  name: 'loading',
  props: ['conditional'],
  components: {
    CircleSpin
  },  
};
</script>