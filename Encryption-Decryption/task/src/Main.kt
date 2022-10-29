package encryptdecrypt

import java.io.File
import java.io.FileNotFoundException

var message = ""
var key = 0

fun main(args: Array<String>) {
    val algorithm = if("-alg" !in args) "shift" else args[args.indexOf("-alg") + 1]
    val job = if ("-mode" !in args) "enc" else args[args.indexOf("-mode") + 1]
    key = if ("-key" !in args) 0 else args[args.indexOf("-key") + 1].toInt()
    if ("-data" in args) {
        message = args[args.indexOf("-data") + 1]
    } else if ("-data" !in args && "-in" in args){
        try {
            val fileIn = args[args.indexOf("-in") + 1]
            message = File(fileIn).readText()
        } catch (e: FileNotFoundException) {
            return
        }
    } else if ("-data" !in args && "-in" !in args) message = ""
    val out = if ("-out" in args) args[args.indexOf("-out") + 1] else ""
    if ("-out" !in args) {
        println(if (job == "enc") encrypt(message, algorithm) else decrypt(message, algorithm))
    } else if ("-out" in args && job == "enc") {
        try {
            File(out).writeText(encrypt(message, algorithm))
        } catch (e: FileNotFoundException) {
            return
        }
    } else {
        try {
            File(out).writeText(decrypt(message, algorithm))
        } catch (e: FileNotFoundException) {
            return
        }
    }
}

fun encrypt(message: String, alg : String) : String {
    var phrase = ""
   if (alg == "shift") {
       val dictionary = "abcdefghijklmnopqrstuvwxyz"
       for (i in message) {
           if (i !in dictionary) {
               phrase += i
           } else {
               val number = dictionary.indexOf(i) + key
               if (i.isUpperCase()) {
                   phrase += if (number <= 25) dictionary[number].uppercase() else dictionary[number - 26].uppercase()
               } else {
                   phrase += if (number <= 25) dictionary[number] else dictionary[number - 26]
               }
           }
       }
    } else {
        for (i in message) {
            val char = (i.code + key).toChar()
            phrase += char
        }
    }
    return phrase
}

fun decrypt(word: String, alg: String) : String {
    var phrase = ""
    if (alg == "shift") {
        val dictionary = "abcdefghijklmnopqrstuvwxyz"
        for (i in message) {
            if (i !in dictionary) {
                phrase += i
            } else {
                val number = dictionary.indexOf(i) - key
                if (i.isUpperCase()) {
                    phrase += if (number < 0) dictionary[26 + number].uppercase() else dictionary[number].uppercase()
                } else {
                    phrase += if (number < 0) dictionary[26 + number] else dictionary[number]
                }
            }
        }
    } else {
        for (i in word) {
            val char = (i.code - key).toChar()
            phrase += char
        }
    }
    return phrase
}
