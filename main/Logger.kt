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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.nyayurn.yutori

import com.github.nyayurn.yutori.Level.*

enum class Level {
    ERROR, WARN, INFO, DEBUG, TRACE
}

interface Logger {
    fun log(level: Level, msg: String) {
        when (level) {
            ERROR -> error(msg)
            WARN -> warn(msg)
            INFO -> info(msg)
            DEBUG -> debug(msg)
            TRACE -> trace(msg)
        }
    }

    fun error(msg: String)
    fun warn(msg: String)
    fun info(msg: String)
    fun debug(msg: String)
    fun trace(msg: String)
}

fun interface LoggerFactory {
    fun getLogger(clazz: Class<*>): Logger

    @JvmSynthetic
    fun getLogger(func: () -> Unit) = getLogger(func.javaClass)
}

object GlobalLoggerFactory : LoggerFactory {
    var factory: LoggerFactory = Slf4jLoggerFactory
    override fun getLogger(clazz: Class<*>) = factory.getLogger(clazz)
}

class Slf4jLogger(clazz: Class<*>) : Logger {
    private val logger = org.slf4j.LoggerFactory.getLogger(clazz)
    override fun error(msg: String) = logger.error(msg)
    override fun warn(msg: String) = logger.warn(msg)
    override fun info(msg: String) = logger.info(msg)
    override fun debug(msg: String) = logger.debug(msg)
    override fun trace(msg: String) = logger.trace(msg)
}

val Slf4jLoggerFactory = LoggerFactory { Slf4jLogger(it) }