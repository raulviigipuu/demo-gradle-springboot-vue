<template>
  <h1>{{ title }}</h1>
  <form @submit.prevent="submitForm">
    <div class="form-group">
      <label for="firstName">Name</label>
      <input v-model="formData.name" type="text" class="form-control" id="name" placeholder="Name">
    </div>
    <button type="submit" class="btn btn-primary">{{ actionText }}</button>
  </form>
</template>
  
<script lang="ts">
import { API_URL } from '@/config'
import router from '@/router'
import type { TeamData } from '@/types'
import axios from 'axios'
import { computed, defineComponent, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'


export default defineComponent({
  name: 'Add/edit form for teams',
  setup() {
    const formData = ref<TeamData>({
      id: null,
      name: '',
    })
    const route = useRoute()
    const id = computed(() => Number(route.params.id))
    const title = computed(() => id.value ? `Edit medical team ${id.value}` : 'Add a medical team')
    const actionText = computed(() => id.value ? 'Update' : 'Create')

    const fetchMedTeam = async () => {
      const response = await axios.get<TeamData>(`http://localhost:8080/api/teams/${id.value}`)
      formData.value = response.data
    }

    onMounted(fetchMedTeam)

    async function submitForm() {
      try {
        if (id.value) {
          await axios.put(`${API_URL}/teams/update/${id.value}`, formData.value)
          router.push('/teams')
        } else {
          await axios.post(`${API_URL}/teams/create`, formData.value)
          router.push('/teams')
        }
      } catch (error) {
        console.error(error)
        alert('An error occurred while saving the form')
      }
    }

    return { formData, title, actionText, submitForm }
  }
})
</script>
