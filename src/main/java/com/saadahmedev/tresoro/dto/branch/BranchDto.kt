package com.saadahmedev.tresoro.dto.branch

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.entity.branch.Branch
import com.saadahmedev.tresoro.util.DateUtil

data class BranchDto(
    @JsonProperty("branch_name")
    val branchName: String? = null,
    @JsonProperty("branch_code")
    val branchCode: String? = null,
    @JsonProperty("routing_number")
    val routingNumber: String? = null
) {
    fun toEntity(): Branch {
        return Branch(
            branchCode = branchCode,
            branchName = branchName,
            routingNumber = routingNumber,
            createdAt = DateUtil.timeInstant(),
            updatedAt = DateUtil.timeInstant()
        )
    }
}
