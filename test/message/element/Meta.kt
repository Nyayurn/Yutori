package com.github.nyayurn.yutori.message.element

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MetaTest {
    @Test
    fun author() {
        Assertions.assertEquals(
            "<author id=\"2.5\" name=\"鸡王\" avatar=\"https://th.bing.com/th/id/OIP.0Ld_Qg_bBOkzJzphqBHWBAHaEK\"/>",
            Author("2.5", "鸡王", "https://th.bing.com/th/id/OIP.0Ld_Qg_bBOkzJzphqBHWBAHaEK").toString()
        )
        Assertions.assertEquals(
            "<author id=\"2.5\" name=\"&quot;&amp;&lt;&gt;\" avatar=\"&quot;&amp;&lt;&gt;\"/>",
            Author("2.5", "\"&<>", "\"&<>").toString()
        )
    }

    @Test
    fun quote() {
        Assertions.assertEquals(
            "<quote>引用</quote>",
            Quote().apply { this += Text("引用") }.toString()
        )
    }
}