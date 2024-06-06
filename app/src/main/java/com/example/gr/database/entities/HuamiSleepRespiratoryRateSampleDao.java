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
 * DAO for table "HUAMI_SLEEP_RESPIRATORY_RATE_SAMPLE".
*/
public class HuamiSleepRespiratoryRateSampleDao extends AbstractDao<HuamiSleepRespiratoryRateSample, Void> {

    public static final String TABLENAME = "HUAMI_SLEEP_RESPIRATORY_RATE_SAMPLE";

    /**
     * Properties of entity HuamiSleepRespiratoryRateSample.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Timestamp = new Property(0, long.class, "timestamp", true, "TIMESTAMP");
        public final static Property DeviceId = new Property(1, long.class, "deviceId", true, "DEVICE_ID");
        public final static Property UserId = new Property(2, long.class, "userId", false, "USER_ID");
        public final static Property UtcOffset = new Property(3, int.class, "utcOffset", false, "UTC_OFFSET");
        public final static Property Rate = new Property(4, int.class, "rate", false, "RATE");
    };

    private DaoSession daoSession;


    public HuamiSleepRespiratoryRateSampleDao(DaoConfig config) {
        super(config);
    }
    
    public HuamiSleepRespiratoryRateSampleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HUAMI_SLEEP_RESPIRATORY_RATE_SAMPLE\" (" + //
                "\"TIMESTAMP\" INTEGER  NOT NULL ," + // 0: timestamp
                "\"DEVICE_ID\" INTEGER  NOT NULL ," + // 1: deviceId
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: userId
                "\"UTC_OFFSET\" INTEGER NOT NULL ," + // 3: utcOffset
                "\"RATE\" INTEGER NOT NULL ," + // 4: rate
                "PRIMARY KEY (" +
                "\"TIMESTAMP\" ," +
                "\"DEVICE_ID\" ) ON CONFLICT REPLACE)" + ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? " WITHOUT ROWID;" : ";")
        );
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HUAMI_SLEEP_RESPIRATORY_RATE_SAMPLE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HuamiSleepRespiratoryRateSample entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getTimestamp());
        stmt.bindLong(2, entity.getDeviceId());
        stmt.bindLong(3, entity.getUserId());
        stmt.bindLong(4, entity.getUtcOffset());
        stmt.bindLong(5, entity.getRate());
    }

    @Override
    protected void attachEntity(HuamiSleepRespiratoryRateSample entity) {
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
    public HuamiSleepRespiratoryRateSample readEntity(Cursor cursor, int offset) {
        HuamiSleepRespiratoryRateSample entity = new HuamiSleepRespiratoryRateSample( //
            cursor.getLong(offset + 0), // timestamp
            cursor.getLong(offset + 1), // deviceId
            cursor.getLong(offset + 2), // userId
            cursor.getInt(offset + 3), // utcOffset
            cursor.getInt(offset + 4) // rate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HuamiSleepRespiratoryRateSample entity, int offset) {
        entity.setTimestamp(cursor.getLong(offset + 0));
        entity.setDeviceId(cursor.getLong(offset + 1));
        entity.setUserId(cursor.getLong(offset + 2));
        entity.setUtcOffset(cursor.getInt(offset + 3));
        entity.setRate(cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(HuamiSleepRespiratoryRateSample entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(HuamiSleepRespiratoryRateSample entity) {
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
            builder.append(" FROM HUAMI_SLEEP_RESPIRATORY_RATE_SAMPLE T");
            builder.append(" LEFT JOIN DEVICE T0 ON T.\"DEVICE_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN USER T1 ON T.\"USER_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected HuamiSleepRespiratoryRateSample loadCurrentDeep(Cursor cursor, boolean lock) {
        HuamiSleepRespiratoryRateSample entity = loadCurrent(cursor, 0, lock);
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

    public HuamiSleepRespiratoryRateSample loadDeep(Long key) {
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
    public List<HuamiSleepRespiratoryRateSample> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<HuamiSleepRespiratoryRateSample> list = new ArrayList<HuamiSleepRespiratoryRateSample>(count);
        
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
    
    protected List<HuamiSleepRespiratoryRateSample> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<HuamiSleepRespiratoryRateSample> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}