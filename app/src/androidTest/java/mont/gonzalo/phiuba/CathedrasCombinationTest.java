package mont.gonzalo.phiuba;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mont.gonzalo.phiuba.layout.ActivityContext;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.CathedraSchedule;
import mont.gonzalo.phiuba.model.CathedrasCombination;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CathedrasCombinationTest {
    @Test
    public void test() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        UserCourses userCourses = UserCourses.getInstance();
        ActivityContext.set(appContext);

        Course c1 = new Course("10.01", "Curso A");
        Course c2 = new Course("20.02", "Curso B");
        Cathedra cat1 = createCathedra1();
        Cathedra cat2 = createCathedra2();
        Cathedra cat3 = createCathedra3();
        Cathedra cat4 = createCathedra4();

        CathedrasCombination catComb = CathedrasCombination.getInstance();
        catComb.addCathedraByCourse(c1.getCode(), Arrays.asList(cat1, cat2));
        catComb.addCathedraByCourse(c2.getCode(), Arrays.asList(cat3, cat4));

        catComb.buildTree();

        Assert.assertEquals(4, catComb.getCombinationCount());

        Assert.assertEquals(Arrays.asList(cat3, cat1), catComb.getAtPosition(1));
        Assert.assertEquals(Arrays.asList(cat4, cat1), catComb.getAtPosition(2));
        Assert.assertEquals(Arrays.asList(cat3, cat2), catComb.getAtPosition(3));
        Assert.assertEquals(Arrays.asList(cat4, cat2), catComb.getAtPosition(4));

        catComb.removeCollisions();

        Assert.assertEquals(2, catComb.getCombinationCount());

        Assert.assertEquals(Arrays.asList(cat3, cat1), catComb.getAtPosition(1));
        Assert.assertEquals(Arrays.asList(cat4, cat2), catComb.getAtPosition(2));
    }

    private Cathedra createCathedra1() {
        List<CathedraSchedule> cathedras = new ArrayList<CathedraSchedule>();
        CathedraSchedule cs1 = new CathedraSchedule();
        cs1.setDay("Lunes");
        cs1.setClassroomCode("20.02");
        cs1.setFrom("19:00");
        cs1.setTo("22:00");
        cathedras.add(cs1);

        cs1 = new CathedraSchedule();
        cs1.setClassroomCode("10.01");
        cs1.setDay("Miércoles");
        cs1.setFrom("19:00");
        cs1.setTo("22:00");
        cathedras.add(cs1);

        Cathedra c1 = new Cathedra();
        c1.setCourseCode("10.01");
        c1.setTeachers("Teachers 10.01 - 1");
        c1.setSchedule(cathedras);
        return c1;
    }

    private Cathedra createCathedra2() {
        List<CathedraSchedule> cathedras = new ArrayList<CathedraSchedule>();
        CathedraSchedule cs2 = new CathedraSchedule();
        cs2.setClassroomCode("10.01");
        cs2.setDay("Martes");
        cs2.setFrom("19:00");
        cs2.setTo("22:00");
        cathedras.add(cs2);

        cs2 = new CathedraSchedule();
        cs2.setClassroomCode("10.01");
        cs2.setDay("Jueves");
        cs2.setFrom("19:00");
        cs2.setTo("22:00");
        cathedras.add(cs2);

        Cathedra c2 = new Cathedra();
        c2.setCourseCode("10.01");
        c2.setTeachers("Teachers 10.01 - 2");
        c2.setSchedule(cathedras);
        return c2;
    }

    private Cathedra createCathedra3() {
        List<CathedraSchedule> cathedras = new ArrayList<CathedraSchedule>();
        CathedraSchedule cs3 = new CathedraSchedule();
        cs3.setClassroomCode("20.02");
        cs3.setDay("Martes");
        cs3.setFrom("17:00");
        cs3.setTo("20:00");
        cathedras.add(cs3);

        cs3 = new CathedraSchedule();
        cs3.setClassroomCode("20.02");
        cs3.setDay("Viernes");
        cs3.setFrom("16:00");
        cs3.setTo("19:00");
        cathedras.add(cs3);

        Cathedra c3 = new Cathedra();
        c3.setCourseCode("20.02");
        c3.setTeachers("Teachers 20.02 - 1");
        c3.setSchedule(cathedras);
        return c3;
    }

    private Cathedra createCathedra4() {
        List<CathedraSchedule> cathedras = new ArrayList<CathedraSchedule>();
        CathedraSchedule cs4 = new CathedraSchedule();
        cs4.setClassroomCode("20.02");
        cs4.setDay("Lunes");
        cs4.setFrom("17:00");
        cs4.setTo("20:00");
        cathedras.add(cs4);

        cs4 = new CathedraSchedule();
        cs4.setClassroomCode("20.02");
        cs4.setDay("Viernes");
        cs4.setFrom("19:00");
        cs4.setTo("22:00");
        cathedras.add(cs4);

        Cathedra c4 = new Cathedra();
        c4.setCourseCode("20.02");
        c4.setTeachers("Teachers 20.02 - 2");
        c4.setSchedule(cathedras);
        return c4;
    }

}
