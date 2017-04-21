@file:Suppress("unused")

package com.yoavst.euler.problems

import com.yoavst.euler.isPrime
import com.yoavst.euler.resourceOf
import com.yoavst.euler.swap

/**
 * We shall say that an n-digit number is pandigital if it makes use of all the digits 1 to n exactly once.
 * For example, 2143 is a 4-digit pandigital and is also prime.
 * What is the largest n-digit pandigital prime that exists?
 */
fun problem41() {
    val numbers = mutableSetOf<Int>()
    fun generateAll(data: CharArray, left: Int = 0) {
        if (left == data.size) {
            val num = String(data).toInt()
            if (num.isPrime()) {
                numbers += num
            }
        } else {
            for (i in left until data.size) {
                data.swap(i, left)
                generateAll(data, left + 1)
                data.swap(i, left)
            }
        }
    }

    for (i in 9 downTo 4) {
        generateAll((1..i).joinToString(separator = "").toCharArray())
        if (numbers.isNotEmpty()) {
            println(numbers.max())
            break
        }
    }
}

/**
 * The nth term of the sequence of triangle numbers is given by, tn = 0.5n(n+1); so the first ten triangle numbers are:
 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
 *
 * By converting each letter in a word to a number corresponding to its alphabetical position and adding these values we form a word value.
 * For example, the word value for SKY is 19 + 11 + 25 = 55 = t10.
 * If the word value is a triangle number then we shall call the word a triangle word.
 * Using words.txt, a 16K text file containing nearly two-thousand common English words,
 * how many are triangle words?


 */
fun problem42() {
    val triangles = (1..30).map { n -> n * (n + 1) / 2 }.toIntArray()
    val count = resourceOf("p042_words.txt").readText().split(',').asSequence().map { it.substring(1, it.length - 1) }
            .map { it.sumBy { it - 'A' + 1 } }.count { triangles.binarySearch(it) >= 0 }

    println(count)
}