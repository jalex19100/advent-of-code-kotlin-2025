import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.io.path.readText
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.nio.file.Files

/**
 * Setup utility to scaffold a new Advent of Code day.
 *
 * Usage:
 *   kotlin -classpath build/libs/... SetupKt <dayNumber>
 * or via Gradle run configuration passing the day number as argument.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: Setup <dayNumber>")
        return
    }

    val dayNumber = args.first().toIntOrNull()
    if (dayNumber == null || dayNumber !in 1..25) {
        println("Please provide a valid day number between 1 and 25.")
        return
    }

    val dayName = "Day" + dayNumber.toString().padStart(2, '0')

    val srcDir = Path("src")
    val ktPath = srcDir.resolve("$dayName.kt")
    val inputPath = srcDir.resolve("$dayName.txt")
    val testInputPath = srcDir.resolve("${dayName}_Test.txt")

    // Ensure src directory exists
    if (!srcDir.exists()) {
        srcDir.createDirectories()
    }

    // Create Kotlin file if missing
    if (!ktPath.exists()) {
        val template = """
            fun main() {
                fun part1(input: List<String>): Int {
                    return input.size
                }

                fun part2(input: List<String>): Int {
                    return input.size
                }

                // Test Input
                val testInput = readInput("${dayName}_test")
                val part1Test = measureTimeMillis({ time, result -> println("Part1 Test (${'$'}time ms): ${'$'}result") }) {
                    part1(testInput)
                }
                check(part1Test == 1)
            
                val part2Test = measureTimeMillis({ time, result -> println("Part2 Test (${'$'}time ms): ${'$'}result") }) {
                    part2(testInput)
                }
                check(part2Test == 1)
            
                // User Input
                val input = readInput("${dayName}")
                val part1 = measureTimeMillis({ time, result -> println("Part1 (${'$'}time ms): ${'$'}result") }) {
                    part1(input)
                }
                check(part1 == 1)
            
                val part2 = measureTimeMillis({ time, result -> println("Part2 (${'$'}time ms): ${'$'}result") }) {
                    part2(input)
                }
                check(part2 == 1)
            }
        """.trimIndent() + System.lineSeparator()
        Files.writeString(ktPath, template)
        println("Created Kotlin file: ${ktPath.toFile().path}")
    } else {
        println("Kotlin file already exists: ${ktPath.toFile().path}")
    }

    // Create input files if missing
    if (!inputPath.exists()) {
        inputPath.writeText("")
        println("Created input file: ${inputPath.toFile().path}")
    } else {
        println("Input file already exists: ${inputPath.toFile().path}")
    }

    if (!testInputPath.exists()) {
        testInputPath.writeText("")
        println("Created test input file: ${testInputPath.toFile().path}")
    } else {
        println("Test input file already exists: ${testInputPath.toFile().path}")
    }

    // If AOC_SESSION is set, try to download the puzzle input for the given day
    val session = System.getenv("AOC_SESSION")
    if (session.isNullOrBlank()) {
        println("Environment variable AOC_SESSION not set; skipping input download.")
        return
    }

    // Only auto-write if the input file is empty to avoid accidental overwrites
    val shouldWrite = !inputPath.exists() || inputPath.readText().isBlank()
    if (!shouldWrite) {
        println("Input file is not empty; skipping auto-download. Delete/clear it to re-download.")
        return
    }

    val url = "https://adventofcode.com/2025/day/${dayNumber}/input"
    println("Attempting to download input from $url ...")

    try {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()

        val request = HttpRequest.newBuilder(URI.create(url))
            .timeout(Duration.ofSeconds(30))
            .header("Cookie", "session=$session")
            .header("User-Agent", "advent-of-code-kotlin-2025-setup (+https://github.com/jalex19100)")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val status = response.statusCode()
        if (status == 200) {
            val body = response.body()
            inputPath.writeText(body)
            println("Downloaded ${body.length} bytes to ${inputPath.toFile().path}")
        } else {
            println("Failed to download input (HTTP $status). The day may be locked or the AOC_SESSION is invalid.")
        }
    } catch (e: Exception) {
        println("Error downloading input: ${e.message}")
    }
}
