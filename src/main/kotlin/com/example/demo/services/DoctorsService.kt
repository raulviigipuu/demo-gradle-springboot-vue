package com.example.demo.services

import com.example.demo.repositories.DoctorsRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DoctorsService(val repository: DoctorsRepository) {

    private val log = LoggerFactory.getLogger(DoctorsService::class.java)

    @Transactional
    fun removeDoctorsFromTeam(teamId: Long) {
        repository.findAllByMedicalTeamId(teamId).forEach {
            log.debug("Removing team id $teamId from doctor ${it.firstName} ${it.lastName}")
            it.medicalTeamId = null
            repository.save(it)
        }
    }
}