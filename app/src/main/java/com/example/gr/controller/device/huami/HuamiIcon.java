/*  Copyright (C) 2017-2024 Andreas Shimokawa, Daniele Gobbetti, Lukas
    Veneziano, Maxim Baz, musover

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

package com.example.gr.controller.device.huami;


import com.example.gr.controller.device.model.NotificationType;

public class HuamiIcon {
    // icons which are unsure which app they are for are suffixed with _NN
    public static final byte WECHAT = 0;
    public static final byte PENGUIN_1 = 1;
    public static final byte MI_CHAT_2 = 2;
    public static final byte FACEBOOK = 3;
    public static final byte TWITTER = 4;
    public static final byte MI_APP_5 = 5;
    public static final byte SNAPCHAT = 6;
    public static final byte WHATSAPP = 7;
    public static final byte RED_WHITE_FIRE_8 = 8;
    public static final byte CHINESE_9 = 9;
    public static final byte ALARM_CLOCK = 10;
    public static final byte APP_11 = 11;
    public static final byte INSTAGRAM = 12;
    public static final byte CHAT_BLUE_13 = 13;
    public static final byte COW_14 = 14;
    public static final byte CHINESE_15 = 15;
    public static final byte CHINESE_16 = 16;
    public static final byte STAR_17 = 17;
    public static final byte APP_18 = 18;
    public static final byte CHINESE_19 = 19;
    public static final byte CHINESE_20 = 20;
    public static final byte CALENDAR = 21;
    public static final byte FACEBOOK_MESSENGER = 22;
    public static final byte VIBER = 23;
    public static final byte LINE = 24;
    public static final byte TELEGRAM = 25;
    public static final byte KAKAOTALK = 26;
    public static final byte SKYPE = 27;
    public static final byte VKONTAKTE = 28;
    public static final byte POKEMONGO = 29;
    public static final byte HANGOUTS = 30;
    public static final byte MI_31 = 31;
    public static final byte CHINESE_32 = 32;
    public static final byte CHINESE_33 = 33;
    public static final byte EMAIL = 34;
    public static final byte WEATHER = 35;
    public static final byte HR_WARNING_36 = 36;

    public static byte mapToIconId(NotificationType type) {
        switch (type) {
            case UNKNOWN:
            case GENERIC_NAVIGATION:
                return APP_11;
            case CONVERSATIONS:
            case RIOT:
            case HIPCHAT:
            case KONTALK:
            case ANTOX:
            case GENERIC_SMS:
            case WECHAT:
                return WECHAT;
            case GENERIC_EMAIL:
            case GMAIL:
            case YAHOO_MAIL:
            case OUTLOOK:
                return EMAIL;
            case GENERIC_CALENDAR:
            case BUSINESS_CALENDAR:
                return CALENDAR;
            case FACEBOOK:
                return FACEBOOK;
            case FACEBOOK_MESSENGER:
            case SIGNAL:
                return FACEBOOK_MESSENGER;
            case GOOGLE_HANGOUTS:
            case GOOGLE_MESSENGER:
                return HANGOUTS;
            case INSTAGRAM:
            case GOOGLE_PHOTOS:
                return INSTAGRAM;
            case KAKAO_TALK:
                return KAKAOTALK;
            case LINE:
                return LINE;
            case WIRE:
            case THREEMA:
                return CHAT_BLUE_13;
            case TWITTER:
                return TWITTER;
            case SKYPE:
                return SKYPE;
            case SNAPCHAT:
                return SNAPCHAT;
            case TELEGRAM:
                return TELEGRAM;
            case VIBER:
            case DISCORD:
                return VIBER;
            case WHATSAPP:
                return WHATSAPP;
            case GENERIC_ALARM_CLOCK:
                return ALARM_CLOCK;
        }
        return APP_11;
    }

    //amazfit workaround
    public static boolean acceptsSender(byte iconId){
        switch(iconId){
            case WECHAT:
            case PENGUIN_1:
            case MI_CHAT_2:
            case SNAPCHAT:
            case WHATSAPP:
            case RED_WHITE_FIRE_8:
            case INSTAGRAM:
            case CHAT_BLUE_13:
            case COW_14:
            case CHINESE_20:
            case FACEBOOK_MESSENGER:
            case VIBER:
            case LINE:
            case TELEGRAM:
            case VKONTAKTE:
            case CHINESE_32:
            case EMAIL:
                return true;
        }

        return false;
    }
}
