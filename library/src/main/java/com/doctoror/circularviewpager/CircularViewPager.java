/*
 * Copyright (C) 2015 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doctoror.circularviewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * {@link ViewPager} that uses {@link CircularOnPageChangeListener} and skips first fake page
 * whenever the adapter is set.
 */
public class CircularViewPager extends ViewPager {

    private final CircularOnPageChangeListener mInternalPageChangeListener
            = new CircularOnPageChangeListener(this);

    private final ArrayList<OnPageChangeListener> mExternalPageChangeListeners = new ArrayList<>();

    ViewPager.OnPageChangeListener mExternalPageChangeListener;

    public CircularViewPager(final Context context) {
        super(context);
    }

    public CircularViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        mExternalPageChangeListener = listener;
    }

    @Override
    public void addOnPageChangeListener(final OnPageChangeListener listener) {
        mExternalPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(final OnPageChangeListener listener) {
        mExternalPageChangeListeners.remove(listener);
    }

    @Override
    public void setAdapter(@Nullable final PagerAdapter adapter) {
        super.removeOnPageChangeListener(mInternalPageChangeListener);
        super.setAdapter(adapter);
        if (adapter != null && adapter.getCount() > 1) {
            setCurrentItemNoOffset(1, false);
        }
        super.addOnPageChangeListener(mInternalPageChangeListener);
    }

    @Override
    public void setCurrentItem(final int item) {
        super.setCurrentItem(item + 1);
    }

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem() - 1;
    }

    /* package */void setCurrentItemNoOffset(final int item, final boolean smooth) {
        super.setCurrentItem(item, smooth);
    }

    /* package */int getCurrentItemNoOffset() {
        return super.getCurrentItem();
    }

    void notifyPageScrolled(final int position, final float positionOffset,
            final int positionOffsetPixels) {
        if (mExternalPageChangeListener != null) {
            mExternalPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        final int size = mExternalPageChangeListeners.size();
        for (int i = 0; i < size; i++) {
            final OnPageChangeListener listener = mExternalPageChangeListeners.get(i);
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    void notifyPageSelected(final int position) {
        if (mExternalPageChangeListener != null) {
            mExternalPageChangeListener.onPageSelected(position);
        }
        final int size = mExternalPageChangeListeners.size();
        for (int i = 0; i < size; i++) {
            final OnPageChangeListener listener = mExternalPageChangeListeners.get(i);
            listener.onPageSelected(position);
        }
    }

    void notifyPageScrollStateChanged(final int state) {
        if (mExternalPageChangeListener != null) {
            mExternalPageChangeListener.onPageScrollStateChanged(state);
        }
        final int size = mExternalPageChangeListeners.size();
        for (int i = 0; i < size; i++) {
            final OnPageChangeListener listener = mExternalPageChangeListeners.get(i);
            listener.onPageScrollStateChanged(state);
        }
    }

    /**
     * Moves to a real page if first or last fake page chosen.
     */
    private final class CircularOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final CircularViewPager mViewPager;

        private int mLastPosition;
        private boolean mPositionChanged;

        public CircularOnPageChangeListener(@NonNull final CircularViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                final int positionOffsetPixels) {
            notifyPageScrolled(position - 1, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(final int position) {
            mPositionChanged = true;
            mLastPosition = position;
            if (position > 0 && position < getAdapter().getCount() - 1) {
                notifyPageSelected(position - 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            if (mPositionChanged && state == ViewPager.SCROLL_STATE_IDLE) {
                final int lastItem = mViewPager.getAdapter().getCount() - 1;
                final int position = mLastPosition;
                final int targetPosition;
                if (position == 0) {
                    targetPosition = lastItem - 1;
                } else if (position == lastItem) {
                    targetPosition = 1;
                } else {
                    return;
                }
                if (mViewPager.getCurrentItemNoOffset() != targetPosition) {
                    mViewPager.setCurrentItemNoOffset(targetPosition, false);
                }
                mPositionChanged = false;
            }
            notifyPageScrollStateChanged(state);
        }
    }
}
