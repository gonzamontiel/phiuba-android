package mont.gonzalo.phiuba.layout;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.CoursesStats;

/**
 * Created by Gonzalo Montiel on 9/27/17.
 */

class MyPlanStatisticsDialog extends Dialog {
    public MyPlanStatisticsDialog(Context context) {
        super(context);
        setContentView(R.layout.my_plan_stats);

        ProgressBar aprov_req_prog = (ProgressBar) findViewById(R.id.aprov_req_prog);
        TextView aprov_req_text = (TextView) findViewById(R.id.aprov_req_text);

        ProgressBar aprov_opt_prog = (ProgressBar) findViewById(R.id.aprov_opt_prog);
        TextView aprov_opt_text = (TextView) findViewById(R.id.aprov_opt_text);

        ProgressBar aprov_orient_prog = (ProgressBar) findViewById(R.id.aprov_orient_prog);
        TextView aprov_orient_text = (TextView) findViewById(R.id.aprov_orient_text);

        ProgressBar stud_req_prog = (ProgressBar) findViewById(R.id.stud_req_prog);
        TextView stud_req_text = (TextView) findViewById(R.id.stud_req_text);

        ProgressBar stud_opt_prog = (ProgressBar) findViewById(R.id.stud_opt_prog);
        TextView stud_opt_text = (TextView) findViewById(R.id.stud_opt_text);

        ProgressBar stud_orient_prog = (ProgressBar) findViewById(R.id.stud_orient_prog);
        TextView stud_orient_text = (TextView) findViewById(R.id.stud_orient_text);

        ProgressBar not_coursed_req_prog = (ProgressBar) findViewById(R.id.not_coursed_req_prog);
        TextView not_coursed_req_text = (TextView) findViewById(R.id.not_coursed_req_text);

        ProgressBar not_coursed_opt_prog = (ProgressBar) findViewById(R.id.not_coursed_opt_prog);
        TextView not_coursed_opt_text = (TextView) findViewById(R.id.not_coursed_opt_text);

        ProgressBar not_coursed_orient_prog = (ProgressBar) findViewById(R.id.not_coursed_orient_prog);
        TextView not_coursed_orient_text = (TextView) findViewById(R.id.not_coursed_orient_text);

        CoursesStats stats = CoursesStats.getInstance();

        int aprov_req_text_int = stats.getApprovedCount(CoursesStats.TYPE.OBL);
        int aprov_opt_text_int = stats.getApprovedCount(CoursesStats.TYPE.OPT);
        int aprov_orient_text_int = stats.getApprovedCount(CoursesStats.TYPE.ORIENT);
        int stud_req_text_int = stats.getStudyingCount(CoursesStats.TYPE.OBL);
        int stud_opt_text_int = stats.getStudyingCount(CoursesStats.TYPE.OPT);
        int stud_orient_text_int = stats.getStudyingCount(CoursesStats.TYPE.ORIENT);
        int not_coursed_req_text_int = stats.getNotCoursedCount(CoursesStats.TYPE.OBL);
        int not_coursed_opt_text_int = stats.getNotCoursedCount(CoursesStats.TYPE.OPT);
        int not_coursed_orient_text_int = stats.getNotCoursedCount(CoursesStats.TYPE.ORIENT);

        aprov_req_text.setText(String.valueOf(aprov_req_text_int));
        aprov_opt_text.setText(String.valueOf(aprov_opt_text_int));
        aprov_orient_text.setText(String.valueOf(aprov_orient_text_int));
        stud_req_text.setText(String.valueOf(stud_req_text_int));
        stud_opt_text.setText(String.valueOf(stud_opt_text_int));
        stud_orient_text.setText(String.valueOf(stud_orient_text_int));
        not_coursed_req_text.setText(String.valueOf(not_coursed_req_text_int));
        not_coursed_opt_text.setText(String.valueOf(not_coursed_opt_text_int));
        not_coursed_orient_text.setText(String.valueOf(not_coursed_orient_text_int));

        aprov_req_prog.setProgress((int) Math.floor(((double) aprov_req_text_int / (double) stats.getRequiredCount() * 100 )));
        aprov_opt_prog.setProgress((int) Math.floor(((double) aprov_opt_text_int / (double) stats.getRequiredCount() * 100 )));
        aprov_orient_prog.setProgress((int) Math.floor(((double) aprov_orient_text_int / (double) stats.getRequiredCount() * 100 )));

//        stud_req_prog.setProgress((int) Math.floor(((double) stud_req_text_int / (double) stats.getOptionalCount() * 100 )));
//        stud_opt_prog.setProgress((int) Math.floor(((double) stud_opt_text_int / (double) stats.getOptionalCount() * 100 )));
//        stud_orient_prog.setProgress((int) Math.floor(((double) stud_orient_text_int / (double) stats.getOptionalCount() * 100 )));
//        not_coursed_req_prog.setProgress((int) Math.floor(((double) not_coursed_req_text_int / (double) stats.getBranchCount() * 100 )));
//        not_coursed_opt_prog.setProgress((int) Math.floor(((double) not_coursed_opt_text_int / (double) stats.getBranchCount() * 100 )));
//        not_coursed_orient_prog.setProgress((int) Math.floor(((double) not_coursed_orient_text_int / (double) stats.getBranchCount() * 100 )));

        stud_req_prog.setProgress(0);
        stud_opt_prog.setProgress(0);
        stud_orient_prog.setProgress(0);
        not_coursed_req_prog.setProgress(0);
        not_coursed_opt_prog.setProgress(0);
        not_coursed_orient_prog.setProgress(0);
    }
}
