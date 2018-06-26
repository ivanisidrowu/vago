package tw.invictus.vagolib

/**
 * PrimitiveTypeConverter helps converting primitive types of classes automatically.
 *
 * Created by ivan on 2018/6/25.
 */
object VagoTypeConverter {
    fun convert(originalType: Class<*>, wantedType: Class<*>, value: Any, customization: Vago.Customization?): Any? {
        var result: Any? = customization?.getConvertedType(originalType, wantedType)

        if (result != null) {
            return result
        }
        when {
            originalType.isString() ->
                result = fromString(wantedType, value)
            originalType.isBoolean() ->
                result = fromBoolean(wantedType, value)
            originalType.isInt() ->
                result = fromInt(wantedType, value)
            originalType.isLong() ->
                result = fromLong(wantedType, value)
            originalType.isDouble() ->
                result = fromDouble(wantedType, value)
            originalType.isFloat() ->
                result = fromFloat(wantedType, value)
            originalType.isChar() ->
                result = fromChar(wantedType, value)
            originalType.isShort() ->
                result = fromShort(wantedType, value)
            else -> {
                VagoLog.w("${originalType.canonicalName} is not in convert types.")
            }
        }
        return result
    }

    private fun fromString(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as String
        when {
            wantedType.isBoolean() -> result = theValue.toBoolean()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isFloat() -> result = theValue.toFloat()
            wantedType.isChar() -> result = theValue.single()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert String to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromBoolean(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Boolean
        when {
            wantedType.isString() -> result = theValue.toString()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Boolean to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromInt(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Int
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isFloat() -> result = theValue.toFloat()
            wantedType.isChar() -> result = theValue.toChar()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Int to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromLong(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Long
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isFloat() -> result = theValue.toFloat()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Long to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromDouble(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Double
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isFloat() -> result = theValue.toFloat()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Double to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromFloat(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Float
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Float to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromChar(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Char
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isFloat() -> result = theValue.toFloat()
            wantedType.isShort() -> result = theValue.toShort()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Char to ${wantedType.name}")
            }
        }
        return result
    }

    private fun fromShort(wantedType: Class<*>, value: Any): Any? {
        var result: Any? = null
        val theValue = value as Short
        when {
            wantedType.isString() -> result = theValue.toString()
            wantedType.isInt() -> result = theValue.toInt()
            wantedType.isLong() -> result = theValue.toLong()
            wantedType.isDouble() -> result = theValue.toDouble()
            wantedType.isFloat() -> result = theValue.toFloat()
            else -> {
                VagoLog.w("${wantedType.canonicalName} is not in convert types, or it is impossible to convert Short to ${wantedType.name}")
            }
        }
        return result
    }
}