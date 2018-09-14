package tw.invictus.vagolib

import com.squareup.javapoet.TypeSpec

interface VagoGenerator {
    fun generate(): TypeSpec
}