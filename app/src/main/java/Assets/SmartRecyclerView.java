package Assets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by rohan on 23/5/17.
 */
public class SmartRecyclerView extends RecyclerView {

    public int computedWidth = 0;

    public SmartRecyclerView(Context context) {
        super(context);
    }

    public SmartRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public int getMinimumWidth() {
        return computedWidth;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        setMeasuredDimension(computedWidth, getMeasuredHeight());
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return computedWidth;
    }
}

