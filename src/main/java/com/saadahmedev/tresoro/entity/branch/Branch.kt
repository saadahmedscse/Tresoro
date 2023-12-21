package com.saadahmedev.tresoro.entity.branch

import jakarta.persistence.*

@Entity
@Table(name = "table_branch")
data class Branch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "branch_code", nullable = false, unique = true, updatable = false)
    var branchCode: String? = null,
    @Column(name = "branch_name", nullable = false)
    var branchName: String? = null,
    @Column(name = "routing_number", nullable = false)
    var routingNumber: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: String? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: String? = null
)
