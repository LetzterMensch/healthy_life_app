/*  Copyright (C) 2022-2024 José Rebelo, MPeter

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
package com.example.gr.utils;

public class ZipFileException extends Exception {
    public ZipFileException(final String message) {
        super(String.format("Error while reading ZIP file: %s", message));
    }

    public ZipFileException(final String message, final Throwable cause) {
        super(String.format("Error while reading ZIP file: %s", message), cause);
    }
}
