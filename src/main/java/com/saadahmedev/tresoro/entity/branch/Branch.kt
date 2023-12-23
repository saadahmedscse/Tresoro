package com.saadahmedev.tresoro.entity.branch

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "table_branch")
data class Branch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "branch_code", nullable = false, unique = true, updatable = false)
    @JsonProperty("branch_code")
    var branchCode: String? = null,
    @Column(name = "branch_name", nullable = false)
    @JsonProperty("branch_name")
    var branchName: String? = null,
    @Column(name = "routing_number", nullable = false)
    @JsonProperty("routing_number")
    var routingNumber: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("created_at")
    var createdAt: String? = null,
    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updated_at")
    var updatedAt: String? = null
)
