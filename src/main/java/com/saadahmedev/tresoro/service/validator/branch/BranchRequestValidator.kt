package com.saadahmedev.tresoro.service.validator.branch

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.branch.BranchDto
import com.saadahmedev.tresoro.entity.branch.Branch
import com.saadahmedev.tresoro.repository.branch.BranchRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.service.validator.ResponseWithResult
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.BRANCH_REQUEST_VALIDATOR)
class BranchRequestValidator : RequestValidator<Branch, BranchDto, Long, BranchRepository>() {

    @Autowired
    private lateinit var branchRepository: BranchRepository

    override fun isCreateRequestValid(body: BranchDto?): ResponseEntity<ApiResponse> {
        return when {
            body == null -> badRequest(message = "Branch request body is required")
            body.branchName.isNullOrBlank() -> badRequest(message = "Branch name is required")
            body.branchCode.isNullOrBlank() -> badRequest(message = "Branch code is required")
            branchRepository.findByBranchCode(body.branchCode).isPresent -> badRequest(message = "Branch with the code ${body.branchCode} already exist")
            body.routingNumber.isNullOrBlank() -> badRequest(message = "Routing number is required")

            else -> ServerResponse.ok()
        }
    }
}