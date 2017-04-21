package com.yoavst.euler

import kotlin.coroutines.experimental.buildSequence

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

//region isPrime
private fun Long.isPrimeNoCache(): Boolean {
    if (this <= 1) return false
    else if (this == 2L) return true
    else if (this % 2 == 0L) return false

    return (3..sqrt() step 2).none { this % it == 0L }
}

private const val MaxPrimeCacheValue = 2_000_000
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

fun Iterable<Long>.multiple() = fold(1L, Long::times)
fun Sequence<Long>.multiple() = fold(1L, Long::times)


//region Int support
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

fun Int.divisors() = toLong().divisors().map(Long::toInt)
fun Int.digits() = toLong().digits()

@JvmName("multipleInt")
fun Iterable<Int>.multiple() = fold(1L, Long::times)

@JvmName("multipleInt")
fun Sequence<Int>.multiple() = fold(1L, Long::times)
//endregion
