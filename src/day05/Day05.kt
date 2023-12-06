package day05

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTimedValue

const val dir = "src/day05/day05"

data class Replacement(val destination: Long, val source: Long, val length: Long) {
    fun containsSource(input: Long): Boolean {
        return source <= input && input < source + length
    }
    fun remap(input: Long): Long {
        return destination + input - source
    }
    val sourceRange
        get() = Range(source, length)
}
data class ReplacementMap(val replacements: List<Replacement>) {
    fun remap(input: Long): Long {
        val replacement = replacements.firstOrNull { replacement -> replacement.containsSource(input) } ?: return input
        return replacement.remap(input)
    }
}
data class Range(val start: Long, val length: Long) {
    val end
        get() = start + length - 1

    fun isEmpty() = length <= 0
}
fun Range.intersect(other: Range): Range {
    if (max(end, other.end) - min(start, other.start) < length + other.length) {
        val s = max(start, other.start)
        val e = min(end, other.end)
        return Range(s, e-s+1)
    }
    return Range(start, 0)
}
fun Range.subtract(other: Range): List<Range> {
    if (other.isEmpty()){
        return listOf(this)
    }
    return listOf(
            Range(start, other.start - start),
            Range(other.end, end - other.end)
    )
}
fun parse(input: String): Pair<List<Long>, List<ReplacementMap>> {
    val sections = input.split("\n\n").toMutableList()
    val seeds = sections.removeFirst().replace("seeds: ", "").split(" ").map { it.toLong() }
    val maps = sections.map { sectionText ->
        val sectionLines = sectionText.split("\n").toMutableList()
        sectionLines.removeFirst()
        ReplacementMap(sectionLines.map { line ->
            val (destination, source, length) = line.split(" ").map { it.toLong() }
            Replacement(destination, source, length)
        })
    }
    return Pair(seeds, maps)
}
fun task1(input: String): Long {
    val (seeds, maps) = parse(input)
    return seeds.minOf { seed ->
        var start = seed
        maps.forEach { map -> start = map.remap(start) }
        start
    }
}
fun task2(input: String): Long {
    val (seedsBefore, maps) = parse(input)
    val seeds = seedsBefore.chunked(2).map { (start, length) -> Range(start, length) }
    val endRanges = maps.fold(seeds){ previousSeeds, map ->
        val newRanges = map.replacements.fold(previousSeeds){ seeds, replacement ->
            seeds.flatMap { seed ->
                val intersection = seed.intersect(replacement.sourceRange)
                (seed.subtract(intersection) + intersection)
            }.filter { !it.isEmpty() }
        }
        newRanges.map {
            Range(map.remap(it.start), it.length)
        }
    }
    return endRanges.minOf { range -> range.start }
}
fun main() {
    println(task1(File("${dir}_test.txt").readText()))
    println(task1(File("${dir}.txt").readText()))

    val range = Range(1, 7)
    println(range.subtract(Range(3, 3)))

    println(task2(File("${dir}_test.txt").readText()))
    val input = File("${dir}.txt").readText()
    val result = measureTimedValue {
        task2(input)
    }
    println(result.value)
    println("Took: ${result.duration}" )

}