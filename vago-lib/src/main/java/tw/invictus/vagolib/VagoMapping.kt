package tw.invictus.vagolib

import kotlin.reflect.KClass

/**
 * Created by ivan on 2018/6/22.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class VagoMapping(val name: String, val type: KClass<*>)