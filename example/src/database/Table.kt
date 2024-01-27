package com.github.nyayurn.qbot.database

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object BlockListTable: Table<Nothing>("block_list") {
    val platform = varchar("platform")
    val selfId = varchar("self_id")
    val userId = varchar("user_id")
    val status = int("status")
}