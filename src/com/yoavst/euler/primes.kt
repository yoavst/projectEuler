@file:Suppress("LoopToCallChain")

package com.yoavst.euler

import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.experimental.buildSequence

fun generatePrimes(upperLimit: Int): LongArray {
    val sieveBound = (upperLimit - 1) / 2
    val upperSqrt = (upperLimit.sqrt() - 1) / 2
    val primeBits = BitSet(sieveBound + 1)

    for (i in 1..upperSqrt) {
        if (!primeBits[i]) {
            var j = i * 2 * (i + 1)
            while (j <= sieveBound) {
                primeBits[j] = true
                j += 2 * i + 1
            }
        }
    }

    val numbers = ArrayList<Long>((upperLimit / (Math.log10(upperLimit.toDouble()) - 1.08366)).toInt())
    numbers += 2
    for (i in 1..sieveBound) {
        if (!primeBits[i]) {
            numbers += (2 * i + 1).toLong()
        }
    }

    return numbers.toLongArray()
}

private const val MaxPrimeCacheValue = 2_000_000L
private val PrimeCache by lazy { generatePrimes(MaxPrimeCacheValue.toInt()) }

private fun Long.isPrimeNoCache(): Boolean {
    if (this <= 1) return false
    else if (this == 2L) return true
    else if (this % 2 == 0L) return false

    return (3..sqrt() step 2).none { this % it == 0L }
}

fun Long.isPrime(): Boolean {
    if (this < MaxPrimeCacheValue)
        return PrimeCache.binarySearch(this) >= 0
    else
        return isPrimeNoCache()
}

fun Int.isPrime() = toLong().isPrime()

val primesSequence = buildSequence<Long> {
    PrimeCache.forEach { yield(it) }

    var num = MaxPrimeCacheValue
    if (num.isEven()) num += 1
    while (true) {
        if (num.isPrime())
            yield(num)
        num += 2
    }
}