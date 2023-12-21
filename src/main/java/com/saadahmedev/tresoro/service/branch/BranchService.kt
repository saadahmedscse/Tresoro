package com.saadahmedev.tresoro.service.branch

import com.saadahmedev.tresoro.dto.branch.BranchDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface BranchService {

    fun createBranch(branchDto: BranchDto?): ResponseEntity<*>

    fun getBranch(id: Long): ResponseEntity<*>

    fun getBranches(): ResponseEntity<*>

    fun updateBranch(id: Long, branchDto: BranchDto?): ResponseEntity<*>

    fun deleteBranch(id: Long): ResponseEntity<*>
}