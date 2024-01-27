package com.github.nyayurn.qbot

import com.alibaba.fastjson2.annotation.JSONField

data class SparkResponse(
    val header: Header,
    val payload: Payload?
) {
    data class Header(
        val code: Int,
        val message: String,
        val sid: String,
        val status: Int
    )

    data class Payload(
        val choices: Choices,
        val usage: Usage?
    ) {
        data class Choices(
            val status: Int,
            val seq: Int,
            val text: List<TextData>
        ) {
            data class TextData(
                val content: String,
                val role: String,
                val index: Int
            )
        }

        data class Usage(val textData: TextData?) {
            data class TextData(
                @JSONField(name = "prompt_tokens") val promptTokens: Int,
                @JSONField(name = "completion_tokens") val completionTokens: Int,
                @JSONField(name = "total_tokens") val totalTokens: Int
            )
        }
    }
}