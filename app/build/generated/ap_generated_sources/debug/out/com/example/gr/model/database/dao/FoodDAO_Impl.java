package com.example.gr.model.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gr.model.Food;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FoodDAO_Impl implements FoodDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Food> __insertionAdapterOfFood;

  private final EntityDeletionOrUpdateAdapter<Food> __deletionAdapterOfFood;

  private final EntityDeletionOrUpdateAdapter<Food> __updateAdapterOfFood;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllFood;

  public FoodDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFood = new EntityInsertionAdapter<Food>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `food` (`id`,`uuid`,`name`,`numberOfServings`,`servingSize`,`calories`,`carb`,`protein`,`fat`,`isCustomized`,`subFoodIds`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUuid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUuid());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        statement.bindDouble(4, entity.getNumberOfServings());
        statement.bindDouble(5, entity.getServingSize());
        statement.bindLong(6, entity.getCalories());
        statement.bindDouble(7, entity.getCarb());
        statement.bindDouble(8, entity.getProtein());
        statement.bindDouble(9, entity.getFat());
        final int _tmp = entity.isCustomized() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getSubFoodIds() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSubFoodIds());
        }
        statement.bindLong(12, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfFood = new EntityDeletionOrUpdateAdapter<Food>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `food` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFood = new EntityDeletionOrUpdateAdapter<Food>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `food` SET `id` = ?,`uuid` = ?,`name` = ?,`numberOfServings` = ?,`servingSize` = ?,`calories` = ?,`carb` = ?,`protein` = ?,`fat` = ?,`isCustomized` = ?,`subFoodIds` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUuid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUuid());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        statement.bindDouble(4, entity.getNumberOfServings());
        statement.bindDouble(5, entity.getServingSize());
        statement.bindLong(6, entity.getCalories());
        statement.bindDouble(7, entity.getCarb());
        statement.bindDouble(8, entity.getProtein());
        statement.bindDouble(9, entity.getFat());
        final int _tmp = entity.isCustomized() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getSubFoodIds() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSubFoodIds());
        }
        statement.bindLong(12, entity.getTimestamp());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllFood = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from food";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<Food> foodList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFood.insert(foodList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertFood(final Food food) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfFood.insertAndReturnId(food);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFood(final Food food) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFood.handle(food);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateFood(final Food food) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFood.handle(food);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllFood() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllFood.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllFood.release(_stmt);
    }
  }

  @Override
  public Food getFoodByTimestamp(final long timestamp) {
    final String _sql = "Select * from food where timestamp = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, timestamp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfIsCustomized = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustomized");
      final int _cursorIndexOfSubFoodIds = CursorUtil.getColumnIndexOrThrow(_cursor, "subFoodIds");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Food _result;
      if (_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        final float _tmpCarb;
        _tmpCarb = _cursor.getFloat(_cursorIndexOfCarb);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        final String _tmpSubFoodIds;
        if (_cursor.isNull(_cursorIndexOfSubFoodIds)) {
          _tmpSubFoodIds = null;
        } else {
          _tmpSubFoodIds = _cursor.getString(_cursorIndexOfSubFoodIds);
        }
        _result = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb,_tmpSubFoodIds);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _result.setUuid(_tmpUuid);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _result.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _result.setServingSize(_tmpServingSize);
        final boolean _tmpIsCustomized;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCustomized);
        _tmpIsCustomized = _tmp != 0;
        _result.setCustomized(_tmpIsCustomized);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Food> getAllFood() {
    final String _sql = "SELECT * FROM food";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfIsCustomized = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustomized");
      final int _cursorIndexOfSubFoodIds = CursorUtil.getColumnIndexOrThrow(_cursor, "subFoodIds");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Food> _result = new ArrayList<Food>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Food _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        final float _tmpCarb;
        _tmpCarb = _cursor.getFloat(_cursorIndexOfCarb);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        final String _tmpSubFoodIds;
        if (_cursor.isNull(_cursorIndexOfSubFoodIds)) {
          _tmpSubFoodIds = null;
        } else {
          _tmpSubFoodIds = _cursor.getString(_cursorIndexOfSubFoodIds);
        }
        _item = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb,_tmpSubFoodIds);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _item.setUuid(_tmpUuid);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _item.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _item.setServingSize(_tmpServingSize);
        final boolean _tmpIsCustomized;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCustomized);
        _tmpIsCustomized = _tmp != 0;
        _item.setCustomized(_tmpIsCustomized);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Food> findFoodByName(final String searchSeq) {
    final String _sql = "SELECT * FROM food WHERE name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchSeq == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchSeq);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfIsCustomized = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustomized");
      final int _cursorIndexOfSubFoodIds = CursorUtil.getColumnIndexOrThrow(_cursor, "subFoodIds");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Food> _result = new ArrayList<Food>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Food _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        final float _tmpCarb;
        _tmpCarb = _cursor.getFloat(_cursorIndexOfCarb);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        final String _tmpSubFoodIds;
        if (_cursor.isNull(_cursorIndexOfSubFoodIds)) {
          _tmpSubFoodIds = null;
        } else {
          _tmpSubFoodIds = _cursor.getString(_cursorIndexOfSubFoodIds);
        }
        _item = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb,_tmpSubFoodIds);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _item.setUuid(_tmpUuid);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _item.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _item.setServingSize(_tmpServingSize);
        final boolean _tmpIsCustomized;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCustomized);
        _tmpIsCustomized = _tmp != 0;
        _item.setCustomized(_tmpIsCustomized);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Food getFoodById(final int id) {
    final String _sql = "SELECT * FROM food WHERE id LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfIsCustomized = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustomized");
      final int _cursorIndexOfSubFoodIds = CursorUtil.getColumnIndexOrThrow(_cursor, "subFoodIds");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Food _result;
      if (_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        final float _tmpCarb;
        _tmpCarb = _cursor.getFloat(_cursorIndexOfCarb);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        final String _tmpSubFoodIds;
        if (_cursor.isNull(_cursorIndexOfSubFoodIds)) {
          _tmpSubFoodIds = null;
        } else {
          _tmpSubFoodIds = _cursor.getString(_cursorIndexOfSubFoodIds);
        }
        _result = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb,_tmpSubFoodIds);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _result.setUuid(_tmpUuid);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _result.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _result.setServingSize(_tmpServingSize);
        final boolean _tmpIsCustomized;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCustomized);
        _tmpIsCustomized = _tmp != 0;
        _result.setCustomized(_tmpIsCustomized);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
