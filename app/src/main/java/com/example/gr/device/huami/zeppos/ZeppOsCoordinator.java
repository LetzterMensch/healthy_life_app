
package com.example.gr.device.huami.zeppos;

import androidx.annotation.NonNull;

import com.example.gr.data.parser.ActivitySummaryParser;
import com.example.gr.database.entities.AbstractActivitySample;
import com.example.gr.data.sample.SampleProvider;
import com.example.gr.database.entities.DaoSession;
import com.example.gr.database.entities.Device;
import com.example.gr.database.entities.HuamiExtendedActivitySampleDao;
import com.example.gr.database.entities.HuamiHeartRateManualSampleDao;
import com.example.gr.database.entities.HuamiHeartRateMaxSampleDao;
import com.example.gr.database.entities.HuamiHeartRateRestingSampleDao;
import com.example.gr.database.entities.HuamiPaiSampleDao;
import com.example.gr.database.entities.HuamiSleepRespiratoryRateSampleDao;
import com.example.gr.database.entities.HuamiSpo2SampleDao;
import com.example.gr.database.entities.HuamiStressSampleDao;
import com.example.gr.device.DeviceSupport;
import com.example.gr.device.GBDevice;
import com.example.gr.device.GBException;
import com.example.gr.device.capability.HeartRateCapability;
import com.example.gr.device.huami.HuamiCoordinator;
import com.example.gr.device.huami.HuamiExtendedSampleProvider;
import com.example.gr.device.huami.HuamiLanguageType;
import com.example.gr.device.huami.zeppos.services.ZeppOsAlexaService;
import com.example.gr.device.huami.zeppos.services.ZeppOsConfigService;
import com.example.gr.device.huami.zeppos.services.ZeppOsPhoneService;
import com.example.gr.device.huami.zeppos.services.ZeppOsShortcutCardsService;
import com.example.gr.utils.DeviceSettingsUtils;
import com.example.gr.utils.FileUtils;
import com.example.gr.utils.Prefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class ZeppOsCoordinator extends HuamiCoordinator {
    public abstract String getDeviceBluetoothName();

    public abstract Set<Integer> getDeviceSources();

    protected Map<Integer, String> getCrcMap() {
        // A map from CRC16 to human-readable version for flashable files
        return Collections.emptyMap();
    }

    @Override
    protected final Pattern getSupportedDeviceName() {
        // Most devices use the exact bluetooth name
        // Some devices have a " XXXX" suffix with the last 4 digits of mac address (eg. Mi Band 7)
        // *However*, some devices broadcast a 2nd bluetooth device with "-XXXX" suffix, which I believe
        // is only used for calls, and Gadgetbridge can't use for pairing, but I was not yet able to
        // fully confirm this, so we still recognize them.
        return Pattern.compile("^" + getDeviceBluetoothName() + "([- ][A-Z0-9]{4})?$");
    }

    @NonNull
    @Override
    public final Class<? extends DeviceSupport> getDeviceSupportClass() {
        return ZeppOsSupport.class;
    }

    /*
        @Override
        public InstallHandler findInstallHandler(final Uri uri, final Context context) {
            if (supportsAgpsUpdates()) {
                final ZeppOsAgpsInstallHandler agpsInstallHandler = new ZeppOsAgpsInstallHandler(uri, context);
                if (agpsInstallHandler.isValid()) {
                    return agpsInstallHandler;
                }
            }

            if (supportsGpxUploads()) {
                final ZeppOsGpxRouteInstallHandler gpxRouteInstallHandler = new ZeppOsGpxRouteInstallHandler(uri, context);
                if (gpxRouteInstallHandler.isValid()) {
                    return gpxRouteInstallHandler;
                }
            }

            final ZeppOsFwInstallHandler handler = new ZeppOsFwInstallHandler(
                    uri,
                    context,
                    getDeviceBluetoothName(),
                    getDeviceSources()
            );
            return handler.isValid() ? handler : null;
        }
    */
    @Override
    public boolean supportsScreenshots() {
        return true;
    }

    @Override
    public boolean supportsHeartRateMeasurement(final GBDevice device) {
        return true;
    }

    @Override
    public boolean supportsManualHeartRateMeasurement(final GBDevice device) {
        // TODO: It should be supported, but not yet properly implemented
        return false;
    }

    @Override
    public boolean supportsRealtimeData() {
        return true;
    }

    @Override
    public boolean supportsWeather() {
        return true;
    }

    @Override
    public boolean supportsUnicodeEmojis() {
        return true;
    }

    @Override
    public boolean supportsRemSleep() {
        return true;
    }

    @Override
    public boolean supportsActivityTracks() {
        return true;
    }

    @Override
    public boolean supportsStressMeasurement() {
        return true;
    }

    @Override
    public boolean supportsSpo2() {
        return true;
    }

    @Override
    public boolean supportsHeartRateStats() {
        return true;
    }

    @Override
    public boolean supportsPai() {
        return true;
    }

    @Override
    public boolean supportsSleepRespiratoryRate() {
        return true;
    }

    @Override
    public boolean supportsMusicInfo() {
        return true;
    }

    @Override
    public int getWorldClocksSlotCount() {
        return 20; // as enforced by Zepp
    }

    @Override
    public int getWorldClocksLabelLength() {
        return 30; // at least
    }

    @Override
    public boolean supportsDisabledWorldClocks() {
        return true;
    }

    @Override
    public boolean supportsAppsManagement(final GBDevice device) {
        return experimentalFeatures(device);
    }

//    @Override
//    public Class<? extends Activity> getAppsManagementActivity() {
//        return AppManagerActivity.class;
//    }

    @Override
    public File getAppCacheDir() throws IOException {
        return new File(FileUtils.getExternalFilesDir(), "zepp-os-app-cache");
    }

    @Override
    public String getAppCacheSortFilename() {
        return "zepp-os-app-cache-order.txt";
    }

    @Override
    public String getAppFileExtension() {
        return ".zip";
    }

    @Override
    public boolean supportsAppListFetching() {
        return true;
    }

    @Override
    public boolean supportsAppReordering() {
        return false;
    }

    @Override
    public boolean supportsCalendarEvents() {
        return true;
    }

    @Override
    protected void deleteDevice(@NonNull final GBDevice gbDevice,
                                @NonNull final Device device,
                                @NonNull final DaoSession session) throws GBException {
        final Long deviceId = device.getId();

        session.getHuamiExtendedActivitySampleDao().queryBuilder()
                .where(HuamiExtendedActivitySampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiStressSampleDao().queryBuilder()
                .where(HuamiStressSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiSpo2SampleDao().queryBuilder()
                .where(HuamiSpo2SampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiHeartRateManualSampleDao().queryBuilder()
                .where(HuamiHeartRateManualSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiHeartRateMaxSampleDao().queryBuilder()
                .where(HuamiHeartRateMaxSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiHeartRateRestingSampleDao().queryBuilder()
                .where(HuamiHeartRateRestingSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiPaiSampleDao().queryBuilder()
                .where(HuamiPaiSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();

        session.getHuamiSleepRespiratoryRateSampleDao().queryBuilder()
                .where(HuamiSleepRespiratoryRateSampleDao.Properties.DeviceId.eq(deviceId))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public SampleProvider<? extends AbstractActivitySample> getSampleProvider(final GBDevice device, final DaoSession session) {
        return new HuamiExtendedSampleProvider(device, session);
    }

    @Override
    public ActivitySummaryParser getActivitySummaryParser(final GBDevice device) {
        return new ZeppOsActivitySummaryParser();
    }

    @Override
    public boolean supportsAlarmSnoozing() {
        // All alarms snooze by default, there doesn't seem to be a flag that disables it
        return false;
    }

    @Override
    public boolean supportsSmartWakeup(final GBDevice device, int position) {
        return true;
    }

//    @Override
//    public int getReminderSlotCount(final GBDevice device) {
//        return ZeppOsRemindersService.getSlotCount(getPrefs(device));
//    }

//    @Override
//    public int getCannedRepliesSlotCount(final GBDevice device) {
//        return 16;
//    }

//    @Override
//    public int getContactsSlotCount(final GBDevice device) {
//        return getPrefs(device).getInt(ZeppOsContactsService.PREF_CONTACTS_SLOT_COUNT, 0);
//    }

    @Override
    public String[] getSupportedLanguageSettings(final GBDevice device) {
        // Return all known languages by default. Unsupported languages will be removed by Huami2021SettingsCustomizer
        final List<String> allLanguages = new ArrayList<>(HuamiLanguageType.idLookup.keySet());
        allLanguages.add(0, "auto");
        return allLanguages.toArray(new String[0]);
    }

//    @Override
//    public PasswordCapabilityImpl.Mode getPasswordCapability() {
//        return PasswordCapabilityImpl.Mode.NUMBERS_6;
//    }

    @Override
    public List<HeartRateCapability.MeasurementInterval> getHeartRateMeasurementIntervals() {
        // Return all known by default. Unsupported will be removed by Huami2021SettingsCustomizer
        return Arrays.asList(HeartRateCapability.MeasurementInterval.values());
    }

    @Override
    public int getBondingStyle() {
        return BONDING_STYLE_REQUIRE_KEY;
    }

    public boolean supportsContinuousFindDevice() {
        // TODO: Auto-detect continuous find device?
        return false;
    }

    public boolean supportsAgpsUpdates() {
        return true;
    }

    /**
     * true for Zepp OS 2.0+, false for Zepp OS 1
     */
    public boolean sendAgpsAsFileTransfer() {
        return true;
    }

    public boolean supportsGpxUploads() {
        return false;
    }

    public boolean supportsControlCenter() {
        // TODO: Auto-detect control center?
        return false;
    }

    public boolean supportsToDoList() {
        // TODO: Not yet implemented
        // TODO: When implemented, query the capability like reminders
        return false;
    }

    public boolean mainMenuHasMoreSection() {
        // Devices that have a control center don't seem to have a "more" section in the main menu
        return !supportsControlCenter();
    }

    public boolean supportsWifiHotspot(final GBDevice device) {
        return false;
    }

    public boolean supportsFtpServer(final GBDevice device) {
        return false;
    }

    public boolean hasGps(final GBDevice device) {
        return supportsConfig(device, ZeppOsConfigService.ConfigArg.WORKOUT_GPS_PRESET);
    }

    public boolean supportsAutoBrightness(final GBDevice device) {
        return supportsConfig(device, ZeppOsConfigService.ConfigArg.SCREEN_AUTO_BRIGHTNESS);
    }

    public boolean supportsBluetoothPhoneCalls(final GBDevice device) {
        return ZeppOsPhoneService.isSupported(getPrefs(device));
    }

    public boolean supportsShortcutCards(final GBDevice device) {
        return ZeppOsShortcutCardsService.isSupported(getPrefs(device));
    }

    public boolean supportsAlexa(final GBDevice device) {
        return experimentalFeatures(device) && ZeppOsAlexaService.isSupported(getPrefs(device));
    }

    private boolean supportsConfig(final GBDevice device, final ZeppOsConfigService.ConfigArg config) {
        return ZeppOsConfigService.deviceHasConfig(getPrefs(device), config);
    }

    public static boolean deviceHasConfig(final Prefs devicePrefs, final ZeppOsConfigService.ConfigArg config) {
        return devicePrefs.getBoolean(DeviceSettingsUtils.getPrefKnownConfig(config.name()), false);
    }

    public static boolean experimentalFeatures(final GBDevice device) {
        return getPrefs(device).getBoolean("zepp_os_experimental_features", false);
    }

    @Override
    public boolean validateAuthKey(final String authKey) {
        final byte[] authKeyBytes = authKey.trim().getBytes();
        return authKeyBytes.length == 32 || (authKey.trim().startsWith("0x") && authKeyBytes.length == 34);
    }
}
