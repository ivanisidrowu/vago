package tw.invictus.vagolib

/**
 * Created by ivan on 2018/6/22.
 */
fun <T : Any> Class<T>.isString(): Boolean {
    return this == String::class.java
}

fun <T : Any> Class<T>.isBoolean(): Boolean {
    return this == Boolean::class.javaPrimitiveType || this == Boolean::class.java || this == Boolean::class.javaObjectType
}

fun <T : Any> Class<T>.isInt(): Boolean {
    return this == Int::class.javaPrimitiveType || this == Int::class.java || this == Int::class.javaObjectType
}

fun <T : Any> Class<T>.isLong(): Boolean {
    return this == Long::class.javaPrimitiveType || this == Long::class.java || this == Long::class.javaObjectType
}

fun <T : Any> Class<T>.isDouble(): Boolean {
    return this == Double::class.javaPrimitiveType || this == Double::class.java || this == Double::class.javaObjectType
}

fun <T : Any> Class<T>.isFloat(): Boolean {
    return this == Float::class.javaPrimitiveType || this == Float::class.java || this == Float::class.javaObjectType
}

fun <T : Any> Class<T>.isChar(): Boolean {
    return this == Char::class.javaPrimitiveType || this == Char::class.java || this == Char::class.javaObjectType
}

fun <T : Any> Class<T>.isShort(): Boolean {
    return this == Short::class.javaPrimitiveType || this == Short::class.java || this == Short::class.javaObjectType
}

fun <T : Any> Class<T>.isList(): Boolean {
    return this == List::class.javaPrimitiveType || this == List::class.java || this == List::class.javaObjectType
}
