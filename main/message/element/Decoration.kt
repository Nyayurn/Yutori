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

package com.github.nyayurn.yutori.message.element

/**
 * 修饰元素
 * @property text 被修饰的文本
 */
abstract class DecorationElement(
    nodeName: String,
    text: String
) : NodeMessageElement(nodeName) {
    var text: String
        get() = (super.children[0] as Text).text
        set(value) {
            if (super.children.isEmpty()) {
                super.children += Text(value)
            } else {
                (super.children[0] as Text).text = value
            }
        }

    init {
        this.text = text
    }
}

/**
 * 粗体
 */
class Bold(text: String) : DecorationElement("b", text)

/**
 * 粗体
 */
class Strong(text: String) : DecorationElement("strong", text)

/**
 * 斜体
 */
class Idiomatic(text: String) : DecorationElement("i", text)

/**
 * 斜体
 */
class Em(text: String) : DecorationElement("em", text)

/**
 * 下划线
 */
class Underline(text: String) : DecorationElement("u", text)

/**
 * 下划线
 */
class Ins(text: String) : DecorationElement("ins", text)

/**
 * 删除线
 */
class Strikethrough(text: String) : DecorationElement("s", text)

/**
 * 删除线
 */
class Delete(text: String) : DecorationElement("del", text)

/**
 * 剧透
 */
class Spl(text: String) : DecorationElement("spl", text)

/**
 * 代码
 */
class Code(text: String) : DecorationElement("code", text)

/**
 * 上标
 */
class Sup(text: String) : DecorationElement("sup", text)

/**
 * 下标
 */
class Sub(text: String) : DecorationElement("sub", text)