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

package com.github.nyayurn.yutori.message

import com.github.nyayurn.yutori.message.element.*

/**
 * 消息 DSL 构造器, 供 Kotlin 使用者使用
 * @param dsl DSL
 * @return 消息
 */
@JvmSynthetic
inline fun message(dsl: MessageDSLBuilder.() -> Unit) = MessageDSLBuilder().apply(dsl).build()

@DslMarker
annotation class MessageDSL

@MessageDSL
class MessageDSLBuilder {
    val list = mutableListOf<MessageElement>()

    fun custom(element: Custom) = list.add(element)
    fun custom(text: String) = list.add(Custom(text))
    inline fun custom(dsl: () -> String) = custom(Custom(dsl()))

    fun text(element: Text) = list.add(element)
    fun text(text: String) = list.add(Text(text))
    inline fun text(dsl: () -> String) = text(dsl())

    fun at(element: At) = list.add(element)
    fun at(
        id: String? = null,
        name: String? = null,
        role: String? = null,
        type: String? = null
    ) = list.add(At(id, name, role, type))

    inline fun at(dsl: AtBuilder.() -> Unit) = at(AtBuilder().apply(dsl).build())

    fun sharp(element: Sharp) = list.add(element)
    fun sharp(id: String, name: String? = null) = list.add(Sharp(id, name))
    inline fun sharp(dsl: SharpBuilder.() -> Unit) = sharp(SharpBuilder().apply(dsl).build())

    fun a(element: Href) = list.add(element)
    fun a(href: String) = list.add(Href(href))
    inline fun a(dsl: () -> String) = a(Href(dsl()))

    fun img(element: Image) = list.add(element)
    fun img(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null
    ) = list.add(Image(src, cache, timeout, width, height))

    inline fun img(dsl: ImageBuilder.() -> Unit) = img(ImageBuilder().apply(dsl).build())

