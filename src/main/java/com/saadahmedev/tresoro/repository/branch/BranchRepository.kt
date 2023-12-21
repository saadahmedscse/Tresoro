package com.saadahmedev.tresoro.repository.branch

import com.saadahmedev.tresoro.entity.branch.Branch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BranchRepository : JpaRepository<Branch, Long> {

    fun findByBranchCode(code: String): Optional<Branch>
}