@file:Suppress("unused")

package com.yoavst.euler.problems

import com.yoavst.euler.firstInt
import com.yoavst.euler.isPrime
import com.yoavst.euler.primesSequence
import java.math.BigInteger

/**
 * By replacing the 1st digit of the 2-digit number *3, it turns out that six of the nine possible values: 13, 23, 43, 53, 73, and 83, are all prime.
 * By replacing the 3rd and 4th digits of 56**3 with the same digit,
 * this 5-digit number is the first example having seven primes among the ten generated numbers,
 * yielding the family: 56003, 56113, 56333, 56443, 56663, 56773, and 56993. Consequently 56003, being the first member of this family,
 * is the smallest prime with this property.
 *
 * Find the smallest prime which, by replacing part of the number (not necessarily adjacent digits) with the same digit,
 * is part of an eight prime value family.
 */
@Suppress("LoopToCallChain")
fun problem51() {
    fun Int.digitsCount(digit: Int): Int {
        var num = this
        var count = 0
        while (num != 0) {
            if (num % 10 == digit) count++
            num /= 10
        }
        return count
    }

    fun Int.check(digit: Char): Boolean {
        val str = toString()
        val num = str.replace(digit, '0').toInt().toString().replace('0', '9').toInt()
        return if (num < this)
            ('1'..'9').count { replaced -> str.replace(digit, replaced).toInt().isPrime() } >= 8
        else
            ('0'..'9').count { replaced -> str.replace(digit, replaced).toInt().isPrime() } >= 8
    }

    val number = primesSequence.map(Long::toInt).filter { it > 100_000 }.first {
        (it.digitsCount(0) >= 3 && it.check('0')) || (it.digitsCount(1) >= 3 && it % 10 != 1 && it.check('1')) ||
                (it.digitsCount(2) >= 3 && it.check('2'))
    }

    println(number)
}

/**
 * It can be seen that the number, 125874, and its double, 251748, contain exactly the same digits, but in a different order.
 *
 * Find the smallest positive integer, x, such that 2x, 3x, 4x, 5x, and 6x, contain the same digits.
 */
fun problem52() {
    val num = firstInt(1) {
        val arr = it.toString().toCharArray()
        arr.sort()
        for (i in 2..6) {
            val arr2 = (it * i).toString().toCharArray()
            if (arr2.size != arr.size)
                return@firstInt false
            arr2.sort()
            if (!(arr contentEquals arr2)) return@firstInt false
        }
        true
    }
    println(num)
}

fun problem53() {
    val results = arrayOfNulls<BigInteger>(101)
    for (i in 0..100) {
        if (i == 0)
            results[0] = BigInteger.ONE
        else
            results[i] = results[i - 1]!! * BigInteger("$i")
    }
    val min = BigInteger("1000000")
    var count = 0
    for (n in 23..100) {
        for (r in 0 until n) {
            if ((results[n]!! / (results[r]!! * results[n - r]!!)) > min)
                count++
        }
    }
    println(count)
}