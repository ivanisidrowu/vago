package tw.invictus.vago;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JavaBeanTest {
    @Test
    public void testBean() {
        VagoParcelable.testAudioBeanParcelable(null);
    }
}
