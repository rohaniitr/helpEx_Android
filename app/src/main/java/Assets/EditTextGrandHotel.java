package Assets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by rohan on 20/5/17.
 */
public class EditTextGrandHotel extends EditText {

public EditTextGrandHotel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GrandHotel.otf"));
        }
}
