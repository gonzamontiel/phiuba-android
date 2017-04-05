package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

/**
 * Created by Gonzalo Montiel on 4/2/17.
 */
public class SIUImporter {

    private final Context context;

    public SIUImporter(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadApprovedCourses(WebView myWebView) {
        myWebView.evaluateJavascript(
                "(function() { return ('<html>' + window.frames[3].document.body.innerHTML + '</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        html = html.replaceAll("(\\\\u003C)", "<")
                                .replaceAll("(\\\\u003C)", ">")
                                .replaceAll("\\\\t", "")
                                .replace("\\n", "")
                                .replace("\\\"", "\"");

                        parseHtml(html);
                    }

                });
    }

    public void parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        if (doc.select("table caption") != null &&
                doc.select("table caption").text().contains("Historia académica")) {
            Elements rows = doc.select("table").first().select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");
                if (cols.size() >= 4) {
                    String codeName = cols.get(0).text();
                    String date = cols.get(1).text();
                    // String status = cols.get(2).text();
                    String calif = cols.get(3).text();

                    String[] codeAndNameSplitted = codeName
                            .replaceAll("\\((\\d{2})(\\d{2})\\)\\s*(.*)", "$1.$2;$3").split(";");
                    String code = codeAndNameSplitted[0];
                    String name = codeAndNameSplitted[1];

                    // Try conversions
                    Date examDate = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        examDate = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Integer calification = null;
                    try {
                        calification = Integer.parseInt(calif);
                    } catch (NumberFormatException e) {
                        calification = 0;
                    }
                    UserCourses.getInstance().addApproved(new Course(code, name), calification);
                }
            }
        }
    }
}
