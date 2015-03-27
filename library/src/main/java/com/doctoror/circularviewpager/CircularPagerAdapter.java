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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data for the ViewPager and creates fake pages.
 *
 * @param <T> Type of items
 */
public abstract class CircularPagerAdapter<T> extends PagerAdapter {

    private final List<T> mItems = new ArrayList<>();

    public final void updateData(@Nullable final List<T> data) {
        mItems.clear();
        if (data != null && !data.isEmpty()) {
            // Create a first fake page, which if a copy of the last one
            mItems.add(data.get(data.size() - 1));
            // Add data
            mItems.addAll(data);
            // Create a last fake page, which is a copy of the first one
            mItems.add(data.get(0));
        }
        notifyDataSetChanged();
    }

    @NonNull
    protected final List<T> getItems() {
        return mItems;
    }

    public final T getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public final int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public final int getCount() {
        return mItems.size();
    }

    @Override
    public final boolean isViewFromObject(final View view, final Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position,
            final Object object) {
        container.removeView((View) object);
    }
}
