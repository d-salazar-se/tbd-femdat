<template>
  <card>
    <div slot="header">
      <h4 class="card-title">Afinidad feminismo paises</h4>
    </div>
    <loading :conditional="mapData == null"></loading>
    <div id="chartdiv"></div>
  </card>
</template>

<!-- Chart code -->
<script>
import * as am4core from "@amcharts/amcharts4/core";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import Loading from 'src/components/Loading';
import axios from 'axios';

export default {
  components: {
    Loading
  },

  data() {
    return {
      countryMaps : [
        { code: "AR", nombre: "Argentina" },
        { code: "CL", nombre: "Chile" },
        { code: "CO", nombre: "Colombia" },
        { code: "CR", nombre: "CostaRica" },
        { code: "DO", nombre: "DominicanRepublic" },
        { code: "EC", nombre: "Ecuador" },
        { code: "SV", nombre: "ElSalvador" },
        { code: "HN", nombre: "Honduras" },
        { code: "MX", nombre: "Mexico" },
        { code: "ES", nombre: "España" },
        { code: "BO", nombre: "Bolivia" },
        { code: "CU", nombre: "Cuba" },
        { code: "GT", nombre: "Guatemala" },
        { code: "GQ", nombre: "EquatorialGuinea" },
        { code: "NI", nombre: "Nicaragua" },
        { code: "PA", nombre: "Panama" },
        { code: "PY", nombre: "Paraguay" },
        { code: "PE", nombre: "Perú" },
        { code: "UY", nombre: "Uruguay" },
        { code: "VE", nombre: "Venezuela" }
      ],
      mapData: null,
    }
  },
  mounted() {
    this.getMapData();
  },

  methods:{
    getMapData(){
      axios.get(process.env.VUE_APP_API_ROOT+'tweets/countries-afinity')
        .then(response => {
          this.elements = response.data;
          var datT = this.elements.map(function(item) {
            return{
                  pais: item.name,
                  cantidad: item.quantity
                }
          });
          this.mapData = [];
          for(var i = 0;i<datT.length;i++){
            for(var x =0;x<20;x++){
              if(this.countryMaps[x].nombre === datT[i].pais ){
                this.mapData.push({id:this.countryMaps[x].code, value:datT[i].cantidad}); 
              }
            }
          }
          this.createMap();
        });
    },
    createMap(){
      am4core.useTheme(am4themes_animated);

      var chart = am4core.create("chartdiv", am4maps.MapChart);
      chart.hiddenState.properties.opacity = 0; // this creates initial fade-in

      chart.geodata = am4geodata_worldLow;
      chart.projection = new am4maps.projections.Orthographic();

      var title = chart.chartContainer.createChild(am4core.Label);

      title.fontSize = 20;
      title.paddingTop = 30;
      title.align = "center";
      title.zIndex = 100;

      var polygonSeries = new am4maps.MapPolygonSeries();
      chart.series.push(polygonSeries);
      var polygonTemplate = polygonSeries.mapPolygons.template;
      polygonTemplate.tooltipText = "{name}: {value.value.formatNumber('#.0')}";
      polygonSeries.heatRules.push({
        property: "fill",
        target: polygonSeries.mapPolygons.template,
        min: am4core.color("#d6586f"),
        max: am4core.color("#5db0ec")
      });
      polygonSeries.useGeodata = true;

      var heatLegend = chart.chartContainer.createChild(am4maps.HeatLegend);
      heatLegend.valign = "bottom";
      heatLegend.align = "left";
      heatLegend.width = am4core.percent(100);
      heatLegend.series = polygonSeries;
      heatLegend.orientation = "horizontal";
      heatLegend.padding(20, 20, 20, 20);
      heatLegend.valueAxis.renderer.labels.template.fontSize = 10;
      heatLegend.valueAxis.renderer.minGridDistance = 40;
      heatLegend.minValue = 0;
      heatLegend.maxValue = 100;

      polygonSeries.mapPolygons.template.events.on("over", event => {
        handleHover(event.target);
      });

      polygonSeries.mapPolygons.template.events.on("hit", event => {
        handleHover(event.target);
      });

      function handleHover(mapPolygon) {
        if (!isNaN(mapPolygon.dataItem.value)) {
          heatLegend.valueAxis.showTooltipAt(mapPolygon.dataItem.value);
        } else {
          heatLegend.valueAxis.hideTooltip();
        }
      }

      polygonSeries.mapPolygons.template.strokeOpacity = 0.4;
      polygonSeries.mapPolygons.template.events.on("out", () => {
        heatLegend.valueAxis.hideTooltip();
      });

      chart.zoomControl = new am4maps.ZoomControl();
      chart.zoomControl.valign = "top";
      chart.seriesContainer.draggable = false;
      chart.seriesContainer.resizable = false;
      var originalDeltaLongitude = 0;

      chart.seriesContainer.events.on("down", function() {
        originalDeltaLongitude = chart.deltaLongitude;
      });

      chart.seriesContainer.events.on("track", function(ev) {
        if (ev.target.isDown) {
          var pointer = ev.target.interactions.downPointers.getIndex(0);
          var startPoint = pointer.startPoint;
          var point = pointer.point;
          var shift = point.x - startPoint.x;
          chart.deltaLongitude = originalDeltaLongitude + shift / 2;
        }
      })
      // life expectancy data
      polygonSeries.data = this.mapData;
    }
  }
}
</script>

<style>
  #chartdiv {
    width: 100%;
    height: 500px;
  }
</style>
