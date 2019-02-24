<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Rechazo vs Aceptación Feminismo según Tweets</h4>
    </div>
    <loading :conditional="chart.data == null"></loading>
    <div id="chartdiv"></div>
  </card>
</template>

<!-- Chart code -->
<script>
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import axios from 'axios';
import Loading from 'src/components/Loading'

export default {
  components: {
    Loading
  },

  data() {
    return {
      chart: {
        data: null
      }
    }
  },

  mounted() {
    am4core.useTheme(am4themes_animated);

    this.chart = am4core.create("chartdiv", am4charts.PieChart);

    axios.get(process.env.VUE_APP_API_ROOT+'tweets/apreciation')
      .then(response => {
          this.chart.data = [
            {
              "aceptación": "Aceptación",
              "cantidad": response.data.good,
              "color": "#ff0000"
            },
            {
              "aceptación": "Rechazo",
              "cantidad": response.data.bad,
              "color": "#ff0000"
            }
          ];
      });      

    this.chart.innerRadius = am4core.percent(50);

    // Add and configure Series
    var pieSeries = this.chart.series.push(new am4charts.PieSeries());
    pieSeries.dataFields.value = "cantidad";
    pieSeries.dataFields.category = "aceptación";
    pieSeries.slices.template.stroke = am4core.color("#fff");
    pieSeries.labels.template.disabled = true;
    pieSeries.ticks.template.disabled = true;
    this.chart.legend = new am4charts.Legend();
    pieSeries.slices.template.strokeWidth = 2;
    pieSeries.slices.template.strokeOpacity = 1;
    var colorSet = new am4core.ColorSet();
    colorSet.list = ["#3BAEEF", "#EF3B69"].map(function(color) {
      return new am4core.color(color);
    });
    pieSeries.colors = colorSet;

    // This creates initial animation
    pieSeries.hiddenState.properties.opacity = 1;
    pieSeries.hiddenState.properties.endAngle = -90;
    pieSeries.hiddenState.properties.startAngle = -90;
  }
}
</script>

<style>
  #chartdiv {
    width: 100%;
    height: 500px;
  }
</style>
