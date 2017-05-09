package Assets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by rohan on 14/3/17.
 */
public class TextViewGrandHotel extends TextView {

    public TextViewGrandHotel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/GrandHotel.otf"));
    }

}