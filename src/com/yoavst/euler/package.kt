@file:Suppress("LoopToCallChain")

package com.yoavst.euler

import java.io.File

fun resourceOf(name: String): File = File("resources/$name")

fun Char.toIntValue() = this - '0'

fun String.splitByLength(length: Int): List<String> {
    return split("(?<=\\G.{$length})".toRegex(), this.length / length)
}

fun CharArray.swap(i1: Int, i2: Int) {
    val temp = this[i1]
    this[i1] = this[i2]
    this[i2] = temp
}

fun String.isPalindrome() = this == reversed()

fun String.isPandigital(n: Int): Boolean = length == n && '0' !in this && ('1'..('1' + (n - 1))).all { it in this }
fun String.isFullPandigital(): Boolean = length == 10 && ('0'..'9').all { it in this }

inline fun firstInt(from: Int = 0, step: Int = 1, func: (Int) -> Boolean): Int {
    @Suppress("NAME_SHADOWING")
    var from = from
    while (!func(from)) from += step
    return from
}

inline fun firstLong(from: Long = 0, step: Int = 1, func: (Long) -> Boolean): Long {
    @Suppress("NAME_SHADOWING")
    var from = from
    while (!func(from)) from += step
    return from
}

@Suppress("NAME_SHADOWING")
fun generateLongArray(length: Int, seed: Long, func: (seed: Long) -> Long): LongArray {
    val array = LongArray(length)
    var seed = seed
    for (i in array.indices) {
        seed = func(seed)
        array[i] = seed
    }
    return array
}

@Suppress("NAME_SHADOWING")
fun generateIntArray(length: Int, seed: Int, func: (seed: Int) -> Int): IntArray {
    val array = IntArray(length)
    var seed = seed
    for (i in array.indices) {
        seed = func(seed)
        array[i] = seed
    }
    return array
}

