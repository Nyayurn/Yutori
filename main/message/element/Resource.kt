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
 * 资源元素
 * @property src 资源的 URL
 * @property cache 是否使用已缓存的文件
 * @property timeout 下载文件的最长时间 (毫秒)
 */
abstract class ResourceElement(
    name: String,
    src: String,
    cache: Boolean?,
    timeout: String?
) : NodeMessageElement(name) {
    var src: String by super.properties
    var cache: Boolean? by super.properties
    var timeout: String? by super.properties

    init {
        this.src = src
        this.cache = cache
        this.timeout = timeout
    }
}

/**
 * 图片
 * @property width 图片的宽度
 * @property height 图片的高度
 */
class Image @JvmOverloads constructor(
    src: String,
    cache: Boolean? = null,
    timeout: String? = null,
    width: Number? = null,
    height: Number? = null
) : ResourceElement("img", src, cache, timeout) {
    var width: Number? by super.properties
    var height: Number? by super.properties

    init {
        this.width = width
        this.height = height
    }
}

/**
 * 语音
 */
class Audio @JvmOverloads constructor(
    src: String,
    cache: Boolean? = null,
    timeout: String? = null
) : ResourceElement("audio", src, cache, timeout)

/**
 * 视频
 */
class Video @JvmOverloads constructor(
    src: String,
    cache: Boolean? = null,
    timeout: String? = null
) : ResourceElement("video", src, cache, timeout)

/**
 * 文件
 */
class File @JvmOverloads constructor(
    src: String,
    cache: Boolean? = null,
    timeout: String? = null
) : ResourceElement("file", src, cache, timeout)