<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Porcentaje Aprobación Diaria Feminismo</h4>
    </div>
    <!-- <loading :conditional="tableData == null"></loading> -->
    <div id="chartdiv"></div>
  </card>
</template>

<!-- Chart code -->
<script>
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import Loading from 'src/components/Loading'
import axios from 'axios';

export default {
  components: {
    Loading,
  },

  data() {
    return {
      elements: [],
      words: [],
      tableData: null
    }
  },

  mounted() {
    am4core.useTheme(am4themes_animated);

    let chart = am4core.create("chartdiv", am4charts.XYChart);
    chart.numberFormatter.numberFormat = "#0'%'";
    chart.data =[];
    axios.get(process.env.VUE_APP_API_ROOT+"tweets/last-week-sa")
      .then(response => {
        this.words = Object.values(response.data);
        this.elements = Object.keys(response.data);
        this.tableData = [];
        
        for(var x=0; x<7; x++){
          this.tableData.push({
            "fecha": this.elements[x],
            "aprob": this.words[x],
            "lineColor":"#BD93EA"
           });  
        }
     
        chart.data = this.tableData;
      })
      .catch(() => {
        this.tableData = []
      });

    let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.ticks.template.disabled = true;
    categoryAxis.renderer.line.opacity = 0;
    categoryAxis.renderer.grid.template.disabled = true;
    categoryAxis.renderer.minGridDistance = 40;
    categoryAxis.dataFields.category = "fecha";
    categoryAxis.startLocation = 0.4;
    categoryAxis.endLocation = 0.6;

    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.tooltip.disabled = true;
    valueAxis.renderer.line.opacity = 0;
    valueAxis.renderer.ticks.template.disabled = true;
    valueAxis.min = 0;
    valueAxis.max = 100;
    valueAxis.strictMinMax = 100;

    let lineSeries = chart.series.push(new am4charts.LineSeries());
    lineSeries.dataFields.categoryX = "fecha";
    lineSeries.dataFields.valueY = "aprob";
    lineSeries.tooltipText = "Aprobación: {valueY.value}";
    lineSeries.fillOpacity = 0.5;
    lineSeries.strokeWidth = 3;
    lineSeries.propertyFields.stroke = "lineColor";
    lineSeries.propertyFields.fill = "lineColor";

    let bullet = lineSeries.bullets.push(new am4charts.CircleBullet());
    bullet.circle.radius = 6;
    bullet.circle.fill = am4core.color("#fff");
    bullet.circle.strokeWidth = 3;

    chart.cursor = new am4charts.XYCursor();
    chart.cursor.behavior = "panX";
    chart.cursor.lineX.opacity = 0;
    chart.cursor.lineY.opacity = 0;

    chart.scrollbarX = new am4core.Scrollbar();
    chart.scrollbarX.parent = chart.bottomAxesContainer;
  }
}
</script>

<style>
  #chartdiv {
    width: 100%;
    height: 500px;
  }
</style>
