package mont.gonzalo.phiuba.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mont.gonzalo.phiuba.R;

/**
 * Created by Gonzalo Montiel on 3/9/17.
 */
public class MaterialColors {
    static List<Integer> randomColors = new ArrayList<Integer>(Arrays.asList(
            R.color.material_1,
            R.color.material_2,
            R.color.material_3,
            R.color.material_4,
            R.color.material_5)
    );

    public static Integer getRandom() {
        Random r = new Random();
        r.setSeed(new Date().getTime());
        return randomColors.get(Math.abs(r.nextInt()) % randomColors.size());
    }
}
