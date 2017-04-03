package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import mont.gonzalo.phiuba.R;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.webview);

        final WebView myWebView = (WebView) this.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://guaranigrado.fi.uba.ar/autogestion/inicial.php");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                HtmlParser.getApprovedCourses(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
//                List<Course> list =  HtmlParser.getApprovedCourses(myWebView);
//                Log.d("courses me muero!!!", String.valueOf(list));
            }
        });
    }

}