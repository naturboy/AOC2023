package day04

import java.io.File
import kotlin.math.pow

const val dir = "src/day04/day04"

data class Card(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {

    fun countWinningNumbers(): Int{
        return numbers.count { winningNumbers.contains(it) }
    }
    fun getPoints(): Int {
        return 2.0.pow(countWinningNumbers()- 1.0).toInt()
    }
}

fun parseInput(lines: List<String>): List<Card> {
    return lines.map { line ->
        val (cardStr, allNumbersStr) = line.split(": ")
        val cardId = cardStr.replace("Card ", "").trim().toInt()
        val (winningNumbersStr, numbersStr) = allNumbersStr.split(" | ")
        Card(
                cardId,
                winningNumbersStr.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() },
                numbersStr.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }
        )
    }
}

fun task1(lines: List<String>): Int {
    val cards = parseInput(lines)
    return cards.sumOf { card -> card.getPoints() }
}

fun task2(lines: List<String>): Int {
    val cards = parseInput(lines).toMutableList()
    val cardCountById = cards.associate { it.id to 1 }.toMutableMap()
    cards.forEach { card ->
        val winningCardIds = (1..card.countWinningNumbers()).map { it + card.id }
        val count = cardCountById[card.id]!!
        winningCardIds.forEach { cardId ->
            if (cardCountById.contains(cardId)){
                cardCountById[cardId] = cardCountById[cardId]!! + count
            }
        }
    }
    return cardCountById.values.sum()
}

fun main() {
    println(task1(File("${dir}_test.txt").readLines()))
    println(task1(File("${dir}.txt").readLines()))


    println(task2(File("${dir}_test2.txt").readLines()))
    println(task2(File("${dir}.txt").readLines()))
}