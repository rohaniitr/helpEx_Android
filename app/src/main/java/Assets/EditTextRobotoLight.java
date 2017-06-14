package Assets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by rohan on 14/6/17.
 */
public class EditTextRobotoLight extends EditText {

    public EditTextRobotoLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf"));
    }
}
