package com.yoavst.euler

import kotlin.coroutines.experimental.buildSequence

fun Long.abs(): Long = Math.abs(this)
fun Long.sqrt(): Long = Math.sqrt(toDouble()).toLong()
fun Long.square(): Long = this * this
fun Long.pow(num: Long): Long {
    var n = num
    var sum = 1L
    while (n > 0) {
        sum *= this
        n--
    }
    return sum
}

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

private val FactorialCache = generateLongArray(20, 1) { a, b ->
    if (a == 0) 1 else a * b
}

fun Long.factorial(): Long {
    return if (this < FactorialCache.size)
        FactorialCache[this.toInt()]
    else
        factorialNoCacheFrom(FactorialCache.lastIndex + 1, FactorialCache.last())
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

@Suppress("LoopToCallChain")
fun Long.divisorsCount(): Int {
    var nod = 0
    val sqrt = this.sqrt()

    for (i in 1..sqrt) {
        if (this % i == 0L) {
            nod += 2
        }
    }

    if (sqrt * sqrt == this) {
        nod--
    }

    return nod
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

fun CharArray.toLong(): Long {
    var multiplier = 1
    var sum = 0L
    for (i in (size - 1) downTo 0) {
        sum += multiplier * this[i].toIntValue()
        multiplier *= 10
    }
    return sum
}


//region Int support
fun Int.abs(): Int = Math.abs(this)

fun Int.sqrt() = toLong().sqrt().toInt()
fun Int.square() = toLong().square().toInt()
fun Int.pow(num: Int) = toLong().pow(num.toLong()).toInt()
fun Int.reversed() = toLong().reversed().toInt()
fun Int.factorial() = toLong().factorial().toInt()

fun Int.isEven() = toLong().isEven()
fun Int.isOdd() = !isEven()
fun Int.isPalindrome() = toLong().isPalindrome()
fun Int.isPandigital(n: Int) = toLong().isPandigital(n)
fun Int.isPermutationOf(num: Int) = toLong().isPermutationOf(num.toLong())

fun Int.divisors() = toLong().divisors().map(Long::toInt)
fun Int.divisorsCount() = toLong().divisorsCount()
fun Int.digits() = toLong().digits()
fun Int.primeFactors() = toLong().primeFactors().map(Long::toInt)

@JvmName("multipleInt")
fun Iterable<Int>.multiple() = fold(1L, Long::times)

@JvmName("multipleInt")
fun Sequence<Int>.multiple() = fold(1L, Long::times)

fun CharArray.toInt() = toLong().toInt()
//endregion
