package mont.gonzalo.phiuba.layout;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mont.gonzalo.phiuba.model.Course;

/**
 * Created by Gonzalo Montiel on 4/2/17.
 */
public class HtmlParser {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Course> getApprovedCourses(WebView myWebView) {
        myWebView.evaluateJavascript(
                "(function() { return ('<html>' + document.getElementsByTagName('html')[0].innerHTML + '</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        Log.d("HTML", html);
                    }
                });
        return null;
    }

    public static List<Course> getApprovedCourses(String text) {
        String[] lines = text.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("(")) {
                Pattern p = Pattern.compile(
                        "\\((\\d{2})(\\d{2})\\)\\s*(.*)\\s(\\d{2}\\/\\d{2}\\/\\d{4})\\s*(aprobado|reprobado)\\s*([\\d{1,2}\\w])",
                        Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(line);
                String replaced = m.replaceAll("$1.$2;$3;$4;$5;$6;");
                String[] parts = replaced.split(";");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String code = parts[0];
                String name = parts[1];
                try {
                    Date date = sdf.parse(parts[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String status = parts[3];
                int calif = Integer.parseInt(parts[4]);
            }
        }
        return null;
    }
}
