/**
 * Created by gonzalo on 10/11/16.
 */

package mont.gonzalo.phiuba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowCourseDetails extends AppCompatActivity {
    private TextView textViewCourseCode;
    private TextView textViewCourseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        //Initializing Views
        textViewCourseCode = (TextView) findViewById(R.id.textViewCourseCode);
        textViewCourseName = (TextView) findViewById(R.id.textViewCourseName);

        //Getting intent
        Intent intent = getIntent();

        //Displaying values by fetching from intent
        textViewCourseCode.setText(String.valueOf(intent.getIntExtra(Course.KEY_COURSE_CODE, 0)));
        textViewCourseName.setText(intent.getStringExtra(Course.KEY_COURSE_NAME));
    }
}
