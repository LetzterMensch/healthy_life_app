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

  private final EntityInsertionAdapter<Food> __insertionAdapterOfFood_1;

  private final EntityDeletionOrUpdateAdapter<Food> __deletionAdapterOfFood;

  private final EntityDeletionOrUpdateAdapter<Food> __updateAdapterOfFood;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllFood;

  public FoodDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFood = new EntityInsertionAdapter<Food>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `food` (`id`,`name`,`numberOfServings`,`servingSize`,`calories`,`carb`,`protein`,`fat`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getNumberOfServings());
        statement.bindDouble(4, entity.getServingSize());
        statement.bindLong(5, entity.getCalories());
        statement.bindDouble(6, entity.getCarb());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getFat());
      }
    };
    this.__insertionAdapterOfFood_1 = new EntityInsertionAdapter<Food>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `food` (`id`,`name`,`numberOfServings`,`servingSize`,`calories`,`carb`,`protein`,`fat`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getNumberOfServings());
        statement.bindDouble(4, entity.getServingSize());
        statement.bindLong(5, entity.getCalories());
        statement.bindDouble(6, entity.getCarb());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getFat());
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
        return "UPDATE OR ABORT `food` SET `id` = ?,`name` = ?,`numberOfServings` = ?,`servingSize` = ?,`calories` = ?,`carb` = ?,`protein` = ?,`fat` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getNumberOfServings());
        statement.bindDouble(4, entity.getServingSize());
        statement.bindLong(5, entity.getCalories());
        statement.bindDouble(6, entity.getCarb());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getFat());
        statement.bindLong(9, entity.getId());
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
  public void insertFood(final Food food) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFood_1.insert(food);
      __db.setTransactionSuccessful();
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
  public List<Food> getAllFood() {
    final String _sql = "SELECT * FROM food LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
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
        _item = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _item.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _item.setServingSize(_tmpServingSize);
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
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
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
        _item = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _item.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _item.setServingSize(_tmpServingSize);
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
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfServingSize = CursorUtil.getColumnIndexOrThrow(_cursor, "servingSize");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "carb");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
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
        _result = new Food(_tmpName,_tmpCalories,_tmpProtein,_tmpFat,_tmpCarb);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final float _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getFloat(_cursorIndexOfNumberOfServings);
        _result.setNumberOfServings(_tmpNumberOfServings);
        final float _tmpServingSize;
        _tmpServingSize = _cursor.getFloat(_cursorIndexOfServingSize);
        _result.setServingSize(_tmpServingSize);
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
