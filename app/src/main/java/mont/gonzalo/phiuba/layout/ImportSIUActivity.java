package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mont.gonzalo.phiuba.R;

public class ImportSIUActivity extends Activity {
    private static final java.lang.String URL = "http://guaranigrado.fi.uba.ar/autogestion/inicial.php";
    private Dialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = ProgressDialog.show(this, "", "Loading...");

//        new android.os.Handler().postDelayed(
//            new Runnable() {
//                public void run() {
//                    progress.dismiss();
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "Parece que la conexión con " + URL + " falló. Intenta de nuevo más tarde",
//                            Toast.LENGTH_LONG).show();
//                }
//            }, 10000);

        this.setContentView(R.layout.webview);

        final WebView myWebView = (WebView) this.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new ChromeClient());
        myWebView.loadUrl(URL);

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        String name = "34551965";
        String password = "parmesano098";
//        myWebView.loadUrl(readJsFileContents("jquery-3.2.1.min.js"));
        myWebView.loadUrl(readJsFileContents("injection.js"));

        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final SIUImporter parser = new SIUImporter();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 parser.loadApprovedCourses(myWebView);
            }
        });
    }

    public String readJsFileContents(String name) {
        BufferedReader reader = null;
        String jsCode = "javascript:";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(name)));

            String mLine;
            StringBuilder sb = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
            jsCode += sb.toString();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        Log.d("read code", jsCode);
        return jsCode;
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress >= 85) {
                progress.dismiss();
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d("message", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class WebAppInterface {
        public WebAppInterface(ImportSIUActivity importSIUActivity) {
        }
    }
}