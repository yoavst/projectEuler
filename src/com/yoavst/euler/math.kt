package com.yoavst.euler

import kotlin.coroutines.experimental.buildSequence

fun Long.abs(): Long = Math.abs(this)
fun Long.sqrt(): Long = Math.sqrt(toDouble()).toLong()
fun Long.square(): Long = this * this
fun Long.pow(num: Long): Long = Math.pow(this.toDouble(), num.toDouble()).toLong()
fun Long.reversed(): Long {
    var result = 0L
    var num = this
    while (num != 0L) {
        result *= 10
        result += num % 10
        num /= 10
    }
    return result
}

//region Factorial
private fun Long.factorialNoCache() = factorialNoCacheFrom(1, 1)

private fun Long.factorialNoCacheFrom(x: Int, result: Long) = (x..this).fold(result, Long::times)

private val FactorialCache = LongArray(20) { it.toLong().factorialNoCache() }
fun Long.factorial(): Long {
    if (this < FactorialCache.size)
        return FactorialCache[this.toInt()]
    else
        return factorialNoCacheFrom(FactorialCache.lastIndex + 1, FactorialCache.last())
}
//endregion

fun Long.isEven() = this % 2 == 0L
fun Long.isOdd() = !isEven()
fun Long.isPalindrome() = this == reversed()
fun Long.isPandigital(n: Int) = toString().isPandigital(n)
fun Long.isPermutationOf(num: Long): Boolean {
    if (this == num) return true
    val arr1 = num.toString().toCharArray()
    arr1.sort()
    val arr2 = toString().toCharArray()
    arr2.sort()
    return arr1 contentEquals arr2
}

//region isPrime
private fun Long.isPrimeNoCache(): Boolean {
    if (this <= 1) return false
    else if (this == 2L) return true
    else if (this % 2 == 0L) return false

    return (3..sqrt() step 2).none { this % it == 0L }
}

private const val MaxPrimeCacheValue = 2_000_000L
private val PrimeCache = (sequenceOf(2L) + (3L..MaxPrimeCacheValue step 2).asSequence()).filter(Long::isPrimeNoCache).toList().toLongArray()

fun Long.isPrime(): Boolean {
    if (this < MaxPrimeCacheValue)
        return PrimeCache.binarySearch(this) >= 0
    else
        return isPrimeNoCache()
}
//endregion

fun Long.divisors(): Sequence<Long> {
    if (this <= 1) return sequenceOf(1)
    else if (this == 2L) return sequenceOf(1, 2)
    return buildSequence {
        val num = this@divisors
        yield(1L)
        yield(num)
        @Suppress("LoopToCallChain")
        for (i in 2..(num / 2)) {
            if (num % i == 0L)
                yield(i)
        }
    }
}

fun Long.digits(): Sequence<Int> {
    if (this <= 10) return sequenceOf(this.toInt())
    return buildSequence {
        var num = this@digits
        while (num != 0L) {
            yield((num % 10).toInt())
            num /= 10
        }
    }
}

fun Long.primeFactors(): Sequence<Long> {
    if (this == 1L) return emptySequence()
    else if (isPrime()) return sequenceOf(this)

    return buildSequence {
        var num = this@primeFactors
        for (prime in primesSequence) {
            if (prime > num) break
            while (num % prime == 0L) {
                yield(prime)
                num /= prime
            }
            if (num == 1L) break
        }
    }
}

fun Iterable<Long>.multiple() = fold(1L, Long::times)
fun Sequence<Long>.multiple() = fold(1L, Long::times)


//region Int support
fun Int.abs(): Int = Math.abs(this)

fun Int.sqrt() = toLong().sqrt().toInt()
fun Int.square() = toLong().square().toInt()
fun Int.pow(num: Int) = toLong().pow(num.toLong()).toInt()
fun Int.reversed() = toLong().reversed().toInt()
fun Int.factorial() = toLong().factorial().toInt()

fun Int.isEven() = toLong().isEven()
fun Int.isOdd() = !isEven()
fun Int.isPrime() = toLong().isPrime()
fun Int.isPalindrome() = toLong().isPalindrome()
fun Int.isPandigital(n: Int) = toLong().isPandigital(n)
fun Int.isPermutationOf(num: Int) = toLong().isPermutationOf(num.toLong())

fun Int.divisors() = toLong().divisors().map(Long::toInt)
fun Int.digits() = toLong().digits()
fun Int.primeFactors() = toLong().primeFactors().map(Long::toInt)

@JvmName("multipleInt")
fun Iterable<Int>.multiple() = fold(1L, Long::times)

@JvmName("multipleInt")
fun Sequence<Int>.multiple() = fold(1L, Long::times)
//endregion

val primesSequence = buildSequence<Long> {
    for (number in PrimeCache)
        yield(number)

    var num = MaxPrimeCacheValue
    if (num.isEven()) num += 1
    while (true) {
        if (num.isPrime())
            yield(num)
        num += 2
    }
}
