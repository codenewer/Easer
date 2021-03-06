/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.core.ui.data;


import androidx.annotation.NonNull;

import ryey.easer.commons.local_skill.InvalidDataInputException;
import ryey.easer.commons.local_skill.SkillView;
import ryey.easer.commons.local_skill.StorageData;

public abstract class SkillViewContainerFragment<T extends StorageData> extends AbstractSkillDataFragment<T> {

    protected SkillView<T> pluginViewFragment = null;

    @Override
    protected void _fill(@NonNull T data) {
        pluginViewFragment.fill(data);
    }

    @Override
    @NonNull
    public T getData() throws InvalidDataInputException {
        return pluginViewFragment.getData();
    }
}
