import java.io.File

fun main() {
    val phrase = "./text.txt"
    val file = File(phrase).readText()
    val text = file.split(" ")
    println(text.size)
}