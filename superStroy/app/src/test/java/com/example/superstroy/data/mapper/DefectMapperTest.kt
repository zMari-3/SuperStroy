package com.example.superstroy.data.mapper

import com.example.superstroy.data.local.DefectEntity
import com.example.superstroy.data.remote.dto.DefectDto
import com.example.superstroy.domain.model.DefectStatus
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefectMapperTest {

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = DefectDto(
            id = "123",
            title = "Дефект",
            description = "Описание",
            responsible = "Иванов Иван",
            term = "2026-11-20",
            createdDate = "2026-11-12T10:00:00Z",
            latitude = 55.75,
            longitude = 37.61,
            photoUrl = "http://photo.jpg",
            status = "OPEN"
        )

        val domain = dto.toDomain()

        assertEquals("123", domain.id)
        assertEquals("Дефект", domain.title)
        assertEquals(DefectStatus.OPEN, domain.status)
        assertEquals(55.75, domain.latitude ?: 0.0, 0.0)
    }

    @Test
    fun `toDomain handles null fields`() {
        val dto = DefectDto(
            id = "123",
            title = "Дефект",
            description = null,
            responsible = "Иванов Иван",
            term = "2026-11-20",
            createdDate = "2026-11-12T10:00:00Z",
            latitude = null,
            longitude = null,
            photoUrl = null,
            status = "OPEN"
        )

        val domain = dto.toDomain()

        assertNull(domain.description)
        assertNull(domain.latitude)
        assertNull(domain.photoUrl)
    }

    @Test
    fun `toDomain from Entity maps correctly`() {
        val entity = DefectEntity(
            id = "456",
            title = "Протечка",
            description = null,
            responsible = "Петров",
            term = LocalDate.now(),
            createdDate = LocalDateTime.now(),
            latitude = 10.0,
            longitude = 20.0,
            photoUrl = null,
            status = DefectStatus.CLOSED,
            isSynced = true
        )

        val domain = entity.toDomain()

        assertEquals("456", domain.id)
        assertEquals(DefectStatus.CLOSED, domain.status)
        assertEquals(10.0, domain.latitude ?: 0.0, 0.0)
    }
}