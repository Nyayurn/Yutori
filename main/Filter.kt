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

package io.github.nyayurn.yutori

/**
 * 事件过滤器
 * @param type 事件
 */
fun eventTypeFilter(type: String) = { _: Actions, event: Event -> event.type == type }

/**
 * 平台过滤器
 * @param platform 平台
 */
fun platformFilter(platform: String) = { _: Actions, event: Event -> event.platform == platform }

/**
 * 自身 ID 过滤器
 * @param selfId 自身 ID
 */
fun selfIdFilter(selfId: String) = { _: Actions, event: Event -> event.selfId == selfId }

/**
 * 自身消息过滤器
 */
fun selfMessageFilter() = { _: Actions, event: Event -> event.user?.id != event.selfId }