package com.saadahmedev.tresoro.controller.branch

import com.saadahmedev.tresoro.dto.branch.BranchDto
import com.saadahmedev.tresoro.service.branch.BranchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/branch")
open class BranchController {

    @Autowired
    private lateinit var branchService: BranchService

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    open fun createBranch(@RequestBody body: BranchDto?) = branchService.createBranch(body)

    @PostMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    open fun updateBranch(@PathVariable("id") id: Long, @RequestBody body: BranchDto?) = branchService.updateBranch(id, body)

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    open fun deleteBranch(@PathVariable("id") id: Long) = branchService.deleteBranch(id)

    @GetMapping
    open fun getBranches() = branchService.getBranches()

    @GetMapping("{id}")
    open fun getBranch(@PathVariable("id") id: Long) = branchService.getBranch(id)
}