package tw.invictus.vago

import org.junit.Test
import tw.invictus.vagolib.Vago
import tw.invictus.vagolib.isString

/**
 * Created by ivan on 2018/6/25.
 */
class AudioRespVoTest {
    @Test
    fun testVo() {
        Vago.testClass(AudioRespVo::class)
    }
    @Test
    fun testVoToBean() {
        VagoAudioRespVoKt.testToAudio(object: Vago.VagoCustomization() {
            override fun getDefaultValueByClass(clazz: Class<*>): Any? {
                if (clazz.isString()) {
                    return "1"
                }
                return super.getDefaultValueByClass(clazz)
            }
        })
    }
}