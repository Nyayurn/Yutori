package com.github.nyayurn.yutori.message.element

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DecoTest {
    @Test
    fun bold() {
        Assertions.assertEquals(
            "<b>胖的</b>",
            Bold("胖的").toString()
        )
        Assertions.assertEquals(
            "<strong>胖的它哥</strong>",
            Strong("胖的它哥").toString()
        )
    }

    @Test
    fun delete() {
        Assertions.assertEquals(
            "<del>中分线</del>",
            Delete("中分线").toString()
        )
        Assertions.assertEquals(
            "<s>删除线</s>",
            Strikethrough("删除线").toString()
        )
    }

    @Test
    fun italic() {
        Assertions.assertEquals(
            "<em>MJ Peek</em>",
            Em("MJ Peek").toString()
        )
        Assertions.assertEquals(
            "<i>Annie are you okay?</i>",
            Idiomatic("Annie are you okay?").toString()
        )
    }

    @Test
    fun underline() {
        Assertions.assertEquals(
            "<ins>我一个滑铲</ins>",
            Ins("我一个滑铲").toString()
        )
        Assertions.assertEquals(
            "<u>填空题</u>",
            Underline("填空题").toString()
        )
    }

    @Test
    fun code() {
        Assertions.assertEquals(
            "<code>&lt;C&gt;+C &lt;C&gt;+V</code>",
            Code("<C>+C <C>+V").toString()
        )
    }

    @Test
    fun spl() {
        Assertions.assertEquals(
            "<spl>透透你</spl>",
            Spl("透透你").toString()
        )
    }

    @Test
    fun su() {
        Assertions.assertEquals(
            "<sub>啥B</sub>",
            Sub("啥B").toString()
        )
        Assertions.assertEquals(
            "<sup>啥批</sup>",
            Sup("啥批").toString()
        )
    }
}