package Assets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.rohansarkar.helpex.Activities.ExperimentTable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by rohan on 17/5/17.
 */
public class TableHorizontalScrollView extends HorizontalScrollView{

    private OnScrollListener listener;


    public TableHorizontalScrollView(Context context) {
        super(context);
        //Subscribing for event.
        EventBus.getDefault().register(this);
    }
    public TableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        EventBus.getDefault().register(this);
    }
    public TableHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
    }

    //Declaring Method to be called for EVENT.
    @Subscribe
    public void onEventMainThread(ExperimentTable.Event event) {
        if (!event.getView().equals(this)) scrollTo(event.getX(), event.getY());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) listener.onScroll(this, l, t);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public interface OnScrollListener {
        void onScroll(HorizontalScrollView view, int x, int y);
    }
}
