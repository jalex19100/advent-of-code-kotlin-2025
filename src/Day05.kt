fun main() {

    fun List<String>.isFresh(ingredient: Number): Boolean {
        return true
    }

    fun List<String>.toRanges(): List<LongRange> {
        return this.map { rangeString ->
            val (start, end) = rangeString.split("-")
            start.toLong()..end.toLong()
        }
    }

    fun part1(input: List<String>): Int {
        val (freshIngredientRangeStrings, availableIngredients) = input.splitLinesByEmptyLine()
        val freshIngredientRanges = freshIngredientRangeStrings.toRanges()
        val availableFreshIngredients = availableIngredients.count { ingredient ->
            freshIngredientRanges.any { freshIngredientRange -> ingredient.toLong() in freshIngredientRange }
        }
        return availableFreshIngredients
    }

    fun part2(input: List<String>): Long {
        val (freshIngredientRangeStrings, _) = input.splitLinesByEmptyLine()
        val freshIngredientRanges = freshIngredientRangeStrings.toRanges()
        val allFreshIngredientsCount = freshIngredientRanges
            .sortedBy { it.first }
            .fold(ArrayList<LongRange>()) { acc, freshIngredientRange ->
                val last = acc.lastOrNull()
                if (last != null && freshIngredientRange.first <= last.last + 1) {
                    val extendEnd = maxOf(last.last, freshIngredientRange.last)
                    acc[acc.lastIndex] = last.first..extendEnd
                } else {
                    acc.add(freshIngredientRange)
                }
                acc
            }
            .sumOf { (it.last - it.first) + 1 }
        return allFreshIngredientsCount
    }

    // Test Input
    val testInput = readInput("Day05_test")
    val part1Test = measureTimeMillis({ time, result -> println("Part1 Test ($time ms): $result") }) {
        part1(testInput)
    }
    check(part1Test == 3)

    val part2Test = measureTimeMillis({ time, result -> println("Part2 Test ($time ms): $result") }) {
        part2(testInput)
    }
    check(part2Test == 14L)

    // User Input
    val input = readInput("Day05")
    val part1 = measureTimeMillis({ time, result -> println("Part1 ($time ms): $result") }) {
        part1(input)
    }
    check(part1 == 698)

    val part2 = measureTimeMillis({ time, result -> println("Part2 ($time ms): $result") }) {
        part2(input)
    }
    check(part2 == 352807801032167L)
}

