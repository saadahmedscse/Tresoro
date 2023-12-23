package com.saadahmedev.tresoro.repository.account

import com.saadahmedev.tresoro.entity.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import java.util.Optional

@Service
interface AccountRepository : JpaRepository<Account, Long> {

    fun findAllByCustomerId(userId: Long): List<Account>

    fun findByIdAndCustomerId(id: Long, customerId: Long): Optional<Account>


    @Query(
        "SELECT" +
                "    a.id," +
                "    a.account_number," +
                "    a.account_type," +
                "    a.balance," +
                "    a.branch_id," +
                "    a.created_at," +
                "    a.currency," +
                "    a.customer_id," +
                "    a.date_opened," +
                "    a.interest_rate," +
                "    a.last_transaction_date," +
                "    a.status," +
                "    a.updated_at," +
                "    b.id AS branch_idss," +
                "    b.branch_code," +
                "    b.branch_name," +
                "    b.created_at AS branch_created_at," +
                "    b.routing_number," +
                "    u.id AS user_idss," +
                "    u.password," +
                "    u.address," +
                "    u.created_at AS user_created_at," +
                "    u.date_of_birth," +
                "    u.email," +
                "    u.first_name," +
                "    u.full_name," +
                "    u.last_name," +
                "    u.phone," +
                "    u.role," +
                "    u.updated_at AS user_updated_at " +
                "FROM" +
                "    table_account a " +
                "LEFT JOIN" +
                "    table_branch b ON a.branch_id = b.id " +
                "LEFT JOIN" +
                "    table_user u ON a.customer_id = u.id " +
                "WHERE a.id = :accountId AND a.customer_id = :customerId",
        nativeQuery = true
    )
    fun getAccountWithLastTransactionDate(@Param("accountId") accountId: Long, @Param("customerId") customerId: Long): Optional<Account>
}