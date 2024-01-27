package com.github.nyayurn.qbot

import com.github.nyayurn.qbot.database.BlockListMapper
import com.github.nyayurn.qbot.database.BlockListTable
import com.github.nyayurn.yutori.Event

val mailBlockFilter = { event: Event -> event.platform != "mail" }

val mailOnlyFilter = { event: Event -> event.platform == "mail" }

val blockListFilter = { event: Event ->
    val query = BlockListMapper.get(event.platform, event.selfId, event.user!!.id)
    if (query.totalRecordsInAllPages == 0) {
        BlockListMapper.insert(event.platform, event.selfId, event.user!!.id)
        true
    } else {
        val next = query.iterator().next()
        next[BlockListTable.status] != 0
    }
}