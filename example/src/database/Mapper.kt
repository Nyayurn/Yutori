package com.github.nyayurn.qbot.database

import org.ktorm.database.Database
import org.ktorm.dsl.*

object BlockListMapper {
    private val database = Database.connect("jdbc:mariadb://localhost:3306/yurn_qbot", user = "Yurn", password = "***")
    fun get(platform: String, selfId: String, userId: String) = database.from(BlockListTable).select(
        BlockListTable.status
    ).where {
        (BlockListTable.platform eq platform) and (BlockListTable.selfId eq selfId) and (BlockListTable.userId eq userId)
    }

    @JvmOverloads
    fun sub(platform: String, selfId: String, userId: String, step: Int = 1) = database.update(BlockListTable) {
        set(BlockListTable.status, BlockListTable.status - step)
        where {
            BlockListTable.platform eq platform
            BlockListTable.selfId eq selfId
            BlockListTable.userId eq userId
        }
    }

    fun insert(platform: String, selfId: String, userId: String) = database.insert(BlockListTable) {
        set(BlockListTable.platform, platform)
        set(BlockListTable.selfId, selfId)
        set(BlockListTable.userId, userId)
    }
}