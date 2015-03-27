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

package com.doctoror.circularviewpager.sample;

import com.doctoror.circularviewpager.CircularPagerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public final class DemoActivity extends Activity {

    private static final TestPagerItem[] DATA = {
            new TestPagerItem(0xffaaaaff, "Page One"),
            new TestPagerItem(0xff77ffff, "Page Two"),
            new TestPagerItem(0xff77ff77, "Page Three")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TestPagerAdapter(getLayoutInflater(), DATA));
    }

    private static final class TestPagerItem {

        public final int color;
        public final String text;

        private TestPagerItem(final int color, final String text) {
            this.color = color;
            this.text = text;
        }
    }

    private static final class TestPagerAdapter extends CircularPagerAdapter<TestPagerItem> {

        @NonNull
        private final LayoutInflater mLayoutInflater;

        TestPagerAdapter(@NonNull final LayoutInflater inflater,
                @NonNull final TestPagerItem[] data) {
            mLayoutInflater = inflater;
            updateData(Arrays.asList(data));
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final TestPagerItem item = getItem(position);
            final TextView view = (TextView) mLayoutInflater
                    .inflate(R.layout.pager_item, container, false);
            view.setText(item.text);
            view.setBackgroundColor(item.color);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position,
                final Object object) {
            container.removeView((View) object);
        }
    }
}
