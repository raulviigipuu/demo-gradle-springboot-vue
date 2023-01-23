<template>
  <h1>{{ title }}</h1>
  <form @submit.prevent="submitForm">
    <div class="form-group">
      <label for="firstName">First Name</label>
      <input v-model="formData.firstName" type="text" class="form-control" id="firstName" placeholder="First name">
    </div>
    <div class="form-group">
      <label for="lastName">Last Name</label>
      <input v-model="formData.lastName" type="text" class="form-control" id="lastName" placeholder="Last name">
    </div>
    <div class="form-group">
      <label for="birthDate">Birth Date</label>
      <input v-model="formData.birthDate" type="date" class="form-control" id="birthDate">
    </div>
    <div class="form-group form-check">
      <input v-model="formData.active" type="checkbox" class="form-check-input" id="active">
      <label class="form-check-label" for="active">Active</label>
    </div>
    <button type="submit" class="btn btn-primary">{{ actionText }}</button>
  </form>
</template>

<script lang="ts">
import router from '@/router'
import type { DoctorData } from '@/types'
import axios from 'axios'
import { computed, defineComponent, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { API_URL } from '../config'

export default defineComponent({
  name: 'Add/edit form for doctors',
  setup() {
    const formData = ref<DoctorData>({
      id: null,
      firstName: '',
      lastName: '',
      birthDate: '',
      medicalTeamId: null,
      active: true,
    })
    const route = useRoute()
    const id = computed(() => Number(route.params.id))
    const title = computed(() => id.value ? `Edit doctor ${id.value}` : 'Add a doctor')
    const actionText = computed(() => id.value ? 'Update' : 'Create')

    const fetchDoctor = async () => {
      const response = await axios.get<DoctorData>(`${API_URL}/doctors/${id.value}`)
      formData.value = response.data
    }

    onMounted(fetchDoctor)

    async function submitForm() {
      try {
        if (id.value) {
          await axios.put(`${API_URL}/doctors/update/${id.value}`, formData.value)
          router.push('/doctors')
        } else {
          await axios.post(`${API_URL}/doctors/create`, formData.value)
          router.push('/doctors')
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