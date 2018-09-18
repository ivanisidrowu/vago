package tw.invictus.processor

import com.squareup.javapoet.TypeSpec

interface VagoGenerator {
    fun generate(): TypeSpec
}