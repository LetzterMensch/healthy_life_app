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
import com.example.gr.model.Exercise;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ExerciseDAO_Impl implements ExerciseDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Exercise> __insertionAdapterOfExercise;

  private final EntityInsertionAdapter<Exercise> __insertionAdapterOfExercise_1;

  private final EntityDeletionOrUpdateAdapter<Exercise> __deletionAdapterOfExercise;

  private final EntityDeletionOrUpdateAdapter<Exercise> __updateAdapterOfExercise;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllExercise;

  public ExerciseDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExercise = new EntityInsertionAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise` (`id`,`name`,`met`,`category`,`defaultDuration`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Exercise entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getMet());
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        statement.bindLong(5, entity.getDefaultDuration());
      }
    };
    this.__insertionAdapterOfExercise_1 = new EntityInsertionAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exercise` (`id`,`name`,`met`,`category`,`defaultDuration`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Exercise entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getMet());
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        statement.bindLong(5, entity.getDefaultDuration());
      }
    };
    this.__deletionAdapterOfExercise = new EntityDeletionOrUpdateAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Exercise entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExercise = new EntityDeletionOrUpdateAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exercise` SET `id` = ?,`name` = ?,`met` = ?,`category` = ?,`defaultDuration` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Exercise entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindDouble(3, entity.getMet());
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        statement.bindLong(5, entity.getDefaultDuration());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllExercise = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from exercise";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<Exercise> exerciseList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfExercise.insert(exerciseList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertExercise(final Exercise exercise) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfExercise_1.insert(exercise);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteExercise(final Exercise exercise) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfExercise.handle(exercise);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateExercise(final Exercise exercise) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfExercise.handle(exercise);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllExercise() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllExercise.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllExercise.release(_stmt);
    }
  }

  @Override
  public List<Exercise> getAllExercise() {
    final String _sql = "SELECT * FROM exercise LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMet = CursorUtil.getColumnIndexOrThrow(_cursor, "met");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfDefaultDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultDuration");
      final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Exercise _item;
        _item = new Exercise();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final float _tmpMet;
        _tmpMet = _cursor.getFloat(_cursorIndexOfMet);
        _item.setMet(_tmpMet);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final int _tmpDefaultDuration;
        _tmpDefaultDuration = _cursor.getInt(_cursorIndexOfDefaultDuration);
        _item.setDefaultDuration(_tmpDefaultDuration);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Exercise> findExerciseByName(final String searchSeq) {
    final String _sql = "SELECT * FROM exercise WHERE name LIKE ?";
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
      final int _cursorIndexOfMet = CursorUtil.getColumnIndexOrThrow(_cursor, "met");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfDefaultDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultDuration");
      final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Exercise _item;
        _item = new Exercise();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final float _tmpMet;
        _tmpMet = _cursor.getFloat(_cursorIndexOfMet);
        _item.setMet(_tmpMet);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final int _tmpDefaultDuration;
        _tmpDefaultDuration = _cursor.getInt(_cursorIndexOfDefaultDuration);
        _item.setDefaultDuration(_tmpDefaultDuration);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Exercise getExerciseById(final int id) {
    final String _sql = "SELECT * FROM exercise WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMet = CursorUtil.getColumnIndexOrThrow(_cursor, "met");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfDefaultDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultDuration");
      final Exercise _result;
      if (_cursor.moveToFirst()) {
        _result = new Exercise();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final float _tmpMet;
        _tmpMet = _cursor.getFloat(_cursorIndexOfMet);
        _result.setMet(_tmpMet);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _result.setCategory(_tmpCategory);
        final int _tmpDefaultDuration;
        _tmpDefaultDuration = _cursor.getInt(_cursorIndexOfDefaultDuration);
        _result.setDefaultDuration(_tmpDefaultDuration);
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
