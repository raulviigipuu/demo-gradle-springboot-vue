package com.example.demo.controller

import com.example.demo.model.MedicalTeam
import com.example.demo.repositories.TeamsRepository
import com.example.demo.services.DoctorsService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
class TeamsController(val teamsRepository: TeamsRepository, val doctorsService: DoctorsService) {

    private val log = LoggerFactory.getLogger(TeamsController::class.java)

    @GetMapping("/list")
    fun list(
        @RequestParam(required = false, defaultValue = "name") sortBy: String,
        @RequestParam(required = false, defaultValue = "asc") sortOrder: String,
        @RequestParam(required = false, defaultValue = "") searchTerm: String,
    ): ResponseEntity<List<MedicalTeam>> {
        log.debug("Get a list of medical teams")
        return try {
            val teams = teamsRepository.findAll(
                searchTerm,
                Sort.by(
                    if (sortOrder == "asc") Sort.Direction.ASC else Sort.Direction.DESC,
                    sortBy
                )
            )
            ResponseEntity(teams, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): ResponseEntity<MedicalTeam> {
        log.debug("Get a team with id $id")
        return try {
            val team = teamsRepository.findById(id)
            if (team.isPresent) {
                ResponseEntity(team.get(), HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/create")
    fun create(@RequestBody team: MedicalTeam): ResponseEntity<MedicalTeam> {
        log.debug("Save medical team's data")
        return try {
            val savedTeam = teamsRepository.save(team)
            ResponseEntity(savedTeam, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/update/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody team: MedicalTeam): ResponseEntity<MedicalTeam> {
        val dbTeam: Optional<MedicalTeam> = teamsRepository.findById(id)
        return if (dbTeam.isPresent) {
            val t: MedicalTeam = dbTeam.get()
            t.name = team.name
            ResponseEntity(teamsRepository.save(t), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<HttpStatus> {
        log.debug("Delete medical team with id $id")
        return try {
            val team = teamsRepository.findById(id)
            if (team != null) {
                doctorsService.removeDoctorsFromTeam(id)
                teamsRepository.deleteById(id);
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            log.error("Error deleting team entity: $e")
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}