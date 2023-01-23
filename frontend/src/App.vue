<script setup lang="ts">
import { RouterView } from "vue-router"
</script>

<template>
  <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
    <div class="container-fluid">
      <a class="navbar-brand" href="/">Swedbank medical teams</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse"
        aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/" class="nav-link" v-bind:class="{ active: $route.path === '/' }">Main</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/doctors" class="nav-link" v-bind:class="{ active: $route.path === '/doctors' }">Doctors</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/teams" class="nav-link" v-bind:class="{ active: $route.path === '/teams' }">Medical teams</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <main class="container">
    <RouterView />
  </main>
</template>
