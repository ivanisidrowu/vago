package tw.invictus.vago

import android.os.Parcel
import android.os.Parcelable
import tw.invictus.annotation.VagoParcel

@VagoParcel
data class AudioBean(var id: Long, var type: Short, var title: String,
                     var mediaUrl: String, var coverUrl: String, var chId: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readInt().toShort(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeInt(type.toInt())
        parcel.writeString(title)
        parcel.writeString(mediaUrl)
        parcel.writeString(coverUrl)
        parcel.writeLong(chId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioBean> {
        override fun createFromParcel(parcel: Parcel): AudioBean {
            return AudioBean(parcel)
        }

        override fun newArray(size: Int): Array<AudioBean?> {
            return arrayOfNulls(size)
        }
    }
}