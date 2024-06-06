package com.example.gr.database.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HUAMI_EXTENDED_ACTIVITY_SAMPLE".
*/
public class HuamiExtendedActivitySampleDao extends AbstractDao<HuamiExtendedActivitySample, Void> {

    public static final String TABLENAME = "HUAMI_EXTENDED_ACTIVITY_SAMPLE";

    /**
     * Properties of entity HuamiExtendedActivitySample.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Timestamp = new Property(0, int.class, "timestamp", true, "TIMESTAMP");
        public final static Property DeviceId = new Property(1, long.class, "deviceId", true, "DEVICE_ID");
        public final static Property UserId = new Property(2, long.class, "userId", false, "USER_ID");
        public final static Property RawIntensity = new Property(3, int.class, "rawIntensity", false, "RAW_INTENSITY");
        public final static Property Steps = new Property(4, int.class, "steps", false, "STEPS");
        public final static Property RawKind = new Property(5, int.class, "rawKind", false, "RAW_KIND");
        public final static Property HeartRate = new Property(6, int.class, "heartRate", false, "HEART_RATE");
        public final static Property Unknown1 = new Property(7, Integer.class, "unknown1", false, "UNKNOWN1");
        public final static Property Sleep = new Property(8, Integer.class, "sleep", false, "SLEEP");
        public final static Property DeepSleep = new Property(9, Integer.class, "deepSleep", false, "DEEP_SLEEP");
        public final static Property RemSleep = new Property(10, Integer.class, "remSleep", false, "REM_SLEEP");
    };

    private DaoSession daoSession;


    public HuamiExtendedActivitySampleDao(DaoConfig config) {
        super(config);
    }
    
    public HuamiExtendedActivitySampleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HUAMI_EXTENDED_ACTIVITY_SAMPLE\" (" + //
                "\"TIMESTAMP\" INTEGER  NOT NULL ," + // 0: timestamp
                "\"DEVICE_ID\" INTEGER  NOT NULL ," + // 1: deviceId
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: userId
                "\"RAW_INTENSITY\" INTEGER NOT NULL ," + // 3: rawIntensity
                "\"STEPS\" INTEGER NOT NULL ," + // 4: steps
                "\"RAW_KIND\" INTEGER NOT NULL ," + // 5: rawKind
                "\"HEART_RATE\" INTEGER NOT NULL ," + // 6: heartRate
                "\"UNKNOWN1\" INTEGER," + // 7: unknown1
                "\"SLEEP\" INTEGER," + // 8: sleep
                "\"DEEP_SLEEP\" INTEGER," + // 9: deepSleep
                "\"REM_SLEEP\" INTEGER," + // 10: remSleep
                "PRIMARY KEY (" +
                "\"TIMESTAMP\" ," +
                "\"DEVICE_ID\" ) ON CONFLICT REPLACE)" + ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? " WITHOUT ROWID;" : ";")
        );
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HUAMI_EXTENDED_ACTIVITY_SAMPLE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HuamiExtendedActivitySample entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getTimestamp());
        stmt.bindLong(2, entity.getDeviceId());
        stmt.bindLong(3, entity.getUserId());
        stmt.bindLong(4, entity.getRawIntensity());
        stmt.bindLong(5, entity.getSteps());
        stmt.bindLong(6, entity.getRawKind());
        stmt.bindLong(7, entity.getHeartRate());
 
        Integer unknown1 = entity.getUnknown1();
        if (unknown1 != null) {
            stmt.bindLong(8, unknown1);
        }
 
        Integer sleep = entity.getSleep();
        if (sleep != null) {
            stmt.bindLong(9, sleep);
        }
 
        Integer deepSleep = entity.getDeepSleep();
        if (deepSleep != null) {
            stmt.bindLong(10, deepSleep);
        }
 
        Integer remSleep = entity.getRemSleep();
        if (remSleep != null) {
            stmt.bindLong(11, remSleep);
        }
    }

    @Override
    protected void attachEntity(HuamiExtendedActivitySample entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public HuamiExtendedActivitySample readEntity(Cursor cursor, int offset) {
        HuamiExtendedActivitySample entity = new HuamiExtendedActivitySample( //
            cursor.getInt(offset + 0), // timestamp
            cursor.getLong(offset + 1), // deviceId
            cursor.getLong(offset + 2), // userId
            cursor.getInt(offset + 3), // rawIntensity
            cursor.getInt(offset + 4), // steps
            cursor.getInt(offset + 5), // rawKind
            cursor.getInt(offset + 6), // heartRate
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // unknown1
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // sleep
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // deepSleep
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10) // remSleep
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HuamiExtendedActivitySample entity, int offset) {
        entity.setTimestamp(cursor.getInt(offset + 0));
        entity.setDeviceId(cursor.getLong(offset + 1));
        entity.setUserId(cursor.getLong(offset + 2));
        entity.setRawIntensity(cursor.getInt(offset + 3));
        entity.setSteps(cursor.getInt(offset + 4));
        entity.setRawKind(cursor.getInt(offset + 5));
        entity.setHeartRate(cursor.getInt(offset + 6));
        entity.setUnknown1(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setSleep(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setDeepSleep(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setRemSleep(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(HuamiExtendedActivitySample entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(HuamiExtendedActivitySample entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDeviceDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM HUAMI_EXTENDED_ACTIVITY_SAMPLE T");
            builder.append(" LEFT JOIN DEVICE T0 ON T.\"DEVICE_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN USER T1 ON T.\"USER_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected HuamiExtendedActivitySample loadCurrentDeep(Cursor cursor, boolean lock) {
        HuamiExtendedActivitySample entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Device device = loadCurrentOther(daoSession.getDeviceDao(), cursor, offset);
         if(device != null) {
            entity.setDevice(device);
        }
        offset += daoSession.getDeviceDao().getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
         if(user != null) {
            entity.setUser(user);
        }

        return entity;    
    }

    public HuamiExtendedActivitySample loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<HuamiExtendedActivitySample> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<HuamiExtendedActivitySample> list = new ArrayList<HuamiExtendedActivitySample>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<HuamiExtendedActivitySample> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<HuamiExtendedActivitySample> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}