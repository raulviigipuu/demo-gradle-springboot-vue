<template>
    <div class="container">
        <h1>Doctors on team {{ teamData?.name }}</h1>
        <div class="form-group">
            <label for="doctorSelect">Select a doctor:</label>
            <select id="doctorSelect" v-model="selectedDoctorId" class="form-control-sm">
                <option v-for="doctor in activeDoctors" :value="doctor.id">{{ doctor.firstName }} {{ doctor.lastName }}
                </option>
            </select>
            <button class="btn btn-primary" @click="addDoctorToTeam()">Add doctor</button>
        </div>
        <div>
            <table v-if="!loading" class="table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in items" :key="item.id">
                        <td>{{ item.firstName }} {{ item.lastName }}</td>
                        <td>
                            <button class="btn btn-danger" @click="removeTeam(item)">Remove</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-else class="text-center">Loading...</div>
        </div>
    </div>
</template>

<script lang="ts">
import { API_URL } from '@/config'
import type { DoctorData, TeamData } from '@/types'
import axios from 'axios'
import { computed, defineComponent, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

export default defineComponent({
    name: 'List of doctors in the team',
    setup() {
        const route = useRoute()
        const teamId = computed(() => Number(route.params.id))
        const items = ref<DoctorData[]>([])
        const activeDoctors = ref<DoctorData[]>([])
        const selectedDoctorId = ref<number | null>()
        const teamData = ref<TeamData>()
        const loading = ref<boolean>(true)
        const fetchItems = async () => {
            const response = await axios.get<DoctorData[]>(`${API_URL}/doctors/list/${teamId.value}`)
            items.value = response.data
            loading.value = false
        }
        const fetchMedTeam = async () => {
            const response = await axios.get<TeamData>(`${API_URL}/teams/${teamId.value}`)
            teamData.value = response.data
        }
        const fetchActiveDoctors = async () => {
            const response = await axios.get<DoctorData[]>(`${API_URL}/doctors/list/active`)
            activeDoctors.value = response.data
        }

        onMounted(fetchItems)
        onMounted(fetchMedTeam)
        onMounted(fetchActiveDoctors)

        const addDoctorToTeam = async () => {
            if (selectedDoctorId.value == null) return
            try {
                const selectedDoctor = activeDoctors.value.filter(it => it.id == selectedDoctorId.value)[0]
                selectedDoctor.medicalTeamId = teamId.value
                items.value.push(selectedDoctor)
                await axios.put(`${API_URL}/doctors/update/${selectedDoctor.id}`, selectedDoctor)
                selectedDoctorId.value = null
            } catch (error) {
                console.error(error)
            }
        }

        const removeTeam = async (item: DoctorData) => {
            try {
                await axios.get<DoctorData>(`${API_URL}/doctors/removeTeam/${item.id}`)
                items.value = items.value.filter(i => i !== item)
            } catch (error) {
                console.error(error)
            }
        }
        return { items, activeDoctors, selectedDoctorId, teamData, loading, addDoctorToTeam, removeTeam }
    }
})
</script>
