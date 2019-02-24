import DashboardLayout from 'src/pages/Dashboard/Layout/DashboardLayout.vue'
// GeneralViews
import NotFound from 'src/pages/GeneralViews/NotFoundPage.vue'

// Page Headers
import DefaultHeader from 'src/pages/Dashboard/DefaultHeader'
import DashboardHeader from 'src/pages/Dashboard/Dashboard/DashboardHeader.vue'

// Dashboard pages
import Dashboard from 'src/pages/Dashboard/Dashboard/Dashboard.vue'

// Components pages
const Buttons = ()=>  import('src/pages/Dashboard/Components/Buttons.vue');
const GridSystem = ()=>  import('src/pages/Dashboard/Components/GridSystem.vue');
const Panels = ()=>  import('src/pages/Dashboard/Components/Panels.vue');
const SweetAlert = () => import('src/pages/Dashboard/Components/SweetAlert.vue');
const Notifications = ()=> import('src/pages/Dashboard/Components/Notifications.vue');
const Icons = ()=> import('src/pages/Dashboard/Components/Icons.vue');
const Typography = ()=> import('src/pages/Dashboard/Components/Typography.vue');

// Forms pages

// TableList pages
const RegularTables = () => import('src/pages/Dashboard/Tables/RegularTables.vue');
const ExtendedTables = () => import('src/pages/Dashboard/Tables/ExtendedTables.vue');
const PaginatedTables = () => import('src/pages/Dashboard/Tables/PaginatedTables.vue');
// Maps pages
const GoogleMaps = () => import('src/pages/Dashboard/Maps/GoogleMaps.vue');
const FullScreenMap = () => import('src/pages/Dashboard/Maps/FullScreenMap.vue');
const VectorMaps = () => import('src/pages/Dashboard/Maps/VectorMaps.vue');

// Charts
const Charts = () => import('src/pages/Dashboard/Charts.vue');

const PieChart = () => import('src/pages/Dashboard/Dashboard/StackedColumn.vue');
const Toptweets = () => import('src/pages/Dashboard/Dashboard/Toptweets.vue');
const Afinidad = () => import('src/pages/Dashboard/Dashboard/Afinidad.vue');
const Institutes = () => import('src/pages/Dashboard/Dashboard/Institutes.vue');
const TopfiveTweets = () => import('src/pages/Dashboard/Dashboard/TopfiveTweets.vue');
const Approval = () => import('src/pages/Dashboard/Dashboard/Approval.vue');
const Login = ()=>  import('src/pages/Dashboard/Pages/Login.vue');
const Keyword = () => import('src/pages/Dashboard/Dashboard/KeyWord.vue');
const Graph = () => import('src/pages/Dashboard/Dashboard/Graph.vue');


const routes = [
  {
    path: '/',
    component: DashboardLayout,
    redirect: '/twitter-activity',
    name: 'Home',
    children: [
      {
        path: 'twitter-activity',
        name: 'TwitterActivity',
        components: {default: DashboardHeader, header: DefaultHeader}
      },
      {
        path: 'twitter-afinity',
        name: 'TwitterAfinity',
        components: {default: PieChart, header: DefaultHeader}
      },
      {
        path: 'institutions',
        name: 'Institutions',
        components: {default: Institutes, header: DefaultHeader}
      },
      {
        path: 'approval',
        name: 'Approval',
        components: {default: Approval, header: DefaultHeader}
      },
      {
        path: 'hashtags',
        name: 'Graph',
        components: {default: Graph, header: DefaultHeader}
      },
      {
        path: 'top-figures',
        name: 'TopFigures',
        components: {default: Dashboard, header: DefaultHeader}
      },
      {
        path: 'top-topics',
        name: 'TopTopics',
        components: {default: Toptweets, header: DefaultHeader}
      },
      {
        path: 'top-tweets',
        name: 'Top Tweets',
        components: {default: TopfiveTweets, header: DefaultHeader}
      },
      {
        path: 'country-afinity',
        name: 'CountryAfinity',
        components: {default: Afinidad, header: DefaultHeader}
      },
      {
        path: 'country-activity',
        name: 'CountryActivity',
        components: {default: PaginatedTables, header: DefaultHeader}
      },
      {
        path: '/login',
        name: 'Login',
        component: Login
      },
      {
        path: '/key-words',
        name: 'Keywords',
        component: Keyword
      }
    ]
  },
  {path: '*', component: NotFound}
];

export default routes
