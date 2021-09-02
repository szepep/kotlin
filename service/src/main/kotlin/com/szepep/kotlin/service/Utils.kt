package com.szepep.kotlin.service

import kotlin.random.Random

private val CHAR_POOL = (('0'..'9') + ('a'..'z') + ('A'..'Z')).toTypedArray()

/**
 * Generates random [String] with [length]. The result string contains [Char]s from
 * [charPool]. The default [charPool] contains alphanumeric chars.
 */
fun Random.nextString(length: Int, charPool: Array<Char> = CHAR_POOL): String =
    (1..length)
        .map { nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")