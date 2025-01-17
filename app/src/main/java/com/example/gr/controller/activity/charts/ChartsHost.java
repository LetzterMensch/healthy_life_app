/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, José Rebelo,
    Petr Vaněk

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
package com.example.gr.controller.activity.charts;

import com.example.gr.controller.device.GBDevice;

import java.util.Date;


public interface ChartsHost {
    String DATE_PREV_DAY = ChartsHost.class.getName().concat(".date_prev_day");
    String DATE_NEXT_DAY = ChartsHost.class.getName().concat(".date_next_day");
    String DATE_PREV_WEEK = ChartsHost.class.getName().concat(".date_prev_week");
    String DATE_NEXT_WEEK = ChartsHost.class.getName().concat(".date_next_week");
    String DATE_PREV_MONTH = ChartsHost.class.getName().concat(".date_prev_month");
    String DATE_NEXT_MONTH = ChartsHost.class.getName().concat(".date_next_month");

    String REFRESH = ChartsHost.class.getName().concat(".refresh");

    GBDevice getDevice();

    void setStartDate(Date startDate);

    void setEndDate(Date endDate);

    Date getStartDate();

    Date getEndDate();

//    void setDateInfo(String dateInfo);

//    ViewGroup getDateBar();

    void enableSwipeRefresh(boolean enable);
}
