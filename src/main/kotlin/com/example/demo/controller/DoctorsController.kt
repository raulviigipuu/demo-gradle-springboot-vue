package com.example.demo.controller

import com.example.demo.model.Doctor
import com.example.demo.repositories.DoctorsRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("/api/doctors")
class DoctorsController(val doctorsRepository: DoctorsRepository) {

    private val log = LoggerFactory.getLogger(DoctorsController::class.java)

    @GetMapping("/list")
    fun list(
        @RequestParam(required = false, defaultValue = "lastName") sortBy: String,
        @RequestParam(required = false, defaultValue = "asc") sortOrder: String,
        @RequestParam(required = false, defaultValue = "") searchTerm: String,
    ): ResponseEntity<List<Doctor>> {
        log.debug("Get a list of doctors")
        return try {
            val doctors = doctorsRepository.findAll(
                searchTerm,
                Sort.by(
                    if (sortOrder == "asc") Sort.Direction.ASC else Sort.Direction.DESC,
                    sortBy
                )
            )
            ResponseEntity(doctors, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/list/{teamId}")
    fun list(@PathVariable("teamId") teamId: Long): ResponseEntity<List<Doctor>> {
        log.debug("Get a list of doctors in a team $teamId")
        return try {
            val doctors = doctorsRepository.findAllByMedicalTeamId(teamId)
            log.debug("Found ${doctors.size} doctors")
            ResponseEntity(doctors, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/list/active")
    fun listActive(): ResponseEntity<List<Doctor>> {
        log.debug("Get a list of active doctors")
        return try {
            val doctors = doctorsRepository.findAllByActive(true)
            log.debug("Found ${doctors.size} active doctors")
            ResponseEntity(doctors, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<Doctor> {
        log.debug("Get a doctors with id $id")
        return try {
            val doctor = doctorsRepository.findById(id)
            if (doctor.isPresent) {
                ResponseEntity(doctor.get(), HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/create")
    fun create(@RequestBody doctor: Doctor): ResponseEntity<Doctor> {
        log.debug("Save doctor's data")
        return try {
            val savedDoctor = doctorsRepository.save(doctor)
            ResponseEntity(savedDoctor, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/update/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody doctor: Doctor): ResponseEntity<Doctor> {
        val dbDoc: Optional<Doctor> = doctorsRepository.findById(id)
        return if (dbDoc.isPresent) {
            val doc: Doctor = dbDoc.get()
            doc.firstName = doctor.firstName
            doc.lastName = doctor.lastName
            doc.birthDate = doctor.birthDate
            doc.active = doctor.active
            doc.medicalTeamId = if (doc.active) doctor.medicalTeamId else null
            ResponseEntity(doctorsRepository.save(doc), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/removeTeam/{id}")
    fun removeTeam(@PathVariable("id") id: Long): ResponseEntity<Doctor> {
        val dbDoc: Optional<Doctor> = doctorsRepository.findById(id)
        return if (dbDoc.isPresent) {
            val doc: Doctor = dbDoc.get()
            doc.medicalTeamId = null
            ResponseEntity(doctorsRepository.save(doc), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<HttpStatus> {
        log.debug("Delete doctor with id $id")
        return try {
            val doc: Optional<Doctor> = doctorsRepository.findById(id)
            if (doc.isPresent) {
                doctorsRepository.deleteById(id);
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            log.error("Error deleting doctor entity: $e")
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
