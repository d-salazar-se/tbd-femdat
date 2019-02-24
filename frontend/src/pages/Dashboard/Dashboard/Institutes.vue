<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Actividad instituciones asociadas a la mujer </h4>
    </div>
    <loading :conditional="tableData == null"></loading>
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
    Loading
  },
  
  data() {
    return {
      words: [],
      progress: 0,
      tableData: null
    }
  },
  
  mounted() {
    am4core.useTheme(am4themes_animated);

    let chart = am4core.create("chartdiv", am4charts.XYChart);
    chart.scrollbarX = new am4core.Scrollbar();

    axios.get(process.env.VUE_APP_API_ROOT+'tweets/institutions-activity')
      .then(response => {
        this.tableData = response.data;
        chart.data = this.tableData;
    });

    // Create axes
    let categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = "name";
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.minGridDistance = 30;
      
    // categoryAxis.renderer.labels.template.wrap = true;
    // categoryAxis.renderer.labels.template.maxWidth = 200;
    categoryAxis.renderer.labels.template.horizontalCenter = "right";
    categoryAxis.renderer.labels.template.verticalCenter = "middle";
    categoryAxis.renderer.labels.template.rotation = 0;
    categoryAxis.tooltip.disabled = true;
    categoryAxis.renderer.minHeight = 110;

    let valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.renderer.minWidth = 50;

    // Create series
    let series = chart.series.push(new am4charts.ColumnSeries());
    series.sequencedInterpolation = true;
    series.dataFields.valueY = "quantity";
    series.dataFields.categoryX = "name";
    series.tooltipText = "Tweets: [{categoryX}: bold]{valueY}[/]";
    series.columns.template.strokeWidth = 0;

    series.tooltip.pointerOrientation = "vertical";

    series.columns.template.column.cornerRadiusTopLeft = 10;
    series.columns.template.column.cornerRadiusTopRight = 10;
    series.columns.template.column.fillOpacity = 0.8;

    // on hover, make corner radiuses bigger
    let hoverState = series.columns.template.column.states.create("hover");
    hoverState.properties.cornerRadiusTopLeft = 0;
    hoverState.properties.cornerRadiusTopRight = 0;
    hoverState.properties.fillOpacity = 1;

    series.columns.template.adapter.add("fill", (fill, target)=>{
      return chart.colors.getIndex(target.dataItem.index);
    })

    // Cursor
    chart.cursor = new am4charts.XYCursor();
  },
}
</script>

<style>
  #chartdiv {
    width: 100%;
    height: 500px;
  }
</style>

