<template>
    <div class="container">
        <h1>Doctors</h1>
        <div>
            <button class="btn btn-primary" @click="$router.push('/doctors/add')">Add doctor</button>
        </div>
        <div>
            <div class="mb-3">
                <input v-model="searchTerm" type="search" class="form-control" id="searchInput" placeholder="Search by first or last name">
                <button class="btn btn-primary" @click="search()">Search</button>
            </div>
        </div>
        <div>
            <table v-if="!loading" class="table">
                <thead>
                    <tr>
                        <th><a @click="sort('firstName')" href="#">First name</a></th>
                        <th><a @click="sort('lastName')" href="#">Last name</a></th>
                        <th><a @click="sort('birthDate')" href="#">Birthdate</a></th>
                        <th><a @click="sort('active')" href="#">Active</a></th>
                        <th><a @click="sort('medicalTeamId')" href="#">Medical team</a></th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in items" :key="item.id">
                        <td>{{ item.firstName }}</td>
                        <td>{{ item.lastName }}</td>
                        <td>{{ item.birthDate }}</td>
                        <td>{{ item.active }}</td>
                        <td>{{ item.medicalTeamId }}</td>
                        <td>
                            <button class="btn btn-primary"
                                @click="$router.push(`/doctors/edit/${item.id}`)">Edit</button>
                            <button class="btn btn-danger" @click="deleteItem(item)">Delete</button>
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
import type { DoctorData } from '@/types'
import axios from 'axios'
import { defineComponent, onMounted, ref } from 'vue'

export default defineComponent({
    name: 'List of doctors',
    setup() {
        const items = ref<DoctorData[]>([])
        const loading = ref<boolean>(true)
        const sortBy = ref<string>("lastName")
        const sortOrder = ref<string>("asc")
        const searchTerm = ref<string>("")
        const fetchItems = async () => {
            const response = await axios.get<DoctorData[]>(`${API_URL}/doctors/list`, {
                params: {
                    sortBy: sortBy.value,
                    sortOrder: sortOrder.value,
                    searchTerm: searchTerm.value
                }
            })
            items.value = response.data
            loading.value = false
        }

        onMounted(fetchItems)

        const search = () => {
            fetchItems()
        }

        const sort = (sortByParam: string) => {
            sortBy.value = sortByParam
            sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
            fetchItems()
        }

        const deleteItem = async (item: DoctorData) => {
            try {
                await axios.delete<DoctorData>(`${API_URL}/doctors/delete/${item.id}`)
                items.value = items.value.filter(i => i !== item)
            } catch (error) {
                console.error(error)
            }
        }
        return { sort, items, loading, searchTerm, search, deleteItem }
    }
})
</script>
