package com.saadahmedev.tresoro.service.branch

import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.branch.BranchDto
import com.saadahmedev.tresoro.entity.branch.Branch
import com.saadahmedev.tresoro.repository.branch.BranchRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import com.saadahmedev.tresoro.util.DateUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
open class BranchServiceImpl : BranchService {

    @Autowired
    private lateinit var branchRepository: BranchRepository

    @Autowired
    @Qualifier(Constants.BeanQualifier.BRANCH_REQUEST_VALIDATOR)
    private lateinit var branchValidator: RequestValidator<Branch, BranchDto, Long, BranchRepository>

    override fun createBranch(branchDto: BranchDto?): ResponseEntity<*> {
        val validationResult = branchValidator.isCreateRequestValid(branchDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return try {
            branchRepository.save(requireNotNull(branchDto).toEntity())
            ServerResponse.created(message = "Branch created successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    override fun getBranch(id: Long): ResponseEntity<*> {
        val existenceResult = branchValidator.isExist(id, branchRepository)
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.response

        return ServerResponse.body(requireNotNull(existenceResult.result))
    }

    override fun getBranches(): ResponseEntity<*> {
        return try {
            ServerResponse.body(branchRepository.findAll())
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    @Transactional
    override fun updateBranch(id: Long, branchDto: BranchDto?): ResponseEntity<*> {
        val updateValidationResult = branchValidator.isUpdateRequestValid(id, branchDto, branchRepository)
        if (updateValidationResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return updateValidationResult.response

        val branch = requireNotNull(updateValidationResult.result)
        requireNotNull(branchDto)

        if (!branchDto.branchName.isNullOrBlank()) branch.branchName = branchDto.branchName
        if (!branchDto.branchCode.isNullOrBlank()) branch.branchCode = branchDto.branchCode
        branch.updatedAt = DateUtil.timeInstant()

        return try {
            branchRepository.save(branch)
            ServerResponse.ok(message = "Branch has been updated successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    @Transactional
    override fun deleteBranch(id: Long): ResponseEntity<*> {
        val existenceResult = branchValidator.isExist(id, branchRepository)
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.response

        return try {
            branchRepository.deleteById(id)
            ServerResponse.ok(message = "Branch has been deleted successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }
}