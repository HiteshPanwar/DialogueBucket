package com.dialogueapp.dialoguebucket.dialoguesharingapp.others;

/**
 * Created by HITESH on 10/9/2017.
 */
/**
 * Created by HITESH on 10/8/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int[] firstVisibleItem;
    int totalItemCount;
    int visibleItemCount;
    //int[] firstVisibleItem;
    private int current_page = 1;

    private StaggeredGridLayoutManager mLayoutManagerStag;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager mLayoutManagerStag) {
        this.mLayoutManagerStag = mLayoutManagerStag;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManagerStag.getItemCount();
        firstVisibleItem = mLayoutManagerStag.findFirstVisibleItemPositions(null);

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem[1] + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
