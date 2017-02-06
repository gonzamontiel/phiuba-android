package mont.gonzalo.phiuba.UI;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.CathedraSchedule;

public class CathedraActivity extends ListActivity {
    public static final String INTENT_CODE = "cathedra";
    private Cathedra mCathedra;

    private ArrayList<CathedraSchedule> mSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_schedules);
        Intent intent = getIntent();
        mCathedra = (Cathedra) intent.getSerializableExtra(CathedraActivity.INTENT_CODE);
        setListAdapter(new ArrayAdapter<CathedraSchedule>(this, R.layout.schedule_row, mCathedra.getSchedule()));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CathedraSchedule cs = (CathedraSchedule) l.getItemAtPosition(position);
        Log.d("Schedule", cs + " was clicked");
    }
}