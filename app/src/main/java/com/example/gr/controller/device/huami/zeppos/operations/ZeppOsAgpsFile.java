package com.example.gr.controller.device.huami.zeppos.operations;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import com.example.gr.controller.device.huami.UIHHContainer;
import com.example.gr.utils.GBZipFile;
import com.example.gr.utils.ZipFileException;

public class ZeppOsAgpsFile {
    private static final Logger LOG = LoggerFactory.getLogger(ZeppOsAgpsFile.class);

    private final byte[] zipBytes;

    public ZeppOsAgpsFile(final byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }

    public boolean isValid() {
        if (!GBZipFile.isZipFile(zipBytes)) {
            return false;
        }

        final GBZipFile zipFile = new GBZipFile(zipBytes);

        try {
            final byte[] manifestBin = zipFile.getFileFromZip("META-INF/MANIFEST.MF");
            if (manifestBin == null) {
                LOG.warn("Failed to get MANIFEST from zip");
                return false;
            }

            final String appJsonString = new String(manifestBin, StandardCharsets.UTF_8)
                    // Remove UTF-8 BOM if present
                    .replace("\uFEFF", "");
            final JSONObject jsonObject = new JSONObject(appJsonString);
            return jsonObject.getString("manifestVersion").equals("2.0") &&
                    zipFile.fileExists("EPO_BDS_3.DAT") &&
                    zipFile.fileExists("EPO_GAL_7.DAT") &&
                    zipFile.fileExists("EPO_GR_3.DAT");
        } catch (final Exception e) {
            LOG.error("Failed to parse read MANIFEST or check file", e);
        }

        return false;
    }

    public byte[] getUihhBytes() {
        final UIHHContainer uihh = new UIHHContainer();

        final GBZipFile zipFile = new GBZipFile(zipBytes);

        try {
            uihh.addFile(UIHHContainer.FileType.AGPS_EPO_GR_3, zipFile.getFileFromZip("EPO_GR_3.DAT"));
            uihh.addFile(UIHHContainer.FileType.AGPS_EPO_GAL_7, zipFile.getFileFromZip("EPO_GAL_7.DAT"));
            uihh.addFile(UIHHContainer.FileType.AGPS_EPO_BDS_3, zipFile.getFileFromZip("EPO_BDS_3.DAT"));
        } catch (final ZipFileException e) {
            throw new IllegalStateException("Failed to read file from zip", e);
        }

        return uihh.toRawBytes();
    }
}
