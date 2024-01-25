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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.nyayurn.yutori.message.element

/**
 * 按钮
 * @property id 按钮的 ID
 * @property type 按钮的类型
 * @property href 按钮的链接
 * @property text 待输入文本
 * @property theme 按钮的样式
 */
class Button @JvmOverloads constructor(
    id: String? = null,
    type: String? = null,
    href: String? = null,
    text: String? = null,
    theme: String? = null
) : NodeMessageElement("button") {
    var id: String? by super.properties
    var type: String? by super.properties
    var href: String? by super.properties
    var text: String? by super.properties
    var theme: String? by super.properties

    init {
        this.id = id
        this.type = type
        this.href = href
        this.text = text
        this.theme = theme
    }
}