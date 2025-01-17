/*  Copyright (C) 2015-2024 Carsten Pfeiffer, Daniele Gobbetti

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
package com.example.gr.view.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.gr.controller.fragment.AbstractGBFragment;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final Set<AbstractGBFragment> fragments = new HashSet<>();
    private Object primaryFragment;

    public AbstractFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        if (fragment instanceof AbstractGBFragment) {
            fragments.add((AbstractGBFragment) fragment);
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragments.remove(object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object != primaryFragment) {
            primaryFragment = object;
            setCurrentFragment(primaryFragment);
        }
    }

    private void setCurrentFragment(Object newCurrentFragment) {
        for (AbstractGBFragment frag : fragments) {
            if (frag != newCurrentFragment) {
                frag.onMadeInvisibleInActivity();
            } else {
                frag.onMadeVisibleInActivityInternal();
            }
        }
    }
}
