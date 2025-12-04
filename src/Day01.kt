fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val sampleInput = readInput("Day01_test")
    check(part1(sampleInput) == 1)
    check(part2(sampleInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
