@file:Suppress("unused")

package com.yoavst.euler.problems

import com.yoavst.euler.*
import java.math.BigInteger
import kotlin.coroutines.experimental.buildSequence

/**
 * Let d(n) be defined as the sum of proper divisors of n (numbers less than n which divide evenly into n).
 * If d(a) = b and d(b) = a, where a ≠ b, then a and b are an amicable pair and each of a and b are called amicable numbers.
 * For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284.
 * The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.
 * Evaluate the sum of all the amicable numbers under 10000.
 */
fun problem21() {
    val divisorsSum = (2..10000).map { it.toLong().divisors().sum().toInt() - it }.toIntArray()

    var sum = 0
    for (num in 2..10000) {
        val value = divisorsSum[num - 2]
        if (value == 0 || value == num)
            continue

        val otherValue = divisorsSum.getOrElse(value - 2) { 0 }
        if (otherValue == num) {
            sum += value + otherValue
        }
    }
    sum /= 2

    println(sum)
}

/**
 * Using names.txt, a 46K text file containing over five-thousand first names, begin by sorting it into alphabetical order.
 * Then working out the alphabetical value for each name, multiply this value by its alphabetical position in the list to obtain a name score.
 * For example, when the list is sorted into alphabetical order, COLIN,
 * which is worth 3 + 15 + 12 + 9 + 14 = 53, is the 938th name in the list. So, COLIN would obtain a score of 938 × 53 = 49714.
 * What is the total of all the name scores in the file?
 */
fun problem22() {
    val sum = resourceOf("p022_names.txt").readText().split(',').asSequence().sorted().map { it.substring(1, it.length - 1) }.foldIndexed(0) { index, total, item ->
        total + (index + 1) * item.sumBy { it - 'A' + 1 }
    }

    println(sum)
}


/**
 * A perfect number is a number for which the sum of its proper divisors is exactly equal to the number.
 * For example, the sum of the proper divisors of 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
 * A number n is called deficient if the sum of its proper divisors is less than n and it is called abundant if this sum exceeds n.
 * As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16,
 * the smallest number that can be written as the sum of two abundant numbers is 24.
 * By mathematical analysis, it can be shown that all integers greater than 28123 can be written as the sum of two abundant numbers.
 * However, this upper limit cannot be reduced any further by analysis even though it is known that
 * the greatest number that cannot be expressed as the sum of two abundant numbers is less than this limit.
 * Find the sum of all the positive integers which cannot be written as the sum of two abundant numbers.
 */
@Suppress("LoopToCallChain")
fun problem23() {
    val abundantNumbers = (12..28123).filter { it.toLong().divisors().sum() - it > it }.toIntArray()

    val limit = 28123
    val numbers = BooleanArray(limit + 1)
    for (m in abundantNumbers) {
        for (n in abundantNumbers) {
            val result = m + n
            if (result > limit)
                break
            numbers[result] = true
        }
    }

    val sum = numbers.foldIndexed(0L) { index, total, isSumOf -> total + (index.takeUnless { isSumOf } ?: 0) }
    println(sum)
}

/**
 * A permutation is an ordered arrangement of objects. For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4.
 * If all of the permutations are listed numerically or alphabetically, we call it lexicographic order.
 * The lexicographic permutations of 0, 1 and 2 are:
 *        012   021   102   120   201   210
 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?
 */
fun problem24V1() {
    fun generateAll(data: String): Sequence<String> {
        if (data.length == 1) return sequenceOf(data)
        var all = emptySequence<String>()
        for ((index, item) in data.withIndex()) {
            val newArray = data.substring(0, index) + data.substring(index + 1)
            val str = item.toString()
            all += generateAll(newArray).map { str + it }
        }
        return all
    }

    val all = generateAll("0123456789").sorted()
    val item = all.elementAt(999_999)
    println(item)
}

/**
 * A permutation is an ordered arrangement of objects. For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4.
 * If all of the permutations are listed numerically or alphabetically, we call it lexicographic order.
 * The lexicographic permutations of 0, 1 and 2 are:
 *        012   021   102   120   201   210
 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?
 */
fun problem24V2() {
    var leftover = 1_000_000
    var availableChars = "0123456789"
    val result = CharArray(10)
    while (availableChars.isNotEmpty()) {
        val factorial = (availableChars.length - 1).factorial()
        for (char in availableChars) {
            if (leftover - factorial > 0)
                leftover -= factorial
            else {
                result[result.size - availableChars.length] = char
                availableChars = availableChars.replace("$char", "")
                break
            }
        }
    }

    print(result)
}

