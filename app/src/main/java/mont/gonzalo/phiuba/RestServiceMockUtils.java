package mont.gonzalo.phiuba;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by gonzalo on 11/28/16.
 */

public class RestServiceMockUtils {
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);
        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }

    public static RetrofitMockClient getClient(Context context, final int httpStatusCode, String reason, String responseFileName) throws Exception {
        return new RetrofitMockClient(httpStatusCode, reason, getStringFromFile(context, responseFileName));
    }
}