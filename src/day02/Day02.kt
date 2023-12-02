package day02

import java.io.File
import kotlin.math.max

const val dir = "src/day02/day02"

enum class CubeColor(val text: String) {
    RED("red"), GREEN("green"), BLUE("blue")
}

data class GameHand(val cubes: Map<CubeColor, Int>)

data class Game(val id: Int, val hands: List<GameHand>)

fun parseGame(line: String): Game {
    val (gameString, handsString) = line.split(": ")
    val gameId = gameString.replace("Game ", "").toInt()
    val hands = handsString.split("; ").map { handLine ->
        GameHand(handLine.split(", ").map { cubeString ->
            val (amountStr, colorStr) = cubeString.split(" ")
            CubeColor.entries.find { it.text == colorStr }!! to amountStr.toInt()
        }.toMap())
    }
    return Game(gameId, hands)
}

fun task1(lines: List<String>): Int {
    val games = lines.map { line -> parseGame(line) }
    val amountOfCubes = mapOf(
            CubeColor.RED to 12,
            CubeColor.GREEN to 13,
            CubeColor.BLUE to 14,
    )
    return games.filter { game -> game.hands.all { hand -> hand.cubes.all { (color, amount) -> amount <= amountOfCubes[color]!! } } }.sumOf { game -> game.id }
}

fun task2(lines: List<String>): Int {
    val games = lines.map { line -> parseGame(line) }
    val gamePowers = games.map { game ->
        val minHand = game.hands.reduce { previousHand, gameHand ->
            GameHand(mapOf(
                    CubeColor.RED to max(previousHand.cubes.getOrDefault(CubeColor.RED, 0), gameHand.cubes.getOrDefault(CubeColor.RED, 0)),
                    CubeColor.GREEN to max(previousHand.cubes.getOrDefault(CubeColor.GREEN, 0), gameHand.cubes.getOrDefault(CubeColor.GREEN, 0)),
                    CubeColor.BLUE to max(previousHand.cubes.getOrDefault(CubeColor.BLUE, 0), gameHand.cubes.getOrDefault(CubeColor.BLUE, 0))
            ))
        }
        minHand.cubes.values.reduce (Int::times)
    }
    return gamePowers.sum()
}

fun main() {
    println(task1(File("${dir}_test.txt").readLines()))
    println(task1(File("${dir}.txt").readLines()))


    println(task2(File("${dir}_test.txt").readLines()))
    println(task2(File("${dir}.txt").readLines()))
}