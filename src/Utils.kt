import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Executes the given [block] and returns elapsed time in milliseconds.
 * https://proandroiddev.com/measuring-execution-times-in-kotlin-460a0285e5ea
 */

inline fun <T> measureTimeMillis(
    loggingFunction: (Long, T) -> Unit,
    function: () -> T
): T {

    val startTime = System.currentTimeMillis()
    val result: T = function.invoke()
    loggingFunction.invoke(System.currentTimeMillis() - startTime, result)

    return result
}


fun List<String>.splitLinesByEmptyLine(): Pair<List<String>, List<String>> {
    val splitIndex = this.indexOfFirst { it.isBlank() }
    return if (splitIndex == -1) {
        this to emptyList()
    } else {
        val first = this.subList(0, splitIndex)
        val second = this.drop(splitIndex + 1)
        first to second
    }
}
