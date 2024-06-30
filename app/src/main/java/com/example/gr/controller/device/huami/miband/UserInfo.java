/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, Sergey Trofimov

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
package com.example.gr.controller.device.huami.miband;


import com.example.gr.model.ActivityUser;


public class UserInfo {

    private final String btAddress;
    private final String alias;
    private final int gender;
    private final int age;
    private final int height;
    private final int weight;
    private final int type;

    private byte[] data = new byte[20];

    /**
     * Creates a default user info.
     *
     * @param btAddress the address of the MI Band to connect to.
     */
    public static UserInfo getDefault(String btAddress) {
        return new UserInfo(btAddress, "1550050550", ActivityUser.defaultUserGender, ActivityUser.defaultUserAge, ActivityUser.defaultUserHeightCm, ActivityUser.defaultUserWeightKg, 0);
    }

    /**
     * Creates a user info with the given data
     *
     * @param address the address of the MI Band to connect to.
     * @throws IllegalArgumentException when the given values are not valid
     */
    public static UserInfo create(String address, String alias, int gender, int age, int height, int weight, int type) throws IllegalArgumentException {
        if (address == null || address.length() == 0 || alias == null || alias.length() == 0 || gender < 0 || age <= 0 || weight <= 0 || type < 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        try {
            return new UserInfo(address, alias, gender, age, height, weight, type);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Illegal user info data", ex);
        }
    }

    /**
     * Creates a user info with the given data
     *
     * @param address the address of the MI Band to connect to.
     */
    private UserInfo(String address, String alias, int gender, int age, int height, int weight, int type) {
        this.btAddress = address;
        this.alias = alias;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.type = type;
    }

}
