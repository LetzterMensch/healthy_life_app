package com.example.gr.model.database.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TAG".
*/
public class TagDao extends AbstractDao<Tag, Long> {

    public static final String TABLENAME = "TAG";

    /**
     * Properties of entity Tag.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property UserId = new Property(3, long.class, "userId", false, "USER_ID");
    };

    private Query<Tag> activityDescription_TagListQuery;

    public TagDao(DaoConfig config) {
        super(config);
    }
    
    public TagDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TAG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"DESCRIPTION\" TEXT," + // 2: description
                "\"USER_ID\" INTEGER NOT NULL );"); // 3: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TAG\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Tag entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
        stmt.bindLong(4, entity.getUserId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Tag readEntity(Cursor cursor, int offset) {
        Tag entity = new Tag( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.getLong(offset + 3) // userId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Tag entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Tag entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Tag entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "tagList" to-many relationship of ActivityDescription. */
    public List<Tag> _queryActivityDescription_TagList(long activityDescriptionId) {
        synchronized (this) {
            if (activityDescription_TagListQuery == null) {
                QueryBuilder<Tag> queryBuilder = queryBuilder();
                queryBuilder.join(ActivityDescTagLink.class, ActivityDescTagLinkDao.Properties.TagId)
                    .where(ActivityDescTagLinkDao.Properties.ActivityDescriptionId.eq(activityDescriptionId));
                activityDescription_TagListQuery = queryBuilder.build();
            }
        }
        Query<Tag> query = activityDescription_TagListQuery.forCurrentThread();
        query.setParameter(0, activityDescriptionId);
        return query.list();
    }

}