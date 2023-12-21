package com.saadahmedev.tresoro.service.validator

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
abstract class RequestValidator<E, T, ID, R: JpaRepository<E, ID>> {

    abstract fun isCreateRequestValid(body: T?): ResponseEntity<ApiResponse>

    open fun isUpdateRequestValid(id: ID & Any , body: T?, repository: R): ResponseWithResult<ResponseEntity<ApiResponse>, E> {
        val existenceResult = isExist(id, repository)
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult

        if (body == null) {
            return ResponseWithResult(
                ServerResponse.badRequest(message = "Request body is required"),
                null
            )
        }

        return ResponseWithResult(
            ServerResponse.ok(),
            existenceResult.result
        )
    }

    open fun isExist(id: ID & Any, repository: R): ResponseWithResult<ResponseEntity<ApiResponse>, E> {
        val optional = repository.findById(id)

        return if (optional.isPresent) {
            ResponseWithResult(
                ServerResponse.ok(),
                optional.get()
            )
        } else {
            ResponseWithResult(
                ServerResponse.badRequest(message = "Data found with the id $id"),
                null
            )
        }
    }
}