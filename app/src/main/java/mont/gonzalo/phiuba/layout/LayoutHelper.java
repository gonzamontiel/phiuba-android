package mont.gonzalo.phiuba.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class LayoutHelper {

    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static void addImageToView(Context context, String img, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context)
                .load(img)
                .resize(1024, 1024)
                .centerInside()
                .into(imageView);
    }

    static void setTextViewHTML(TextView text, String html, final Activity activity) {
        CharSequence sequence = fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span, activity);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    static private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span,
                                          final Activity activity) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.d("ClickLink", span.getURL());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(span.getURL()));
                activity.startActivity(i);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }
}
