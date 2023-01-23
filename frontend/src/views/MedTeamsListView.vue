<template>
    <div class="container">
        <h1>Medical teams</h1>
        <div>
            <button class="btn btn-primary" @click="$router.push('/teams/add')">Add medical team</button>
        </div>
        <div>
            <div class="mb-3">
                <input v-model="searchTerm" type="search" class="form-control" id="searchInput"
                    placeholder="Search by name">
                <button class="btn btn-primary" @click="search()">Search</button>
            </div>
        </div>
        <div>
            <table v-if="!loading" class="table">
                <thead>
                    <tr>
                        <th><a @click="sort('name')" href="#">Name</a></th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="item in items" :key="item.id">
                        <td><a :href="`/teams/details/${item.id}`">{{ item.name }}</a></td>
                        <td>
                            <button class="btn btn-primary"
                                @click="$router.push(`/teams/edit/${item.id}`)">Edit</button>
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
import type { TeamData } from '@/types'
import axios from 'axios'
import { defineComponent, onMounted, ref } from 'vue'

export default defineComponent({
    name: 'List of medical teams',
    setup() {
        const items = ref<TeamData[]>([])
        const loading = ref<boolean>(true)
        const sortBy = ref<string>("name")
        const sortOrder = ref<string>("asc")
        const searchTerm = ref<string>("")
        const fetchItems = async () => {
            const response = await axios.get<TeamData[]>(`${API_URL}/teams/list`, {
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

        const deleteItem = async (item: TeamData) => {
            try {
                await axios.delete<TeamData>(`${API_URL}/teams/delete/${item.id}`)
                items.value = items.value.filter(i => i !== item)
            } catch (error) {
                console.error(error)
            }
        }

        return { items, loading, sort, search, searchTerm, deleteItem }
    }
})
</script>
