package com.example.gr.database.dao;

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
import com.example.gr.model.FoodLog;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FoodLogDAO_Impl implements FoodLogDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FoodLog> __insertionAdapterOfFoodLog;

  private final EntityDeletionOrUpdateAdapter<FoodLog> __deletionAdapterOfFoodLog;

  private final EntityDeletionOrUpdateAdapter<FoodLog> __updateAdapterOfFoodLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllFoodLog;

  public FoodLogDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFoodLog = new EntityInsertionAdapter<FoodLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `foodlog` (`id`,`totalCarb`,`totalProtein`,`totalFat`,`totalCalories`,`meal`,`numberOfServings`,`foodId`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final FoodLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getTotalCarb());
        statement.bindDouble(3, entity.getTotalProtein());
        statement.bindDouble(4, entity.getTotalFat());
        statement.bindLong(5, entity.getTotalCalories());
        statement.bindLong(6, entity.getMeal());
        statement.bindLong(7, entity.getNumberOfServings());
        statement.bindLong(8, entity.getFoodId());
      }
    };
    this.__deletionAdapterOfFoodLog = new EntityDeletionOrUpdateAdapter<FoodLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `foodlog` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final FoodLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFoodLog = new EntityDeletionOrUpdateAdapter<FoodLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `foodlog` SET `id` = ?,`totalCarb` = ?,`totalProtein` = ?,`totalFat` = ?,`totalCalories` = ?,`meal` = ?,`numberOfServings` = ?,`foodId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final FoodLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getTotalCarb());
        statement.bindDouble(3, entity.getTotalProtein());
        statement.bindDouble(4, entity.getTotalFat());
        statement.bindLong(5, entity.getTotalCalories());
        statement.bindLong(6, entity.getMeal());
        statement.bindLong(7, entity.getNumberOfServings());
        statement.bindLong(8, entity.getFoodId());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllFoodLog = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from foodlog";
        return _query;
      }
    };
  }

  @Override
  public void insertFoodLog(final FoodLog foodlog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFoodLog.insert(foodlog);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFoodLog(final FoodLog foodlog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFoodLog.handle(foodlog);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateFoodLog(final FoodLog foodlog) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFoodLog.handle(foodlog);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllFoodLog() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllFoodLog.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllFoodLog.release(_stmt);
    }
  }

  @Override
  public List<FoodLog> getAllFoodLog() {
    final String _sql = "SELECT * FROM foodlog LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTotalCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCarb");
      final int _cursorIndexOfTotalProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "totalProtein");
      final int _cursorIndexOfTotalFat = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFat");
      final int _cursorIndexOfTotalCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCalories");
      final int _cursorIndexOfMeal = CursorUtil.getColumnIndexOrThrow(_cursor, "meal");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfFoodId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodId");
      final List<FoodLog> _result = new ArrayList<FoodLog>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final FoodLog _item;
        _item = new FoodLog();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final float _tmpTotalCarb;
        _tmpTotalCarb = _cursor.getFloat(_cursorIndexOfTotalCarb);
        _item.setTotalCarb(_tmpTotalCarb);
        final float _tmpTotalProtein;
        _tmpTotalProtein = _cursor.getFloat(_cursorIndexOfTotalProtein);
        _item.setTotalProtein(_tmpTotalProtein);
        final float _tmpTotalFat;
        _tmpTotalFat = _cursor.getFloat(_cursorIndexOfTotalFat);
        _item.setTotalFat(_tmpTotalFat);
        final int _tmpTotalCalories;
        _tmpTotalCalories = _cursor.getInt(_cursorIndexOfTotalCalories);
        _item.setTotalCalories(_tmpTotalCalories);
        final int _tmpMeal;
        _tmpMeal = _cursor.getInt(_cursorIndexOfMeal);
        _item.setMeal(_tmpMeal);
        final int _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getInt(_cursorIndexOfNumberOfServings);
        _item.setNumberOfServings(_tmpNumberOfServings);
        final int _tmpFoodId;
        _tmpFoodId = _cursor.getInt(_cursorIndexOfFoodId);
        _item.setFoodId(_tmpFoodId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public FoodLog getFoodLogById(final int id) {
    final String _sql = "SELECT * FROM foodlog WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTotalCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCarb");
      final int _cursorIndexOfTotalProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "totalProtein");
      final int _cursorIndexOfTotalFat = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFat");
      final int _cursorIndexOfTotalCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCalories");
      final int _cursorIndexOfMeal = CursorUtil.getColumnIndexOrThrow(_cursor, "meal");
      final int _cursorIndexOfNumberOfServings = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfServings");
      final int _cursorIndexOfFoodId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodId");
      final FoodLog _result;
      if (_cursor.moveToFirst()) {
        _result = new FoodLog();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final float _tmpTotalCarb;
        _tmpTotalCarb = _cursor.getFloat(_cursorIndexOfTotalCarb);
        _result.setTotalCarb(_tmpTotalCarb);
        final float _tmpTotalProtein;
        _tmpTotalProtein = _cursor.getFloat(_cursorIndexOfTotalProtein);
        _result.setTotalProtein(_tmpTotalProtein);
        final float _tmpTotalFat;
        _tmpTotalFat = _cursor.getFloat(_cursorIndexOfTotalFat);
        _result.setTotalFat(_tmpTotalFat);
        final int _tmpTotalCalories;
        _tmpTotalCalories = _cursor.getInt(_cursorIndexOfTotalCalories);
        _result.setTotalCalories(_tmpTotalCalories);
        final int _tmpMeal;
        _tmpMeal = _cursor.getInt(_cursorIndexOfMeal);
        _result.setMeal(_tmpMeal);
        final int _tmpNumberOfServings;
        _tmpNumberOfServings = _cursor.getInt(_cursorIndexOfNumberOfServings);
        _result.setNumberOfServings(_tmpNumberOfServings);
        final int _tmpFoodId;
        _tmpFoodId = _cursor.getInt(_cursorIndexOfFoodId);
        _result.setFoodId(_tmpFoodId);
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
