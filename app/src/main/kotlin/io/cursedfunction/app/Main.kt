package io.cursedfunction.app

fun main() {
    /*
     * The output of this code will be:
     * ThIs iS SoMe sErIoUs tExT
     */
    val output1 = ThisIsSerious("This is some serious text", 43).mockMe()
    println(output1)

    /*
     * The output of this code will be:
     * ThIs iS AlSo vErY SeRiOuS TeXt!!!
     */
    val output2 = ThisIsAlsoSerious("This is also very serious text!!!").mockMe()
    println(output2)
}
