package com.example.demo.repositories

import com.example.demo.model.Doctor
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DoctorsRepository : JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE (:searchTerm = '' OR LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    fun findAll(@Param("searchTerm") searchTerm: String, sort: Sort): List<Doctor>
    fun findAllByMedicalTeamId(teamId: Long): List<Doctor>
    fun findAllByActive(active: Boolean): List<Doctor>
}