package com.saadahmedev.tresoro.repository.branch

import com.saadahmedev.tresoro.entity.branch.Branch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BranchRepository : JpaRepository<Branch, Long> {
}