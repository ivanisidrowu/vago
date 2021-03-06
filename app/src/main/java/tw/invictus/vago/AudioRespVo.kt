package tw.invictus.vago

import tw.invictus.annotation.VagoMapping
import tw.invictus.annotation.VagoMethod

class AudioRespVo {
    var id: Long? = null
        get() = field ?: 0L
    var type: Short? = null
        get() = field ?: 0
    var title: String? = null
        get() = field ?: ""
    var mediaUrl: String? = null
        get() = field ?: ""
    var coverUrl: String? = null
        get() = field ?: ""

    @VagoMapping(name = "chId", type = Long::class)
    var childId: String? = null
        get() = field ?: ""
}

@VagoMethod
fun AudioRespVo.toAudio(): AudioBean {
    return AudioBean(id!!, type!!, title!!, mediaUrl!!, coverUrl!!, childId!!.toLong())
}