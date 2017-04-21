@file:Suppress("unused")

package com.yoavst.euler.problems

import com.yoavst.euler.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.coroutines.experimental.buildSequence

/**
 * In England the currency is made up of pound, £, and pence, p, and there are eight coins in general circulation:
 * 1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p).
 *
 * It is possible to make £2 in the following way:
 * 1×£1 + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p
 *
 * How many different ways can £2 be made using any number of coins?
 */
fun problem31() {
    val values = intArrayOf(200, 100, 50, 20, 10, 5, 2, 1)

    var count = 0

    fun generateWays(now: Int, coins: IntArray) {
        if (now == 200)
            count++
        if (now < 200 && !coins.isEmpty()) {
            val coin = coins[0]
            val newCoins = coins.copyOfRange(1, coins.size)

            generateWays(now, newCoins)
            var sum = now
            while (sum < 200) {
                sum += coin
                generateWays(sum, newCoins)
            }

        }
    }

    generateWays(0, values)
    println(count)
}

/**
 * We shall say that an n-digit number is pandigital if it makes use of all the digits 1 to n exactly once;
 * for example, the 5-digit number, 15234, is 1 through 5 pandigital.
 *
 * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing multiplicand, multiplier, and product is 1 through 9 pandigital.
 * Find the sum of all products whose multiplicand/multiplier/product identity can be written as a 1 through 9 pandigital.
 *
 * HINT: Some products can be obtained in more than one way so be sure to only include it once in your sum.
 */
fun problem32() {
    val numbers = mutableSetOf<Int>()
    for (a in 0..10000) {
        for (b in 0..10000) {
            val c = a * b
            val result = "$a*$b=$c"
            if (result.length == 11 && '0' !in result && result.toSet().size == 11) {
                numbers += c
            }
        }
    }

    val sum = numbers.sum()
    println(sum)
}

/**
 * The fraction 49/98 is a curious fraction, as an inexperienced mathematician in attempting to simplify
 * it may incorrectly believe that 49/98 = 4/8, which is correct, is obtained by cancelling the 9s.
 * We shall consider fractions like, 30/50 = 3/5, to be trivial examples.
 *
 * There are exactly four non-trivial examples of this type of fraction,
 * less than one in value, and containing two digits in the numerator and denominator.
 * If the product of these four fractions is given in its lowest common terms, find the value of the denominator.
 */
fun problem33() {
    val numbers = mutableSetOf<Pair<Int, Int>>()

    /**
     * a   c
     * _ = _  <=> ad = bc
     * b   d
     */
    fun isTheSame(a: Int, b: Int, c: Int, d: Int) = a * d == b * c

    for (a in 10..99) {
        for (b in 10..99) {
            if (a < b && (a % 10 != 0 || b % 10 != 0)) {
                val a1 = a / 10
                val a2 = a % 10
                val b1 = b / 10
                val b2 = b % 10

                if ((a1 == b1 && isTheSame(a, b, a2, b2)) || (a1 == b2 && isTheSame(a, b, a2, b1)) ||
                        (a2 == b1 && isTheSame(a, b, a1, b2)) || (a2 == b2 && isTheSame(a, b, a1, b1)))
                    numbers += a to b
            }
        }
    }

    val result = numbers.fold(BigDecimal.ONE) { sum, (a, b) -> sum * BigDecimal("$a").divide(BigDecimal("$b"), 3, RoundingMode.HALF_UP) }
    println(result)
}

/**
 * 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
 * Find the sum of all numbers which are equal to the sum of the factorial of their digits.
 * Note: as 1! = 1 and 2! = 2 are not sums they are not included.
 */
fun problem34() {
    val sum = (10..1_000_000).filter { it.digits().sumBy(Int::factorial) == it }.sum()

    println(sum)
}

/**
 * The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves prime.
 * There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.
 * How many circular primes are there below one million?
 */
