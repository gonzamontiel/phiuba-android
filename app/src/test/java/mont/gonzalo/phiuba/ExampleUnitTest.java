package mont.gonzalo.phiuba;

import org.junit.Test;

import mont.gonzalo.phiuba.layout.HtmlParser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String text = " Materia	Fecha	Resultado	Nota	Forma de Aprobación	Acta o Resolución	Plan \n" +
                " (6201) FISICA I A	29/07/2008	Aprobado	7	Examen	2-106-238	1986 \n" +
                " (6203) FISICA II A	24/02/2009	Aprobado	6	Examen	2-107-112	1986 \n";
        HtmlParser.getApprovedCourses(text);
    }
}