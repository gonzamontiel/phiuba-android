package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mont.gonzalo.phiuba.R;

/**
 * Created by Gonzalo Montiel on 3/9/17.
 */

public class MaterialColors {
    public static Integer getRandom(int seed) {
        List<Integer> randomColors = new ArrayList<Integer>(Arrays.asList(
                R.color.material_1,
                R.color.material_2,
                R.color.material_3,
                R.color.material_4,
                R.color.material_5)
        );
        Collections.shuffle(randomColors);
        return randomColors.get(seed % randomColors.size());
    }
}
