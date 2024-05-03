package io.cursedfunction.app

import io.cursedfunction.annotation.mockme.MockingText

fun main() {
    /*
     * The output of this code will be:
     * ThIs iS SoMe sErIoUs tExT
     */
    println(ThisIsSerious("This is some serious text", 43).mockMe())

    /*
     * The output of this code will be:
     * ThIs iS AlSo vErY SeRiOuS TeXt!!!
     */
    println(ThisIsAlsoSerious("This is also very serious text!!!").mockMe())
}

@MockingText
data class ThisIsSerious(val str: String, val n: Int)

@MockingText
data class ThisIsAlsoSerious(val str: String)
