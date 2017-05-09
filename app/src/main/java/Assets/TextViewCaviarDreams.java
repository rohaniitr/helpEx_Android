package Assets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by rohan on 15/3/17.
 */
public class TextViewCaviarDreams extends TextView {

    public TextViewCaviarDreams(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/CaviarDreams.ttf"));
    }

}