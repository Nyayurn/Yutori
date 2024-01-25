package com.github.nyayurn.yutori.message.element

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BasicTest {
    @Test
    fun text() {
        Assertions.assertEquals(
            "&quot;&amp;&lt;&gt;",
            Text("\"&<>").toString()
        )
    }

    @Test
    fun at() {
        Assertions.assertEquals(
            "<at id=\"114514\" name=\"田所浩二\" role=\"admin\" type=\"all\"/>",
            At("114514", "田所浩二", "admin", "all").toString()
        )
        Assertions.assertEquals(
            "<at id=\"114514\" name=\"&quot;&amp;&lt;&gt;\" role=\"admin\" type=\"all\"/>",
            At("114514", "\"&<>", "admin", "all").toString()
        )
    }

    @Test
    fun href() {
        Assertions.assertEquals(
            "<a href=\"https://www.baidu.com\"/>",
            Href("https://www.baidu.com").toString()
        )
        Assertions.assertEquals(
            "<a href=\"https://www.baidu.com/&quot;&amp;&lt;&gt;\"/>",
            Href("https://www.baidu.com/\"&<>").toString()
        )
    }

    @Test
    fun sharp() {
        Assertions.assertEquals(
            "<sharp id=\"1919810\" name=\"大粪交流群\"/>",
            Sharp("1919810", "大粪交流群").toString()
        )
        Assertions.assertEquals(
            "<sharp id=\"1919810\" name=\"&quot;&amp;&lt;&gt;\"/>",
            Sharp("1919810", "\"&<>").toString()
        )
    }
}