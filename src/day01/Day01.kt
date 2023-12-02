package day01

import java.io.File

const val dir = "src/day01/day01"

data class DigitPos(val digit : Int, val pos: Int)

fun task1(lines: List<String>, additionalMatchers: Map<String, Int> = emptyMap()): Int {
    val matchers : Map<String, Int> = (1..9).associateBy { "$it" } + additionalMatchers
    return lines.sumOf { line ->
        val firstDigitText = line.findAnyOf(matchers.keys)?.second
        val lastDigitText = line.findLastAnyOf(matchers.keys)?.second
        matchers[firstDigitText]!! * 10 + matchers[lastDigitText]!!
    }
}

fun task2(lines: List<String>): Int {
    val digitsAsText = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
    )
    return task1(lines, digitsAsText)
}

fun main() {
    println(task1(File("${dir}_test.txt").readLines()))
    println(task1(File("${dir}.txt").readLines()))

    println(task2(File("${dir}_test2.txt").readLines()))
    println(task2(File("${dir}.txt").readLines()))
}