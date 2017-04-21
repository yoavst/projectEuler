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

fun String.isPandigital(n: Int): Boolean = length == n && '0' !in this && ('1'..('1' + (n-1))).all { it in this }