    fun audio(element: Audio) = list.add(element)
    fun audio(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = list.add(Audio(src, cache, timeout))

    inline fun audio(dsl: AudioBuilder.() -> Unit) = audio(AudioBuilder().apply(dsl).build())

    fun video(element: Video) = list.add(element)
    fun video(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = list.add(Video(src, cache, timeout))

    inline fun video(dsl: VideoBuilder.() -> Unit) = video(VideoBuilder().apply(dsl).build())

    fun file(element: File) = list.add(element)
    fun file(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = list.add(File(src, cache, timeout))

    inline fun file(dsl: FileBuilder.() -> Unit) = file(FileBuilder().apply(dsl).build())

    fun b(element: Bold) = list.add(element)
    fun b(text: String) = list.add(Bold(text))
    inline fun b(dsl: () -> String) = b(Bold(dsl()))

    fun strong(element: Strong) = list.add(element)
    fun strong(text: String) = list.add(Strong(text))
    inline fun strong(dsl: () -> String) = strong(Strong(dsl()))

    fun i(element: Idiomatic) = list.add(element)
    fun i(text: String) = list.add(Idiomatic(text))
    inline fun i(dsl: () -> String) = i(Idiomatic(dsl()))

    fun em(element: Em) = list.add(element)
    fun em(text: String) = list.add(Em(text))
    inline fun em(dsl: () -> String) = em(Em(dsl()))

    fun u(element: Underline) = list.add(element)
    fun u(text: String) = list.add(Underline(text))
    inline fun u(dsl: () -> String) = u(Underline(dsl()))

    fun ins(element: Ins) = list.add(element)
    fun ins(text: String) = list.add(Ins(text))
    inline fun ins(dsl: () -> String) = ins(Ins(dsl()))

    fun s(element: Strikethrough) = list.add(element)
    fun s(text: String) = list.add(Strikethrough(text))
    inline fun s(dsl: () -> String) = s(Strikethrough(dsl()))

    fun del(element: Delete) = list.add(element)
    fun del(text: String) = list.add(Delete(text))
    inline fun del(dsl: () -> String) = del(Delete(dsl()))

    fun spl(element: Spl) = list.add(element)
    fun spl(text: String) = list.add(Spl(text))
    inline fun spl(dsl: () -> String) = spl(Spl(dsl()))

    fun code(element: Code) = list.add(element)
    fun code(text: String) = list.add(Code(text))
    inline fun code(dsl: () -> String) = code(Code(dsl()))

    fun sup(element: Sup) = list.add(element)
    fun sup(text: String) = list.add(Sup(text))
    inline fun sup(dsl: () -> String) = sup(Sup(dsl()))

    fun sub(element: Sub) = list.add(element)
    fun sub(text: String) = list.add(Sub(text))
    inline fun sub(dsl: () -> String) = sub(Sub(dsl()))

    fun br() = list.add(Br)
    fun p() = list.add(Paragraph)

    fun message(element: Message) = list.add(element)
    fun message(
        id: String? = null,
        forward: Boolean? = null,
        vararg element: MessageElement
    ) = list.add(Message(id, forward).apply { children.addAll(element) })

    inline fun message(dsl: MessageBuilder.() -> Unit) = message(MessageBuilder().apply(dsl).build())

    fun quote(element: Quote) = list.add(element)
    fun quote(vararg elements: MessageElement) = list.add(Quote().apply { children.addAll(elements) })
    inline fun quote(dsl: MessageDSLBuilder.() -> Unit) = quote(Quote().apply {
        children.addAll(MessageDSLBuilder().apply(dsl).list)
    })

    fun author(element: Author) = list.add(element)
    fun author(
        id: String? = null,
        name: String? = null,
        avatar: String? = null
    ) = list.add(Author(id, name, avatar))

    inline fun author(dsl: AuthorBuilder.() -> Unit) = author(AuthorBuilder().apply(dsl).build())

    fun button(element: Button) = list.add(element)
    fun button(
        id: String? = null,
        type: String? = null,
        href: String? = null,
        text: String? = null,
        theme: String? = null
    ) = list.add(Button(id, type, href, text, theme))

    inline fun button(dsl: ButtonBuilder.() -> Unit) = button(ButtonBuilder().apply(dsl).build())

    fun build() = list.joinToString("") { it.toString() }
    override fun toString() = "MessageDSLBuilder(list=$list)"

    @MessageDSL
    class AtBuilder {
        var id: String? = null
        var name: String? = null
        var role: String? = null
        var type: String? = null

        fun id(lambda: () -> String) {
            this.id = lambda()
        }

        fun name(lambda: () -> String) {
            this.name = lambda()
        }

        fun role(lambda: () -> String) {
            this.role = lambda()
        }

        fun type(lambda: () -> String) {
            this.type = lambda()
        }

        fun build() = At(id, name, role, type)
    }

    @MessageDSL
    class SharpBuilder {
        var id: String = ""
        var name: String? = null

        fun id(lambda: () -> String) {
            this.id = lambda()
        }

        fun name(lambda: () -> String) {
            this.name = lambda()
        }

        fun build() = Sharp(id, name)
    }

    @MessageDSL
    class ImageBuilder {
        var src: String = ""
        var cache: Boolean? = null
        var timeout: String? = null
        var width: Number? = null
        var height: Number? = null

        fun src(lambda: () -> String) {
            this.src = lambda()
        }

        fun cache(lambda: () -> Boolean) {
            this.cache = lambda()
        }

        fun timeout(lambda: () -> String) {
            this.timeout = lambda()
        }

        fun width(lambda: () -> Number) {
            this.width = lambda()
        }

        fun height(lambda: () -> Number) {
            this.height = lambda()
        }

        fun build() = Image(src, cache, timeout, width, height)
    }

    @MessageDSL
    class AudioBuilder {
        var src: String = ""
        var cache: Boolean? = null
        var timeout: String? = null

        fun src(lambda: () -> String) {
            this.src = lambda()
        }

        fun cache(lambda: () -> Boolean) {
            this.cache = lambda()
        }

        fun timeout(lambda: () -> String) {
            this.timeout = lambda()
        }

        fun build() = Audio(src, cache, timeout)
    }

    @MessageDSL
    class VideoBuilder {
        var src: String = ""
        var cache: Boolean? = null
        var timeout: String? = null

        fun src(lambda: () -> String) {
            this.src = lambda()
        }

        fun cache(lambda: () -> Boolean) {
            this.cache = lambda()
        }

        fun timeout(lambda: () -> String) {
            this.timeout = lambda()
        }

        fun build() = Video(src, cache, timeout)
    }

    @MessageDSL
    class FileBuilder {
        var src: String = ""
        var cache: Boolean? = null
        var timeout: String? = null

        fun src(lambda: () -> String) {
            this.src = lambda()
        }

        fun cache(lambda: () -> Boolean) {
            this.cache = lambda()
        }

        fun timeout(lambda: () -> String) {
            this.timeout = lambda()
        }

        fun build() = File(src, cache, timeout)
    }

    @MessageDSL
    class MessageBuilder {
        var id: String? = null
        var forward: Boolean? = null
        val elements = mutableListOf<MessageElement>()

        fun id(lambda: () -> String) {
            id = lambda()
        }

        fun forward(lambda: () -> Boolean) {
            forward = lambda()
        }

        operator fun set(index: Int, element: MessageElement) {
            elements[index] = element
        }

        operator fun plusAssign(element: MessageElement) {
            elements += element
        }

        fun build() = Message(id, forward).apply { children.addAll(elements) }
    }

    @MessageDSL
    class AuthorBuilder {
        var id: String? = null
        var name: String? = null
        var avatar: String? = null
        fun id(lambda: () -> String) {
            id = lambda()
        }

        fun name(lambda: () -> String) {
            name = lambda()
        }

        fun avatar(lambda: () -> String) {
            avatar = lambda()
        }

        fun build() = Author(id, name, avatar)
    }

    @MessageDSL
    class ButtonBuilder {
        var id: String? = null
        var type: String? = null
        var href: String? = null
        var text: String? = null
        var theme: String? = null
        fun id(lambda: () -> String) {
            id = lambda()
        }

        fun type(lambda: () -> String) {
            type = lambda()
        }

        fun href(lambda: () -> String) {
            href = lambda()
        }

        fun text(lambda: () -> String) {
            text = lambda()
        }

        fun theme(lambda: () -> String) {
            theme = lambda()
        }

        fun build() = Button(id, type, href, text, theme)
    }
}

/**
 * 消息链式构造器, 供 Java 使用者使用
 * @property list 消息列表
 */
class MessageChainBuilder {
    val list = mutableListOf<MessageElement>()

    fun custom(element: Custom) = this.apply { list.add(element) }
    fun custom(text: String) = this.apply { list.add(Custom(text)) }

    fun text(element: Text) = this.apply { list.add(element) }
    fun text(text: String) = this.apply { list.add(Text(text)) }

    fun at(element: At) = this.apply { list.add(element) }

    @JvmOverloads
    fun at(
        id: String? = null,
        name: String? = null,
        role: String? = null,
        type: String? = null
    ) = this.apply { list.add(At(id, name, role, type)) }

    fun sharp(element: Sharp) = this.apply { list.add(element) }

    @JvmOverloads
    fun sharp(id: String, name: String? = null) = this.apply { list.add(Sharp(id, name)) }

    fun a(element: Href) = this.apply { list.add(element) }
    fun a(href: String) = this.apply { list.add(Href(href)) }

    fun img(element: Image) = this.apply { list.add(element) }

    @JvmOverloads
    fun img(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null,
        width: Number? = null,
        height: Number? = null
    ) = this.apply { list.add(Image(src, cache, timeout, width, height)) }

    fun audio(element: Audio) = this.apply { list.add(element) }

    @JvmOverloads
    fun audio(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = this.apply { list.add(Audio(src, cache, timeout)) }

    fun video(element: Video) = this.apply { list.add(element) }

    @JvmOverloads
    fun video(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = this.apply { list.add(Video(src, cache, timeout)) }

    fun file(element: File) = this.apply { list.add(element) }

    @JvmOverloads
    fun file(
        src: String,
        cache: Boolean? = null,
        timeout: String? = null
    ) = this.apply { list.add(File(src, cache, timeout)) }

    fun b(element: Bold) = this.apply { list.add(element) }
    fun b(text: String) = this.apply { list.add(Bold(text)) }

    fun strong(element: Strong) = this.apply { list.add(element) }
    fun strong(text: String) = this.apply { list.add(Strong(text)) }

    fun i(element: Idiomatic) = this.apply { list.add(element) }
    fun i(text: String) = this.apply { list.add(Idiomatic(text)) }

    fun em(element: Em) = this.apply { list.add(element) }
    fun em(text: String) = this.apply { list.add(Em(text)) }

    fun u(element: Underline) = this.apply { list.add(element) }
    fun u(text: String) = this.apply { list.add(Underline(text)) }

    fun ins(element: Ins) = list.add(element)
    fun ins(text: String) = this.apply { list.add(Ins(text)) }

    fun s(element: Strikethrough) = this.apply { list.add(element) }
    fun s(text: String) = this.apply { list.add(Strikethrough(text)) }

    fun del(element: Delete) = this.apply { list.add(element) }
    fun del(text: String) = this.apply { list.add(Delete(text)) }

    fun spl(element: Spl) = this.apply { list.add(element) }
    fun spl(text: String) = this.apply { list.add(Spl(text)) }

    fun code(element: Code) = this.apply { list.add(element) }
    fun code(text: String) = this.apply { list.add(Code(text)) }

    fun sup(element: Sup) = this.apply { list.add(element) }
    fun sup(text: String) = this.apply { list.add(Sup(text)) }

    fun sub(element: Sub) = this.apply { list.add(element) }
    fun sub(text: String) = this.apply { list.add(Sub(text)) }

    fun br() = this.apply { list.add(Br) }
    fun p() = this.apply { list.add(Paragraph) }

    fun message(element: Message) = this.apply { list.add(element) }

    @JvmOverloads
    fun message(
        id: String? = null,
        forward: Boolean? = null,
        vararg elements: MessageElement
    ) = this.apply { list.add(Message(id, forward).apply { children.addAll(elements) }) }

    fun quote(element: Quote) = this.apply { list.add(element) }
    fun quote(vararg elements: MessageElement) = this.apply { list.add(Quote().apply { children.addAll(elements) }) }

    fun author(element: Author) = this.apply { list.add(element) }

    @JvmOverloads
    fun author(
        id: String? = null,
        name: String? = null,
        avatar: String? = null
    ) = this.apply { list.add(Author(id, name, avatar)) }

    fun button(element: Button) = this.apply { list.add(element) }

    @JvmOverloads
    fun button(
        id: String? = null,
        type: String? = null,
        href: String? = null,
        text: String? = null,
        theme: String? = null
    ) = this.apply { list.add(Button(id, type, href, text, theme)) }

    fun build() = list.joinToString("") { it.toString() }
    override fun toString() = "MessageChainBuilder(list=$list)"

    companion object {
        @JvmStatic
        fun of() = MessageChainBuilder()
    }
}