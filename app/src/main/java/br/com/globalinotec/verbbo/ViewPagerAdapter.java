package br.com.globalinotec.verbbo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int layoutList[];
    private ViewGroup views;

    public ViewPagerAdapter(Context context, int layoutList[]) {

        this.context = context;
        this.layoutList = layoutList;
        
        this.views = new ViewGroup(context) {

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };
    }

    @Override
    public int getCount() {

        return layoutList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutList[position], container, false);

        views.addView(view);
        container.addView(view);

        return view;
    }

    public View getViewAt(int pos) {

        return views.getChildAt(pos);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

        //super.destroyItem(container, position, object);
    }
}
