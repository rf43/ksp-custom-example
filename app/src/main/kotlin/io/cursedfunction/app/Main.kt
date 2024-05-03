package io.cursedfunction.app

import io.cursedfunction.annotation.mockme.MockingText

fun main() {
    println(ThisIsSerious("This is some serious text", 43).mockMe())
    println(ThisIsAlsoSerious("This is also very serious text!!!").mockMe())
}

@MockingText
data class ThisIsSerious(val str: String, val n: Int)

@MockingText
data class ThisIsAlsoSerious(val str: String)
