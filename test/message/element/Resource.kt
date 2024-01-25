package com.github.nyayurn.yutori.message.element

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ResourceTest {
    @Test
    fun image() {
        Assertions.assertEquals(
            "<img src=\"image\" cache timeout=\"60000\" width=1920 height=1080/>",
            Image("image", true, "60000", 1920L, 1080L).toString()
        )
        Assertions.assertEquals(
            "<img src=\"&quot;&amp;&lt;&gt;\" cache timeout=\"60000\" width=1920 height=1080/>",
            Image("\"&<>", true, "60000", 1920L, 1080L).toString()
        )
    }

    @Test
    fun audio() {
        Assertions.assertEquals(
            "<audio src=\"audio\" cache timeout=\"60000\"/>",
            Audio("audio", true, "60000").toString()
        )
        Assertions.assertEquals(
            "<audio src=\"&quot;&amp;&lt;&gt;\" cache timeout=\"60000\"/>",
            Audio("\"&<>", true, "60000").toString()
        )
    }

    @Test
    fun file() {
        Assertions.assertEquals(
            "<file src=\"file\" cache timeout=\"60000\"/>",
            File("file", true, "60000").toString()
        )
        Assertions.assertEquals(
            "<file src=\"&quot;&amp;&lt;&gt;\" cache timeout=\"60000\"/>",
            File("\"&<>", true, "60000").toString()
        )
    }

    @Test
    fun video() {
        Assertions.assertEquals(
            "<video src=\"video\" cache timeout=\"60000\"/>",
            Video("video", true, "60000").toString()
        )
        Assertions.assertEquals(
            "<video src=\"&quot;&amp;&lt;&gt;\" cache timeout=\"60000\"/>",
            Video("\"&<>", true, "60000").toString()
        )
    }
}