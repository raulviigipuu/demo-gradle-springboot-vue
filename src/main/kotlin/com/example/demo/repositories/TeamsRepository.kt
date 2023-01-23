package com.example.demo.repositories

import com.example.demo.model.MedicalTeam
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TeamsRepository : JpaRepository<MedicalTeam, Long> {
    @Query("SELECT t FROM MedicalTeam t WHERE (:searchTerm = '' OR LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    fun findAll(@Param("searchTerm") searchTerm: String, sort: Sort): List<MedicalTeam>
}