package tw.invictus.vago

import android.os.Parcel
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import tw.invictus.vagolib.Vago

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("tw.invictus.vago", appContext.packageName)
    }

    @Test
    fun testBean() {
        val bean: AudioBean = Vago.createInstance(AudioBean::class) as AudioBean

        val parcel = Parcel.obtain()
        bean.writeToParcel(parcel, bean.describeContents())
        parcel.setDataPosition(0)

        val createdFromParcel = AudioBean.createFromParcel(parcel)
        Assert.assertThat(createdFromParcel == bean, CoreMatchers.`is`(true))
    }
}
