#set( $Code = "bar" )
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    check(part1(listOf("...")) == 1)
    val sampleInput = readInput("Day${Day}_Test")
    check(part1(sampleInput) == 1)
    check(part2(sampleInput) == 1)

    val input = readInput("Day$Day")
    part1(input).println()
    part2(input).println()
}
