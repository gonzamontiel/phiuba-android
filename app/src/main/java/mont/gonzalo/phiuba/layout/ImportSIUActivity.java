package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import mont.gonzalo.phiuba.R;

public class ImportSIUActivity extends Activity implements Observer {
    private static final java.lang.String URL = "http://guaranigrado.fi.uba.ar/autogestion/inicial.php";
    private Dialog progressDialog;
    private int loadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (loadProgress < 80) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Parece que la conexión con " + URL + " falló. Intenta de nuevo más tarde",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, 10000);


        this.setContentView(R.layout.webview);

        final WebView myWebView = (WebView) this.findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new ChromeClient());
        myWebView.loadUrl(URL);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final SIUImporter parser = new SIUImporter(this);
        parser.addObserver(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 parser.loadApprovedCourses(myWebView);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        Toast.makeText(this, " materias procesadas agregadas satisfactoriamente.", Toast.LENGTH_LONG);
        finish();
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            loadProgress = newProgress;
            if(newProgress >= 85) {
                progressDialog.dismiss();
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}