package io.cursedfunction.app

import io.cursedfunction.annotation.mockme.MockingText

@MockingText
data class ThisIsSerious(val str: String, val n: Int)

@MockingText
data class ThisIsAlsoSerious(val str: String)