/**
 * The Fibonacci sequence is defined by the recurrence relation:
 * Fn = Fn−1 + Fn−2, where F1 = 1 and F2 = 1.
 * Hence the first 12 terms will be:
 *
 * F1 = 1
 * F2 = 1
 * F3 = 2
 * F4 = 3
 * F5 = 5
 * F6 = 8
 * F7 = 13
 * F8 = 21
 * F9 = 34
 * F10 = 55
 * F11 = 89
 * F12 = 144
 * The 12th term, F12, is the first term to contain three digits.
 *
 * What is the index of the first term in the Fibonacci sequence to contain 1000 digits?
 */
fun problem25() {
    val MinNumber = BigInteger("10").pow(999)
    val fibo: Sequence<BigInteger> = buildSequence {
        var f1 = BigInteger.ZERO
        var f2 = BigInteger.ONE
        while (true) {
            yield(f2)
            val temp = f2
            f2 += f1
            f1 = temp
        }
    }
    val index = fibo.indexOfFirst { it >= MinNumber } + 1
    println(index)
}

/**
 * Euler discovered the remarkable quadratic formula:
 * n^2+n+41
 * It turns out that the formula will produce 40 primes for the consecutive integer values 0≤n≤39
 * However, when n=40,40^2+40+41=40(40+1)+41 is divisible by 41,
 * and certainly when n=41,41^2+41+41 is clearly divisible by 41.
 * The incredible formula n^2−79n+1601 was discovered, which produces 80 primes for the consecutive values 0≤n≤790≤n≤79.
 * The product of the coefficients, −79 and 1601, is −126479.
 * Considering quadratics of the form:
 *
 * n2+an+b, where |a|<100 and |b|≤1000
 * where |n| is the modulus/absolute value of nn
 * e.g. |11|=11 and |−4|=4
 * Find the product of the coefficients,
 */
fun problem27() {
    val possibleAs = (-1000 until 1000)
    val possibleBs = (0..1000).filter(Int::isPrime).flatMap { listOf(it, -it) }.toIntArray()

    var max = 0
    var maxAB = 0
    for (a in possibleAs) {
        for (b in possibleBs) {
            var n = 1
            while ((n.square() + a * n + b).isPrime()) n++
            if (n > max) {
                max = n
                maxAB = a * b
                println("${a}n^2 + ${a}n + $b :: $n")
            }
        }
    }

    println(maxAB)
}

/**
 * Starting with the number 1 and moving to the right in a clockwise direction a 5 by 5 spiral is formed as follows:
 *
 *     21 22 23 24 25
 *     20  7  8  9 10
 *     19  6  1  2 11
 *     18  5  4  3 12
 *     17 16 15 14 13
 *
 * It can be verified that the sum of the numbers on the diagonals is 101.
 * What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral formed in the same way?
 */
fun problem28() {
    val n = 501
    val toRightTopSum = n * (2 * n - 1) * (2 * n + 1) / 3 - 1
    val toRightBottomSum = n * (4 * n.square() - 9 * n + 8) / 3 - 1
    val toLeftTopSum = n * (4 * n.square() - 3 * n + 2) / 3 - 1
    val toLeftBottomSum = n * (4 * n.square() - 6 * n + 5) / 3 - 1
    val totalSum = toRightTopSum + toRightBottomSum + toLeftTopSum + toLeftBottomSum + 1

    println(totalSum)
}

/**
 * Consider all integer combinations of a^b for 2 ≤ a ≤ 5 and 2 ≤ b ≤ 5:
 *
 *    2^2=4, 2^3=8, 2^4=16, 2^5=32
 *    3^2=9, 3^3=27, 3^4=81, 3^5=243
 *    4^2=16, 4^3=64, 4^4=256, 4^5=1024
 *    5^2=25, 5^3=125, 5^4=625, 5^5=3125
 *
 * If they are then placed in numerical order, with any repeats removed, we get the following sequence of 15 distinct terms:
 *           4, 8, 9, 16, 25, 27, 32, 64, 81, 125, 243, 256, 625, 1024, 3125

How many distinct terms are in the sequence generated by ab for 2 ≤ a ≤ 100 and 2 ≤ b ≤ 100?
 */
fun problem29() {
    val count = (2L..100).asSequence().flatMap { a -> (2L..100).asSequence().map { b -> Math.pow(a.toDouble(), b.toDouble()) } }.distinct().count()

    println(count)
}

/**
 * Surprisingly there are only three numbers that can be written as the sum of fourth powers of their digits:
 * 1634 = 1^4 + 6^4 + 3^4 + 4^4
 * 8208 = 8^4 + 2^4 + 0^4 + 8^4
 * 9474 = 9^4 + 4^4 + 7^4 + 4^4
 * As 1 = 1^4 is not a sum it is not included.
 *
 * The sum of these numbers is 1634 + 8208 + 9474 = 19316.
 * Find the sum of all the numbers that can be written as the sum of fifth powers of their digits.
 */
fun problem30() {
    val sum = (10..1_000_000).filter { it.digits().sumBy { digit -> digit.pow(5) } == it }.sum()

    println(sum)
}