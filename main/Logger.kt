/*
Copyright (c) 2023 Yurn
Yutori is licensed under Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
         http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
See the Mulan PSL v2 for more details.
 */

@file:Suppress("unused")

package com.github.nyayurn.yutori

import com.github.nyayurn.yutori.Level.*
import org.slf4j.LoggerFactory

enum class Level {
    ERROR, WARN, INFO, DEBUG, TRACE
}

interface Logger {
    fun log(level: Level, msg: String, clazz: Class<*>) {
        when (level) {
            ERROR -> error(msg, clazz)
            WARN -> warn(msg, clazz)
            INFO -> info(msg, clazz)
            DEBUG -> debug(msg, clazz)
            TRACE -> trace(msg, clazz)
        }
    }

    fun error(msg: String, clazz: Class<*>)
    fun warn(msg: String, clazz: Class<*>)
    fun info(msg: String, clazz: Class<*>)
    fun debug(msg: String, clazz: Class<*>)
    fun trace(msg: String, clazz: Class<*>)
}

object Slf4jLogger : Logger {
    override fun error(msg: String, clazz: Class<*>) = LoggerFactory.getLogger(clazz).error(msg)
    override fun warn(msg: String, clazz: Class<*>) = LoggerFactory.getLogger(clazz).warn(msg)
    override fun info(msg: String, clazz: Class<*>) = LoggerFactory.getLogger(clazz).info(msg)
    override fun debug(msg: String, clazz: Class<*>) = LoggerFactory.getLogger(clazz).debug(msg)
    override fun trace(msg: String, clazz: Class<*>) = LoggerFactory.getLogger(clazz).trace(msg)
}