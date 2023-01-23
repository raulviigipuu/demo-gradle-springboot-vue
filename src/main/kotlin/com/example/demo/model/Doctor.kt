package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var birthDate: LocalDate? = null
    var medicalTeamId: Long? = null
    var active: Boolean = true

    constructor() {}

    constructor(id: Long?, firstName: String?, lastName: String?, birthDate: LocalDate?, medicalTeamId: Long?, active: Boolean) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.birthDate = birthDate
        this.medicalTeamId = medicalTeamId
        this.active = active
    }
}