fun problem35() {
    val count = (1..999_999).asSequence().map(Int::toString)
            .filter { num -> num.indices.all { i -> (num.substring(i) + num.substring(0, i)).toInt().isPrime() } }
            .count()

    println(count)
}

/**
 * The decimal number, 585 = 10010010012 (binary), is palindromic in both bases.
 * Find the sum of all numbers, less than one million, which are palindromic in base 10 and base 2.
 *
 * (Please note that the palindromic number, in either base, may not include leading zeros.)
 */
fun problem36() {
    val sum = (1..999_999).asSequence().filter(Int::isPalindrome)
            .map { it.toString(2) }.filter(String::isPalindrome)
            .map { it.toInt(2) }.sum()

    println(sum)
}

/**
 * The number 3797 has an interesting property. Being prime itself,
 * it is possible to continuously remove digits from left to right, and remain prime at each stage: 3797, 797, 97, and 7.
 * Similarly we can work from right to left: 3797, 379, 37, and 3.
 *
 * Find the sum of the only eleven primes that are both truncatable from left to right and right to left.
 * NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes.
 */
fun problem37() {
    fun Int.allPrime(): Boolean {
        var num = this
        while (num != 0) {
            if (!num.isPrime()) return false
            num /= 10
        }
        num = this
        while (num != 0) {
            if (!num.isPrime()) return false
            num = num.toString().substring(1).toIntOrNull() ?: 0
        }
        return true
    }

    3797.allPrime()

    val sum = (10..Int.MAX_VALUE).asSequence().filter(Int::allPrime).take(11).sum()
    println(sum)
}

/**
 * Take the number 192 and multiply it by each of 1, 2, and 3:
 * 192 × 1 = 192
 * 192 × 2 = 384
 * 192 × 3 = 576
 *
 * By concatenating each product we get the 1 to 9 pandigital, 192384576. We will call 192384576 the concatenated product of 192 and (1,2,3)
 * The same can be achieved by starting with 9 and multiplying by 1, 2, 3, 4, and 5, giving the pandigital, 918273645,
 * which is the concatenated product of 9 and (1,2,3,4,5).
 *
 * What is the largest 1 to 9 pandigital 9-digit number that can be formed as the concatenated product of an integer with (1,2, ... , n) where n > 1?
 */
fun problem38() {
    val max = (1..10000).asSequence().flatMap { num ->
        buildSequence {
            var n = 1
            var str = ""
            while (str.length < 9) {
                str += "${n * num}"
                n++
            }
            if (str.isPandigital(9))
                yield(str)
        }
    }.map(String::toLong).max()

    println(max)
}

/**
 * If p is the perimeter of a right angle triangle with integral length sides, {a,b,c}, there are exactly three solutions for p = 120.
 *
 * {20,48,52}, {24,45,51}, {30,40,50}
 * For which value of p ≤ 1000, is the number of solutions maximised?
 */
fun problem39() {
    val max = (1..1000).maxBy { p ->
        var count = 0
        for (a in 0 until p) {
            for (b in 0 until p - a) {
                val c = p - a - b
                if (c.square() == a.square() + b.square())
                    count++
            }
        }
        count
    }
    println(max)
}

/**
 * An irrational decimal fraction is created by concatenating the positive integers:
 *
 * 0.123456789101112131415161718192021...
 *
 * It can be seen that the 12th digit of the fractional part is 1.
 * If dn represents the nth digit of the fractional part, find the value of the following expression.
 * d1 × d10 × d100 × d1000 × d10000 × d100000 × d1000000
 */
fun problem40() {
    val fraction = (0..Int.MAX_VALUE).asSequence().map(Int::toString).flatMap(String::asSequence)

    val result = fraction.elementAt(1).toIntValue() * fraction.elementAt(100).toIntValue() *
            fraction.elementAt(1000).toIntValue() * fraction.elementAt(10000).toIntValue() *
            fraction.elementAt(100000).toIntValue() * fraction.elementAt(1000000).toIntValue()

    println(result)
}