package com.example.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GRDaoGenerator {
    private static final String VALID_FROM_UTC = "validFromUTC";
    private static final String VALID_TO_UTC = "validToUTC";
    private static final String MAIN_PACKAGE = "com.example.gr";
    private static final String MODEL_PACKAGE = ".model";
    private static final String FUNC_PACKAGE = MAIN_PACKAGE + ".device.model";
    private static final String VALID_BY_DATE = FUNC_PACKAGE + ".ValidByDate";
    private static final String ACTIVITY_SUMMARY = MAIN_PACKAGE + MODEL_PACKAGE + ".ActivitySummary";
    private static final String OVERRIDE = "@Override";
    private static final String SAMPLE_RAW_INTENSITY = "rawIntensity";
    private static final String SAMPLE_STEPS = "steps";
    private static final String SAMPLE_RAW_KIND = "rawKind";
    private static final String SAMPLE_HEART_RATE = "heartRate";
    private static final String SAMPLE_TEMPERATURE = "temperature";
    private static final String SAMPLE_TEMPERATURE_TYPE = "temperatureType";
    private static final String TIMESTAMP_FROM = "timestampFrom";
    private static final String TIMESTAMP_TO = "timestampTo";

    public static void main(String[] args) throws Exception {
        final Schema schema = new Schema(71, "com.example.gr.data.sample");

        Entity userAttributes = addUserAttributes(schema);
        Entity user = addUserInfo(schema, userAttributes);

        Entity deviceAttributes = addDeviceAttributes(schema);
        Entity device = addDevice(schema, deviceAttributes);

        // yeah deep shit, has to be here (after device) for db upgrade and column order
        // because addDevice adds a property to deviceAttributes also....
        deviceAttributes.addStringProperty("volatileIdentifier");

        Entity tag = addTag(schema);
        Entity userDefinedActivityOverlay = addActivityDescription(schema, tag, user);

        addMiBandActivitySample(schema, user, device);
        addHuamiExtendedActivitySample(schema, user, device);
        addHuamiStressSample(schema, user, device);
        addHuamiSpo2Sample(schema, user, device);
        addHuamiHeartRateManualSample(schema, user, device);
        addHuamiHeartRateMaxSample(schema, user, device);
        addHuamiHeartRateRestingSample(schema, user, device);
        addHuamiPaiSample(schema, user, device);
        addHuamiSleepRespiratoryRateSample(schema, user, device);

        addCalendarSyncState(schema, device);
        addAlarms(schema, user, device);
        addReminders(schema, user, device);
        addWorldClocks(schema, user, device);
        addContacts(schema, user, device);
        addAppSpecificNotificationSettings(schema, device);

        Entity notificationFilter = addNotificationFilters(schema);

        addNotificationFilterEntry(schema, notificationFilter);

        addActivitySummary(schema, user, device);
        addBatteryLevel(schema, device);
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static Entity addTag(Schema schema) {
        Entity tag = addEntity(schema, "Tag");
        tag.addIdProperty();
        tag.addStringProperty("name").notNull();
        tag.addStringProperty("description").javaDocGetterAndSetter("An optional description of this tag.");
        tag.addLongProperty("userId").notNull();

        return tag;
    }


    private static Entity addActivityDescription(Schema schema, Entity tag, Entity user) {
        Entity activityDesc = addEntity(schema, "ActivityDescription");
        activityDesc.setJavaDoc("A user may further specify his activity with a detailed description and the help of tags.\nOne or more tags can be added to a given activity range.");
        activityDesc.addIdProperty();
        activityDesc.addIntProperty(TIMESTAMP_FROM).notNull();
        activityDesc.addIntProperty(TIMESTAMP_TO).notNull();
        activityDesc.addStringProperty("details").javaDocGetterAndSetter("An optional detailed description, specific to this very activity occurrence.");

        Property userId = activityDesc.addLongProperty("userId").notNull().getProperty();
        activityDesc.addToOne(user, userId);

        Entity activityDescTagLink = addEntity(schema, "ActivityDescTagLink");
        activityDescTagLink.addIdProperty();
        Property sourceId = activityDescTagLink.addLongProperty("activityDescriptionId").notNull().getProperty();
        Property targetId = activityDescTagLink.addLongProperty("tagId").notNull().getProperty();

        activityDesc.addToMany(tag, activityDescTagLink, sourceId, targetId);

        return activityDesc;
    }

    private static Entity addUserInfo(Schema schema, Entity userAttributes) {
        Entity user = addEntity(schema, "User");
        user.addIdProperty();
        user.addStringProperty("name").notNull();
        user.addDateProperty("birthday").notNull();
        user.addIntProperty("gender").notNull();
        Property userId = userAttributes.addLongProperty("userId").notNull().getProperty();

        // sorted by the from-date, newest first
        Property userAttributesSortProperty = getPropertyByName(userAttributes, VALID_FROM_UTC);
        user.addToMany(userAttributes, userId).orderDesc(userAttributesSortProperty);

        return user;
    }

    private static Property getPropertyByName(Entity entity, String propertyName) {
        for (Property prop : entity.getProperties()) {
            if (propertyName.equals(prop.getPropertyName())) {
                return prop;
            }
        }
        throw new IllegalStateException("Could not find property " + propertyName + " in entity " + entity.getClassName());
    }


    private static Entity addUserAttributes(Schema schema) {
        // additional properties of a user, which may change during the lifetime of a user
        // this allows changing attributes while preserving user identity
        Entity userAttributes = addEntity(schema, "UserAttributes");
        userAttributes.addIdProperty();
        userAttributes.addIntProperty("heightCM").notNull();
        userAttributes.addIntProperty("weightKG").notNull();
        userAttributes.addIntProperty("sleepGoalHPD").javaDocGetterAndSetter("Desired number of hours of sleep per day.");
        userAttributes.addIntProperty("stepsGoalSPD").javaDocGetterAndSetter("Desired number of steps per day.");
        addDateValidityTo(userAttributes);

        return userAttributes;
    }

    private static void addDateValidityTo(Entity entity) {
        entity.addDateProperty(VALID_FROM_UTC).codeBeforeGetter(OVERRIDE);
        entity.addDateProperty(VALID_TO_UTC).codeBeforeGetter(OVERRIDE);

        entity.implementsInterface(VALID_BY_DATE);
    }

    private static Entity addDevice(Schema schema, Entity deviceAttributes) {
        Entity device = addEntity(schema, "Device");
        device.addIdProperty();
        device.addStringProperty("name").notNull();
        device.addStringProperty("manufacturer").notNull();
        device.addStringProperty("identifier").notNull().unique().javaDocGetterAndSetter("The fixed identifier, i.e. MAC address of the device.");
        device.addIntProperty("type").notNull().javaDocGetterAndSetter("The DeviceType key, i.e. the GBDevice's type.").codeBeforeGetterAndSetter("@Deprecated");
        device.addStringProperty("typeName").notNull().javaDocGetterAndSetter("The DeviceType enum name, for example SONY_WH_1000XM3");
        device.addStringProperty("model").javaDocGetterAndSetter("An optional model, further specifying the kind of device.");
        device.addStringProperty("alias");
        device.addStringProperty("parentFolder").javaDocGetterAndSetter("Folder name containing this device.");
        Property deviceId = deviceAttributes.addLongProperty("deviceId").notNull().getProperty();
        // sorted by the from-date, newest first
        Property deviceAttributesSortProperty = getPropertyByName(deviceAttributes, VALID_FROM_UTC);
        device.addToMany(deviceAttributes, deviceId).orderDesc(deviceAttributesSortProperty);

        return device;
    }

    private static Entity addDeviceAttributes(Schema schema) {
        Entity deviceAttributes = addEntity(schema, "DeviceAttributes");
        deviceAttributes.addIdProperty();
        deviceAttributes.addStringProperty("firmwareVersion1").notNull();
        deviceAttributes.addStringProperty("firmwareVersion2");
        addDateValidityTo(deviceAttributes);

        return deviceAttributes;
    }

    private static void addActivitySummary(Schema schema, Entity user, Entity device) {
        Entity summary = addEntity(schema, "BaseActivitySummary");
        summary.implementsInterface(ACTIVITY_SUMMARY);
        summary.addIdProperty();

        summary.setJavaDoc(
                "This class represents the summary of a user's activity event. I.e. a walk, hike, a bicycle tour, etc.");

        summary.addStringProperty("name").codeBeforeGetter(OVERRIDE);
        summary.addDateProperty("startTime").notNull().codeBeforeGetter(OVERRIDE);
        summary.addDateProperty("endTime").notNull().codeBeforeGetter(OVERRIDE);
        summary.addIntProperty("activityKind").notNull().codeBeforeGetter(OVERRIDE);

        summary.addIntProperty("baseLongitude").javaDocGetterAndSetter("Temporary, bip-specific");
        summary.addIntProperty("baseLatitude").javaDocGetterAndSetter("Temporary, bip-specific");
        summary.addIntProperty("baseAltitude").javaDocGetterAndSetter("Temporary, bip-specific");

        summary.addStringProperty("gpxTrack").codeBeforeGetter(OVERRIDE);
        summary.addStringProperty("rawDetailsPath");

        Property deviceId = summary.addLongProperty("deviceId").notNull().codeBeforeGetter(OVERRIDE).getProperty();
        summary.addToOne(device, deviceId);
        Property userId = summary.addLongProperty("userId").notNull().codeBeforeGetter(OVERRIDE).getProperty();
        summary.addToOne(user, userId);
        summary.addStringProperty("summaryData");
        summary.addByteArrayProperty("rawSummaryData");
    }

    private static void addNotificationFilterEntry(Schema schema, Entity notificationFilterEntity) {
        Entity notificatonFilterEntry = addEntity(schema, "NotificationFilterEntry");
        notificatonFilterEntry.addIdProperty().autoincrement();
        Property notificationFilterId = notificatonFilterEntry.addLongProperty("notificationFilterId").notNull().getProperty();
        notificatonFilterEntry.addStringProperty("notificationFilterContent").notNull().getProperty();
        notificatonFilterEntry.addToOne(notificationFilterEntity, notificationFilterId);
    }

    private static Entity addNotificationFilters(Schema schema) {
        Entity notificatonFilter = addEntity(schema, "NotificationFilter");
        Property appIdentifier = notificatonFilter.addStringProperty("appIdentifier").notNull().getProperty();

        notificatonFilter.addIdProperty().autoincrement();

        Index indexUnique = new Index();
        indexUnique.addProperty(appIdentifier);
        indexUnique.makeUnique();
        notificatonFilter.addIndex(indexUnique);

        Property notificationFilterMode = notificatonFilter.addIntProperty("notificationFilterMode").notNull().getProperty();
        Property notificationFilterSubMode = notificatonFilter.addIntProperty("notificationFilterSubMode").notNull().getProperty();
        return notificatonFilter;
    }

    private static Entity addAppSpecificNotificationSettings(Schema schema, Entity device) {
        Entity perAppSetting = addEntity(schema, "AppSpecificNotificationSetting");
        perAppSetting.addStringProperty("packageId").notNull().primaryKey();
        Property deviceId = perAppSetting.addLongProperty("deviceId").primaryKey().notNull().getProperty();
        perAppSetting.addToOne(device, deviceId);
        perAppSetting.addStringProperty("ledPattern");
        perAppSetting.addStringProperty("vibrationPattern");
        perAppSetting.addStringProperty("vibrationRepetition");
        return perAppSetting;
    }

    private static void addContacts(Schema schema, Entity user, Entity device) {
        Entity contact = addEntity(schema, "Contact");
        contact.implementsInterface("com.example.gr.device.model.Contact");
        Property deviceId = contact.addLongProperty("deviceId").notNull().getProperty();
        Property userId = contact.addLongProperty("userId").notNull().getProperty();
        Property contactId = contact.addStringProperty("contactId").notNull().primaryKey().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(deviceId);
        indexUnique.addProperty(userId);
        indexUnique.addProperty(contactId);
        indexUnique.makeUnique();
        contact.addIndex(indexUnique);
        contact.addStringProperty("name").notNull();
        contact.addStringProperty("number").notNull();
        contact.addToOne(user, userId);
        contact.addToOne(device, deviceId);
    }

    private static void addWorldClocks(Schema schema, Entity user, Entity device) {
        Entity worldClock = addEntity(schema, "WorldClock");
        worldClock.implementsInterface("com.example.gr.device.model.WorldClock");
        Property deviceId = worldClock.addLongProperty("deviceId").notNull().getProperty();
        Property userId = worldClock.addLongProperty("userId").notNull().getProperty();
        Property worldClockId = worldClock.addStringProperty("worldClockId").notNull().primaryKey().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(deviceId);
        indexUnique.addProperty(userId);
        indexUnique.addProperty(worldClockId);
        indexUnique.makeUnique();
        worldClock.addIndex(indexUnique);
        worldClock.addStringProperty("label").notNull();
        worldClock.addBooleanProperty("enabled");
        worldClock.addStringProperty("code");
        worldClock.addStringProperty("timeZoneId").notNull();
        worldClock.addToOne(user, userId);
        worldClock.addToOne(device, deviceId);
    }

    private static void addReminders(Schema schema, Entity user, Entity device) {
        Entity reminder = addEntity(schema, "Reminder");
        reminder.implementsInterface("com.example.gr.device.model.Reminder");
        Property deviceId = reminder.addLongProperty("deviceId").notNull().getProperty();
        Property userId = reminder.addLongProperty("userId").notNull().getProperty();
        Property reminderId = reminder.addStringProperty("reminderId").notNull().primaryKey().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(deviceId);
        indexUnique.addProperty(userId);
        indexUnique.addProperty(reminderId);
        indexUnique.makeUnique();
        reminder.addIndex(indexUnique);
        reminder.addStringProperty("message").notNull();
        reminder.addDateProperty("date").notNull();
        reminder.addIntProperty("repetition").notNull();
        reminder.addToOne(user, userId);
        reminder.addToOne(device, deviceId);
    }

    private static void addAlarms(Schema schema, Entity user, Entity device) {
        Entity alarm = addEntity(schema, "Alarm");
        alarm.implementsInterface("com.example.gr.device.model.Alarm");
        Property deviceId = alarm.addLongProperty("deviceId").notNull().getProperty();
        Property userId = alarm.addLongProperty("userId").notNull().getProperty();
        Property position = alarm.addIntProperty("position").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(deviceId);
        indexUnique.addProperty(userId);
        indexUnique.addProperty(position);
        indexUnique.makeUnique();
        alarm.addIndex(indexUnique);
        alarm.addBooleanProperty("enabled").notNull();
        alarm.addBooleanProperty("smartWakeup").notNull();
        alarm.addIntProperty("smartWakeupInterval");
        alarm.addBooleanProperty("snooze").notNull();
        alarm.addIntProperty("repetition").notNull().codeBeforeGetter(
                "public boolean isRepetitive() { return getRepetition() != ALARM_ONCE; } " +
                        "public boolean getRepetition(int dow) { return (this.repetition & dow) > 0; }"
        );
        alarm.addIntProperty("hour").notNull();
        alarm.addIntProperty("minute").notNull();
        alarm.addBooleanProperty("unused").notNull();
        alarm.addStringProperty("title");
        alarm.addStringProperty("description");
        alarm.addToOne(user, userId);
        alarm.addToOne(device, deviceId);
    }

    private static void addCalendarSyncState(Schema schema, Entity device) {
        Entity calendarSyncState = addEntity(schema, "CalendarSyncState");
        calendarSyncState.addIdProperty();
        Property deviceId = calendarSyncState.addLongProperty("deviceId").notNull().getProperty();
        Property calendarEntryId = calendarSyncState.addLongProperty("calendarEntryId").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(deviceId);
        indexUnique.addProperty(calendarEntryId);
        indexUnique.makeUnique();
        calendarSyncState.addIndex(indexUnique);
        calendarSyncState.addToOne(device, deviceId);
        calendarSyncState.addIntProperty("hash").notNull();
    }

    private static Entity addHuamiHeartRateMaxSample(Schema schema, Entity user, Entity device) {
        Entity hrMaxSample = addEntity(schema, "HuamiHeartRateMaxSample");
        addCommonTimeSampleProperties("AbstractHeartRateSample", hrMaxSample, user, device);
        hrMaxSample.addIntProperty("utcOffset").notNull();
        hrMaxSample.addIntProperty(SAMPLE_HEART_RATE).notNull().codeBeforeGetter(OVERRIDE);
        return hrMaxSample;
    }

    private static Entity addHuamiHeartRateRestingSample(Schema schema, Entity user, Entity device) {
        Entity hrRestingSample = addEntity(schema, "HuamiHeartRateRestingSample");
        addCommonTimeSampleProperties("AbstractHeartRateSample", hrRestingSample, user, device);
        hrRestingSample.addIntProperty("utcOffset").notNull();
        hrRestingSample.addIntProperty(SAMPLE_HEART_RATE).notNull().codeBeforeGetter(OVERRIDE);
        return hrRestingSample;
    }

    private static Entity addHuamiPaiSample(Schema schema, Entity user, Entity device) {
        Entity paiSample = addEntity(schema, "HuamiPaiSample");
        addCommonTimeSampleProperties("AbstractPaiSample", paiSample, user, device);
        paiSample.addIntProperty("utcOffset").notNull();
        paiSample.addFloatProperty("paiLow").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addFloatProperty("paiModerate").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addFloatProperty("paiHigh").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addIntProperty("timeLow").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addIntProperty("timeModerate").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addIntProperty("timeHigh").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addFloatProperty("paiToday").notNull().codeBeforeGetter(OVERRIDE);
        paiSample.addFloatProperty("paiTotal").notNull().codeBeforeGetter(OVERRIDE);
        return paiSample;
    }

    private static Entity addHuamiSleepRespiratoryRateSample(Schema schema, Entity user, Entity device) {
        Entity sleepRespiratoryRateSample = addEntity(schema, "HuamiSleepRespiratoryRateSample");
        addCommonTimeSampleProperties("AbstractSleepRespiratoryRateSample", sleepRespiratoryRateSample, user, device);
        sleepRespiratoryRateSample.addIntProperty("utcOffset").notNull();
        sleepRespiratoryRateSample.addIntProperty("rate").notNull().codeBeforeGetter(OVERRIDE);
        return sleepRespiratoryRateSample;
    }

    private static Entity addHuamiHeartRateManualSample(Schema schema, Entity user, Entity device) {
        Entity hrManualSample = addEntity(schema, "HuamiHeartRateManualSample");
        addCommonTimeSampleProperties("AbstractHeartRateSample", hrManualSample, user, device);
        hrManualSample.addIntProperty("utcOffset").notNull();
        hrManualSample.addIntProperty(SAMPLE_HEART_RATE).notNull().codeBeforeGetter(OVERRIDE);
        return hrManualSample;
    }

    private static Entity addHuamiSpo2Sample(Schema schema, Entity user, Entity device) {
        Entity spo2sample = addEntity(schema, "HuamiSpo2Sample");
        addCommonTimeSampleProperties("AbstractSpo2Sample", spo2sample, user, device);
        spo2sample.addIntProperty("typeNum").notNull().codeBeforeGetter(OVERRIDE);
        spo2sample.addIntProperty("spo2").notNull().codeBeforeGetter(OVERRIDE);
        return spo2sample;
    }

    private static void addCommonTimeSampleProperties(String superClass, Entity timeSample, Entity user, Entity device) {
        timeSample.setSuperclass(superClass);
        timeSample.addImport("com.example.gr.data.sample.TimeSampleProvider");
        timeSample.setJavaDoc(
                "This class represents a sample specific to the device. Values might be device specific, depending on the sample type.\n" +
                        "Normalized values can be retrieved through the corresponding {@link TimeSampleProvider}.");
        timeSample.addLongProperty("timestamp").notNull().codeBeforeGetterAndSetter(OVERRIDE).primaryKey();
        Property deviceId = timeSample.addLongProperty("deviceId").primaryKey().notNull().codeBeforeGetterAndSetter(OVERRIDE).getProperty();
        timeSample.addToOne(device, deviceId);
        Property userId = timeSample.addLongProperty("userId").notNull().codeBeforeGetterAndSetter(OVERRIDE).getProperty();
        timeSample.addToOne(user, userId);
    }

    private static Entity addHuamiStressSample(Schema schema, Entity user, Entity device) {
        Entity stressSample = addEntity(schema, "HuamiStressSample");
        addCommonTimeSampleProperties("AbstractStressSample", stressSample, user, device);
        stressSample.addIntProperty("typeNum").notNull().codeBeforeGetter(OVERRIDE);
        stressSample.addIntProperty("stress").notNull().codeBeforeGetter(OVERRIDE);
        return stressSample;
    }

    private static Entity addHuamiExtendedActivitySample(Schema schema, Entity user, Entity device) {
        Entity activitySample = addEntity(schema, "HuamiExtendedActivitySample");
        addCommonActivitySampleProperties("MiBandActivitySample", activitySample, user, device);
        activitySample.addIntProperty(SAMPLE_RAW_INTENSITY).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        activitySample.addIntProperty(SAMPLE_STEPS).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        activitySample.addIntProperty(SAMPLE_RAW_KIND).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        addHeartRateProperties(activitySample);
        activitySample.addIntProperty("unknown1");
        activitySample.addIntProperty("sleep");
        activitySample.addIntProperty("deepSleep");
        activitySample.addIntProperty("remSleep");
        return activitySample;
    }

    private static Entity addMiBandActivitySample(Schema schema, Entity user, Entity device) {
        Entity activitySample = addEntity(schema, "MiBandActivitySample");
        activitySample.implementsSerializable();
        addCommonActivitySampleProperties("AbstractActivitySample", activitySample, user, device);
        activitySample.addIntProperty(SAMPLE_RAW_INTENSITY).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        activitySample.addIntProperty(SAMPLE_STEPS).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        activitySample.addIntProperty(SAMPLE_RAW_KIND).notNull().codeBeforeGetterAndSetter(OVERRIDE);
        addHeartRateProperties(activitySample);
        return activitySample;
    }

    private static void addHeartRateProperties(Entity activitySample) {
        activitySample.addIntProperty(SAMPLE_HEART_RATE).notNull().codeBeforeGetterAndSetter(OVERRIDE);
    }

    private static void addCommonActivitySampleProperties(String superClass, Entity activitySample, Entity user, Entity device) {
        activitySample.setSuperclass(superClass);
        activitySample.addImport("com.example.gr.data.sample.SampleProvider");
        activitySample.setJavaDoc(
                "This class represents a sample specific to the device. Values like activity kind or\n" +
                        "intensity, are device specific. Normalized values can be retrieved through the\n" +
                        "corresponding {@link SampleProvider}.");
        activitySample.addIntProperty("timestamp").notNull().codeBeforeGetterAndSetter(OVERRIDE).primaryKey();
        Property deviceId = activitySample.addLongProperty("deviceId").primaryKey().notNull().codeBeforeGetterAndSetter(OVERRIDE).getProperty();
        activitySample.addToOne(device, deviceId);
        Property userId = activitySample.addLongProperty("userId").notNull().codeBeforeGetterAndSetter(OVERRIDE).getProperty();
        activitySample.addToOne(user, userId);
    }


    private static Entity addEntity(Schema schema, String className) {
        Entity entity = schema.addEntity(className);
        entity.addImport("de.greenrobot.dao.AbstractDao");
        return entity;
    }

    private static Entity addBatteryLevel(Schema schema, Entity device) {
        Entity batteryLevel = addEntity(schema, "BatteryLevel");
        batteryLevel.implementsSerializable();
        batteryLevel.addIntProperty("timestamp").notNull().primaryKey();
        Property deviceId = batteryLevel.addLongProperty("deviceId").primaryKey().notNull().getProperty();
        batteryLevel.addToOne(device, deviceId);
        batteryLevel.addIntProperty("level").notNull();
        batteryLevel.addIntProperty("batteryIndex").notNull().primaryKey();
        return batteryLevel;
    }

}