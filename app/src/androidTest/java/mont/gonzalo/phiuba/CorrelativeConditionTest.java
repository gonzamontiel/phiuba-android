package mont.gonzalo.phiuba;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import mont.gonzalo.phiuba.model.CorrelativeCondition;
import mont.gonzalo.phiuba.model.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CorrelativeConditionTest {
    @Test
    public void test() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        CorrelativeCondition ccCredits = new CorrelativeCondition("CRED200");
        CorrelativeCondition ccCourse= new CorrelativeCondition("75.01");
        CorrelativeCondition ccCualca= new CorrelativeCondition("CRE45.56");

        User mockUser = new User("Test", "Test");

        assertTrue(ccCredits.isCreditsCode());
        assertTrue(ccCourse.isCourseCode());

        assertEquals(ccCredits.getCreditsFromCorrCode(), 200);
        assertFalse(ccCredits.isCourseCode());
        assertFalse(ccCourse.isCreditsCode());

        assertFalse(ccCredits.isMetBy(mockUser));
    }


}
