import { createRouter, createWebHistory } from "vue-router"
import MainView from "./views/MainView.vue"
import DoctorsListView from "./views/DoctorsListView.vue"
import DoctorsEditView from "./views/DoctorsEditView.vue"
import MedTeamsListView from "./views/MedTeamsListView.vue"
import MedTeamsEditView from "./views/MedTeamsEditView.vue"
import MedTeamsDetailView from "./views/MedTeamsDetailView.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: MainView,
    },
    {
      path: "/doctors",
      name: "doctors",
      component: DoctorsListView,
    },
    {
      path: "/doctors/add",
      name: "doctorsAdd",
      component: DoctorsEditView,
    },
    {
      path: "/doctors/edit/:id",
      name: "doctorsEdit",
      component: DoctorsEditView,
    },
    {
      path: "/teams",
      name: "teams",
      component: MedTeamsListView,
    },
    ,
    {
      path: "/teams/add",
      name: "teamsAdd",
      component: MedTeamsEditView,
    },
    {
      path: "/teams/edit/:id",
      name: "teamsEdit",
      component: MedTeamsEditView,
    },
    {
      path: "/teams/details/:id",
      name: "teamsDetails",
      component: MedTeamsDetailView,
    },
  ],
})

export default router
