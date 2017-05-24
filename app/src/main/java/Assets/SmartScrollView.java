package Assets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by rohan on 24/5/17.
 */
public class SmartScrollView extends RecyclerView.OnScrollListener {

    @Override
    public final void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this);
        }
    }
}