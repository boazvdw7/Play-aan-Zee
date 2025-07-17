import { createRouter, createWebHashHistory } from 'vue-router'
import welcomeComponent from '@/components/WelcomeComponent.vue'
import Overview31 from "@/components/cabins/Overview31.vue";
import Overview32 from "@/components/cabins/Overview32.vue";
import UnknownRoute from "@/components/UnknownRoute.vue";
import Overview33 from "@/components/cabins/Overview33.vue";
import CabinsDetail32 from "@/components/cabins/CabinsDetail32.vue";
import CabinsDetail34 from "@/components/cabins/CabinsDetail34.vue";
import Overview37 from "@/components/cabins/Overview37.vue";
import CabinsDetail37 from "@/components/cabins/CabinsDetail37.vue";
import CabinsDetail37c from "@/components/cabins/CabinsDetail37c.vue";
import Overview37c from "@/components/cabins/Overview37c.vue";
import CabinsDetail45 from "@/components/cabins/CabinsDetail45.vue";
import Overview45 from "@/components/cabins/Overview45.vue";
import RentalOverview45 from "@/components/rentals/Overview45.vue";
import RentalsDetail45 from "@/components/rentals/RentalsDetail45.vue";
import SignIn from "@/components/SignIn.vue";
import SignUp from "@/components/SignUp.vue";
import RequestError from "@/components/RequestError.vue";

const routes = [
  {
    path: '/',
    component: welcomeComponent
  },
  {
    path: '/home',
    name: 'home',
    component: welcomeComponent
  },
  {
    path: '/sign-in',
    name:'signIn',
    component: SignIn
  },
  {
    path: '/sign-out',
    name:'signOut',
    beforeEnter: (to, from, next) => {
      next({ path: '/sign-in', query: { signOff: true }});
    }
  },
  {
    path: '/sign-up',
    name:'signUp',
    component: SignUp,
  },
  {
    path: '/overview31',
    name:'overview31',
    component: Overview31
  },
  {
    path: '/overview32',
    name:'overview32',
    component: Overview32
  },
  {
    path: '/overview33',
    name:'overview33',
    component: Overview33,
    children: [
        { path: ':id', name:'detail32', component: CabinsDetail32, props: true }
    ]
  },
  {
    path: '/overview34',
    name:'overview34',
    component: Overview33,
    children: [
        { path: ':id', name:'detail34', component: CabinsDetail34, props: true }
    ]
  },
  {
    path: '/overview37',
    name:'overview37',
    component: Overview37,
    children: [
      { path: ':id', name:'detail37', component: CabinsDetail37, props: true }
    ]
  },
  {
    path: '/overview37c',
    name:'overview37c',
    component: Overview37c,
    children: [
      { path: ':id', name:'detail37c', component: CabinsDetail37c, props: true }
    ]
  },
  {
    path: '/overview45',
    name:'overview45',
    component: Overview45,
    children: [
      { path: ':id', name:'detail45', component: CabinsDetail45, props: true }
    ]
  },
  {
    path: '/request-rental',
    name:'rentalOverview45',
    component: RentalOverview45,
    children: [
      { path: ':id', name:'rentalDetail45', component: RentalsDetail45, props: true }
    ]
  },
  {
    path: '/error',
    name: 'ERROR',
    component: RequestError,
    props: true
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'UnknownRoute',
    component: UnknownRoute}
  ];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router