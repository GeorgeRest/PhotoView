/*
 Copyright 2011, 2012 Chris Banes.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.github.chrisbanes.photoview.sample;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.github.chrisbanes.photoview.PhotoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerActivity extends AppCompatActivity {
    PhotoView[] photoViews;
    private SamplePagerAdapter adapter;
    private int tempPreviousPosition = -1;
    private boolean isFirstPage = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = findViewById(R.id.view_pager);
        adapter = new SamplePagerAdapter();
        viewPager.setAdapter(adapter);
        photoViews = new PhotoView[adapter.getCount()];
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPosition = -1;
            boolean needToResetPrevious = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isFirstPage) {
                    tempPreviousPosition = 0;  // 设置为第一页
                    isFirstPage = false;  // 标志置为false，后续不再执行这个逻辑
                } else {
                    if (currentPosition >= 0) {
                        tempPreviousPosition = currentPosition;
                    }
                }
                currentPosition = position;
                System.out.println("onPageSelected position------------------- = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("onPageScrollStateChanged state------------------- = " + state);
                if (state == ViewPager.SCROLL_STATE_IDLE && tempPreviousPosition >= 0) {
                    PhotoView previousPhotoView = photoViews[tempPreviousPosition];
                    System.out.println("onPageScrollStateChanged tempPreviousPosition------------------- = " + tempPreviousPosition);
                    if (previousPhotoView != null) {
                        previousPhotoView.setScale(1.0f, false);
                    }
                    tempPreviousPosition = -1; // 重置临时变量
                }
            }
        });
    }

    class SamplePagerAdapter extends PagerAdapter {

        private final int[] sDrawables = {R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
            R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper};

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            photoViews[position] = photoView;
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
