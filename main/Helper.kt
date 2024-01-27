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

import com.github.nyayurn.yutori.message.element.*
import com.github.nyayurn.yutori.message.element.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * JsonObject 字符串 DSL 构建器
 */
@JvmSynthetic
inline fun jsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) = JsonObjectDSLBuilder().apply(dsl).toString()

/**
 * JsonArray 字符串 DSL 构建器
 */
@JvmSynthetic
inline fun jsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) = JsonArrayDSLBuilder().apply(dsl).toString()

class JsonObjectDSLBuilder {
    private val map = mutableMapOf<String, Any>()
    fun put(key: String, value: Any?) {
        value?.let { map[key] = it }
    }

    fun put(key: String, dsl: () -> Any?) {
        dsl()?.let { map[key] = it }
    }

    fun putJsonObj(key: String, dsl: JsonObjectDSLBuilder.() -> Unit) {
        map[key] = JsonObjectDSLBuilder().apply(dsl)
    }

    fun putJsonArr(key: String, dsl: JsonArrayDSLBuilder.() -> Unit) {
        map[key] = JsonArrayDSLBuilder().apply(dsl)
    }

    override fun toString() = map.entries.joinToString(",", "{", "}") { (key, value) ->
        buildString {
            append("\"$key\":")
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

class JsonArrayDSLBuilder {
    private val list = mutableListOf<Any>()
    fun add(value: Any?) {
        value?.let { list += it }
    }

    fun add(dsl: () -> Any?) {
        dsl()?.let { list += it }
    }

    fun addJsonArr(dsl: JsonArrayDSLBuilder.() -> Unit) {
        list += JsonArrayDSLBuilder().apply(dsl)
    }

    fun addJsonObj(dsl: JsonObjectDSLBuilder.() -> Unit) {
        list += JsonObjectDSLBuilder().apply(dsl)
    }

    override fun toString() = list.joinToString(",", "[", "]") { value ->
        buildString {
            append(
                when (value) {
                    is String -> "\"$value\""
                    else -> value.toString()
                }
            )
        }
    }
}

/**
 * 辅助类
 */
object MessageUtil {
    /**
     * 转义字符串
     * @return 转以后的字符串
     */
    @JvmStatic
    fun String.encode() = replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;")

    @JvmStatic
    fun String.decode() = replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&amp;", "&")

    /**
     * 提取出 Satori 消息字符串中的纯文本消息元素
     * @param raw 字符串
     * @return 元素链
     */
    @JvmStatic
    fun extractTextChain(raw: String) = mutableListOf<Text>().apply {
        for (node in Jsoup.parse(raw).body().childNodes()) if (node is TextNode) this.add(Text(node.text()))
    }

    /**
     * 将 Satori 消息字符串转换为元素链
     * @param raw 字符串
     * @return 元素链
     */
    @JvmStatic
    fun parseElementChain(raw: String) = mutableListOf<MessageElement>().apply {
        for (node in Jsoup.parse(raw).body().childNodes()) this.add(
            parseMessageElement(node) ?: throw IllegalStateException("unknown node: ${node.nodeName()}")
        )
    }

    /**
     * 解析消息元素
     * @param node 节点
     * @return 消息元素
     */
    private fun parseMessageElement(node: Node) = when (node) {
        is TextNode -> Text(node.text())
        is Element -> when (node.tagName()) {
            "at" -> at(node)
            "sharp" -> sharp(node)
            "a" -> href(node)
            "img" -> image(node)
            "audio" -> audio(node)
            "video" -> video(node)
            "file" -> file(node)
            "b" -> Bold((node.childNode(0) as TextNode).text())
            "strong" -> Strong((node.childNode(0) as TextNode).text())
            "i" -> Idiomatic((node.childNode(0) as TextNode).text())
            "em" -> Em((node.childNode(0) as TextNode).text())
            "u" -> Underline((node.childNode(0) as TextNode).text())
            "ins" -> Ins((node.childNode(0) as TextNode).text())
            "s" -> Strikethrough((node.childNode(0) as TextNode).text())
            "del" -> Delete((node.childNode(0) as TextNode).text())
            "spl" -> Spl((node.childNode(0) as TextNode).text())
            "code" -> Code((node.childNode(0) as TextNode).text())
            "sup" -> Sup((node.childNode(0) as TextNode).text())
            "sub" -> Sub((node.childNode(0) as TextNode).text())
            "br" -> Br
            "p" -> Paragraph
            "message" -> message(node)
            "quote" -> quote(node)
            "author" -> author(node)
            "button" -> button(node)
            else -> null
        }

        else -> null
    }

    private fun at(node: Node) = At().apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun sharp(node: Node) = Sharp(node.attr("id")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }


    private fun href(node: Node) = Href(node.attr("href")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun image(node: Node) = Image(node.attr("src")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun audio(node: Node) = Audio(node.attr("src")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun video(node: Node) = Video(node.attr("src")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun file(node: Node) = File(node.attr("src")).apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun message(node: Node) = Message().apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun quote(node: Node): Quote = Quote().apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
        for (sub in node.childNodes()) parseMessageElement(sub)?.let { this += it }
    }

    private fun author(node: Node) = Author().apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }

    private fun button(node: Node) = Button().apply {
        for (attr in node.attributes()) this[attr.key] = attr.value
    }
}