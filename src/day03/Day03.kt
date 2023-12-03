package day03

import java.io.File
import kotlin.collections.ArrayList

const val dir = "src/day03/day03"

data class SchematicPlan(val partNumbers: List<PartNumber>, val symbols: List<Symbol>)

data class PartNumber(val x: Int, val y: Int, var length: Int, var value: Int) {
    fun getNeighbours(): List<Pair<Int, Int>> {
        val neighbours = ArrayList<Pair<Int, Int>>()
        //left/right
        neighbours.add(Pair(x - 1, y))
        neighbours.add(Pair(x + length, y))
        //up / down
        for (xDiff in -1..length) {
            neighbours.add(Pair(x + xDiff, y - 1))
            neighbours.add(Pair(x + xDiff, y + 1))
        }
        return neighbours
    }
}

data class Symbol(val x: Int, val y: Int, val value: String) {
    fun getNeighbours(): List<Pair<Int, Int>> {
        val neighbours = ArrayList<Pair<Int, Int>>()
        //left/right
        neighbours.add(Pair(x - 1, y))
        neighbours.add(Pair(x + 1, y))
        //up / down
        for (xDiff in -1..1) {
            neighbours.add(Pair(x + xDiff, y - 1))
            neighbours.add(Pair(x + xDiff, y + 1))
        }
        return neighbours
    }
}

fun parseSchematic(lines: List<String>): SchematicPlan {
    val partNumbers = ArrayList<PartNumber>()
    val symbols = ArrayList<Symbol>()
    lines.forEachIndexed { y, line ->
        var x = 0
        var currentPartNumber: PartNumber? = null
        for (char in line) {
            when (char) {
                '.' -> {
                    currentPartNumber = null
                }

                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    if (currentPartNumber == null) {
                        currentPartNumber = PartNumber(x, y, 1, char.toString().toInt())
                        partNumbers.add(currentPartNumber)
                    } else {
                        currentPartNumber.length++
                        currentPartNumber.value *= 10
                        currentPartNumber.value += char.toString().toInt()
                    }
                }

                else -> {
                    currentPartNumber = null
                    symbols.add(Symbol(x, y, char.toString()))
                }
            }
            x++
        }
    }
    return SchematicPlan(partNumbers, symbols)
}

fun task1(lines: List<String>): Int {
    val (parts, symbols) = parseSchematic(lines)
    val symbolMap = HashMap<Int, HashMap<Int, Symbol>>()
    for (symbol in symbols) {
        val yMap = symbolMap.getOrDefault(symbol.y, HashMap())
        yMap[symbol.x] = symbol
        symbolMap[symbol.y] = yMap
    }
    return parts.filter { part ->
        part.getNeighbours().mapNotNull { (x, y) ->
            symbolMap[y]?.get(x)
        }.isNotEmpty()
    }.sumOf { part -> part.value }
}

fun task2(lines: List<String>): Int {
    val (parts, symbols) = parseSchematic(lines)
    val partMap = HashMap<Int, HashMap<Int, PartNumber>>()
    for (partNumber in parts) {
        val yMap = partMap.getOrDefault(partNumber.y, HashMap())
        for (xDiff in 0..<partNumber.length) {
            yMap[partNumber.x + xDiff] = partNumber
        }
        partMap[partNumber.y] = yMap
    }

    return symbols.filter { symbol -> symbol.value == "*" }.sumOf { symbol ->
        val partNumbers = symbol.getNeighbours().mapNotNull { (x, y) ->
            partMap[y]?.get(x)
        }.distinct()
        if ( partNumbers.size == 2){
            partNumbers[0].value * partNumbers[1].value
        }else{
            0
        }
    }
}

fun main() {
    println(task1(File("${dir}_test.txt").readLines()))
    println(task1(File("${dir}.txt").readLines()))


    println(task2(File("${dir}_test.txt").readLines()))
    println(task2(File("${dir}.txt").readLines()))
}