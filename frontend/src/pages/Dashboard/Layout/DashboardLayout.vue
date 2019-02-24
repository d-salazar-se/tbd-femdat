<template>
  <div class="wrapper" :class="{'nav-open': $sidebar.showSidebar}">
    <notifications></notifications>
    <side-bar>
      <template slot="links">
        <sidebar-item :link="{name: 'Gráficos Actividad', icon: 'now-ui-icons business_chart-bar-32'}"> 
          <sidebar-item :link="{name: 'Actividad Tweets Semanales', path: '/twitter-activity'}"></sidebar-item>
          <sidebar-item :link="{name: 'Rechazo vs Aceptación', path: '/twitter-afinity'}"></sidebar-item>
          <sidebar-item :link="{name: 'Actividad Instituciones ', path: '/institutions'}"></sidebar-item>
          <sidebar-item :link="{name: '% Aprobación Semanal', path: '/approval'}"></sidebar-item>
          <sidebar-item :link="{name: '# de Twitteros Feministas', path: '/hashtags'}"></sidebar-item>
        </sidebar-item>
        
        <sidebar-item :link="{name: 'Rankings', icon: 'now-ui-icons files_single-copy-04'}">
          <sidebar-item :link="{name: 'Twitteros Influyentes', path: '/top-figures'}"></sidebar-item>
          <sidebar-item :link="{name: 'Top 5 keywords', path: '/top-topics'}"></sidebar-item>
          <sidebar-item :link="{name: 'Top 5 tweets', path: '/top-tweets'}"></sidebar-item>

        </sidebar-item>

        <sidebar-item :link="{name: 'Países', icon: 'now-ui-icons location_map-big'}">
          <sidebar-item :link="{name: 'Afinidad Feminismo', path: '/country-afinity'}"></sidebar-item>
          <sidebar-item :link="{name: 'Actividad', path: '/country-activity'}"></sidebar-item>
        </sidebar-item>
      </template>
    </side-bar>

    <div class="main-panel">
      <top-navbar></top-navbar>
      <router-view name="header"></router-view>

      <div :class="{content: !$route.meta.hideContent}" @click="toggleSidebar">
        <zoom-center-transition :duration="200" mode="out-in">
          <!-- your content here -->
          <router-view></router-view>
        </zoom-center-transition>
      </div>
      <content-footer v-if="!$route.meta.hideFooter"></content-footer>
    </div>
  </div>
</template>

<script>
  /* eslint-disable no-new */
  import PerfectScrollbar from 'perfect-scrollbar'
  import 'perfect-scrollbar/css/perfect-scrollbar.css'

  function hasElement (className) {
    return document.getElementsByClassName(className).length > 0;
  }

  function initScrollbar (className) {
    if (hasElement(className)) {
      new PerfectScrollbar(`.${className}`);
    } else {
      // try to init it later in case this component is loaded async
      setTimeout(() => {
        initScrollbar(className);
      }, 100);
    }
  }

  import TopNavbar from './TopNavbar.vue'
  import ContentFooter from './ContentFooter.vue'
  import DashboardContent from './Content.vue'
  import MobileMenu from './Extra/MobileMenu.vue'
  import UserMenu from './Extra/UserMenu.vue'
  import {SlideYDownTransition, ZoomCenterTransition} from 'vue2-transitions'

  export default {
    components: {
      TopNavbar,
      ContentFooter,
      DashboardContent,
      MobileMenu,
      UserMenu,
      SlideYDownTransition,
      ZoomCenterTransition
    },
      sidebarLinks: {
        type: Array,
        default: () => ["hola","paloma"]
      },
    methods: {
      toggleSidebar () {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      }
    },
    mounted () {
      let docClasses = document.body.classList;
      let isWindows = navigator.platform.startsWith('Win');
      if (isWindows) {
        // if we are on windows OS we activate the perfectScrollbar function
        initScrollbar('sidebar');
        initScrollbar('sidebar-wrapper');

        docClasses.add('perfect-scrollbar-on')
      } else {
        docClasses.add('perfect-scrollbar-off')
      }
    }
  };

</script>
<style lang="scss" scoped>
  $scaleSize: .95;
  @keyframes zoomIn95 {
    from {
      opacity: 0;
      transform: scale3d($scaleSize, $scaleSize, $scaleSize);
    }
    to {
      opacity: 1;
    }
  }
  .main-panel .zoomIn {
    animation-name: zoomIn95;
  }
  @keyframes zoomOut95 {
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
      transform: scale3d($scaleSize, $scaleSize, $scaleSize);
    }
  }
  .main-panel .zoomOut {
    animation-name: zoomOut95;
  }
</style>
