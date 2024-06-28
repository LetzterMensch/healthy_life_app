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
import com.example.gr.model.RecordedWorkout;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RecordedWorkoutDAO_Impl implements RecordedWorkoutDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecordedWorkout> __insertionAdapterOfRecordedWorkout;

  private final EntityDeletionOrUpdateAdapter<RecordedWorkout> __deletionAdapterOfRecordedWorkout;

  private final EntityDeletionOrUpdateAdapter<RecordedWorkout> __updateAdapterOfRecordedWorkout;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecordedWorkout;

  public RecordedWorkoutDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecordedWorkout = new EntityInsertionAdapter<RecordedWorkout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `recordedworkout` (`id`,`name`,`baseActivitySummaryId`,`createdAt`,`activityKind`,`endTime`,`duration`,`avgHeartRate`,`minHeartRate`,`maxHeartRate`,`caloriesBurnt`,`distance`,`timeStamp`,`diaryID`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RecordedWorkout entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getBaseActivitySummaryId());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCreatedAt());
        }
        statement.bindLong(5, entity.getActivityKind());
        statement.bindLong(6, entity.getEndTime());
        statement.bindLong(7, entity.getDuration());
        statement.bindLong(8, entity.getAvgHeartRate());
        statement.bindLong(9, entity.getMinHeartRate());
        statement.bindLong(10, entity.getMaxHeartRate());
        statement.bindLong(11, entity.getCaloriesBurnt());
        statement.bindDouble(12, entity.getDistance());
        statement.bindLong(13, entity.getTimeStamp());
        statement.bindLong(14, entity.getDiaryID());
      }
    };
    this.__deletionAdapterOfRecordedWorkout = new EntityDeletionOrUpdateAdapter<RecordedWorkout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recordedworkout` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RecordedWorkout entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecordedWorkout = new EntityDeletionOrUpdateAdapter<RecordedWorkout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recordedworkout` SET `id` = ?,`name` = ?,`baseActivitySummaryId` = ?,`createdAt` = ?,`activityKind` = ?,`endTime` = ?,`duration` = ?,`avgHeartRate` = ?,`minHeartRate` = ?,`maxHeartRate` = ?,`caloriesBurnt` = ?,`distance` = ?,`timeStamp` = ?,`diaryID` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RecordedWorkout entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getBaseActivitySummaryId());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCreatedAt());
        }
        statement.bindLong(5, entity.getActivityKind());
        statement.bindLong(6, entity.getEndTime());
        statement.bindLong(7, entity.getDuration());
        statement.bindLong(8, entity.getAvgHeartRate());
        statement.bindLong(9, entity.getMinHeartRate());
        statement.bindLong(10, entity.getMaxHeartRate());
        statement.bindLong(11, entity.getCaloriesBurnt());
        statement.bindDouble(12, entity.getDistance());
        statement.bindLong(13, entity.getTimeStamp());
        statement.bindLong(14, entity.getDiaryID());
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllRecordedWorkout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from recordedWorkout";
        return _query;
      }
    };
  }

  @Override
  public void insertRecordedWorkout(final RecordedWorkout recordedWorkout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecordedWorkout.insert(recordedWorkout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRecordedWorkout(final RecordedWorkout recordedWorkout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRecordedWorkout.handle(recordedWorkout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRecordedWorkout(final RecordedWorkout recordedWorkout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRecordedWorkout.handle(recordedWorkout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllRecordedWorkout() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecordedWorkout.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllRecordedWorkout.release(_stmt);
    }
  }

  @Override
  public List<RecordedWorkout> getAllRecordedWorkout() {
    final String _sql = "SELECT * FROM recordedworkout LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBaseActivitySummaryId = CursorUtil.getColumnIndexOrThrow(_cursor, "baseActivitySummaryId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActivityKind = CursorUtil.getColumnIndexOrThrow(_cursor, "activityKind");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
      final int _cursorIndexOfMinHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "minHeartRate");
      final int _cursorIndexOfMaxHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHeartRate");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfDiaryID = CursorUtil.getColumnIndexOrThrow(_cursor, "diaryID");
      final List<RecordedWorkout> _result = new ArrayList<RecordedWorkout>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RecordedWorkout _item;
        _item = new RecordedWorkout();
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
        final long _tmpBaseActivitySummaryId;
        _tmpBaseActivitySummaryId = _cursor.getLong(_cursorIndexOfBaseActivitySummaryId);
        _item.setBaseActivitySummaryId(_tmpBaseActivitySummaryId);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _item.setCreatedAt(_tmpCreatedAt);
        final int _tmpActivityKind;
        _tmpActivityKind = _cursor.getInt(_cursorIndexOfActivityKind);
        _item.setActivityKind(_tmpActivityKind);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpAvgHeartRate;
        _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
        _item.setAvgHeartRate(_tmpAvgHeartRate);
        final int _tmpMinHeartRate;
        _tmpMinHeartRate = _cursor.getInt(_cursorIndexOfMinHeartRate);
        _item.setMinHeartRate(_tmpMinHeartRate);
        final int _tmpMaxHeartRate;
        _tmpMaxHeartRate = _cursor.getInt(_cursorIndexOfMaxHeartRate);
        _item.setMaxHeartRate(_tmpMaxHeartRate);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _item.setCaloriesBurnt(_tmpCaloriesBurnt);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _item.setDistance(_tmpDistance);
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        _item.setTimeStamp(_tmpTimeStamp);
        final int _tmpDiaryID;
        _tmpDiaryID = _cursor.getInt(_cursorIndexOfDiaryID);
        _item.setDiaryID(_tmpDiaryID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RecordedWorkout> findRecordedWorkoutWithTimeStamp(final long timestamp) {
    final String _sql = "SELECT * FROM recordedworkout WHERE timeStamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, timestamp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBaseActivitySummaryId = CursorUtil.getColumnIndexOrThrow(_cursor, "baseActivitySummaryId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActivityKind = CursorUtil.getColumnIndexOrThrow(_cursor, "activityKind");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
      final int _cursorIndexOfMinHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "minHeartRate");
      final int _cursorIndexOfMaxHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHeartRate");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfDiaryID = CursorUtil.getColumnIndexOrThrow(_cursor, "diaryID");
      final List<RecordedWorkout> _result = new ArrayList<RecordedWorkout>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RecordedWorkout _item;
        _item = new RecordedWorkout();
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
        final long _tmpBaseActivitySummaryId;
        _tmpBaseActivitySummaryId = _cursor.getLong(_cursorIndexOfBaseActivitySummaryId);
        _item.setBaseActivitySummaryId(_tmpBaseActivitySummaryId);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _item.setCreatedAt(_tmpCreatedAt);
        final int _tmpActivityKind;
        _tmpActivityKind = _cursor.getInt(_cursorIndexOfActivityKind);
        _item.setActivityKind(_tmpActivityKind);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpAvgHeartRate;
        _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
        _item.setAvgHeartRate(_tmpAvgHeartRate);
        final int _tmpMinHeartRate;
        _tmpMinHeartRate = _cursor.getInt(_cursorIndexOfMinHeartRate);
        _item.setMinHeartRate(_tmpMinHeartRate);
        final int _tmpMaxHeartRate;
        _tmpMaxHeartRate = _cursor.getInt(_cursorIndexOfMaxHeartRate);
        _item.setMaxHeartRate(_tmpMaxHeartRate);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _item.setCaloriesBurnt(_tmpCaloriesBurnt);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _item.setDistance(_tmpDistance);
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        _item.setTimeStamp(_tmpTimeStamp);
        final int _tmpDiaryID;
        _tmpDiaryID = _cursor.getInt(_cursorIndexOfDiaryID);
        _item.setDiaryID(_tmpDiaryID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RecordedWorkout> findRecordedWorkoutByDate(final String dateTime) {
    final String _sql = "SELECT * FROM recordedworkout WHERE createdAt LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dateTime == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, dateTime);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBaseActivitySummaryId = CursorUtil.getColumnIndexOrThrow(_cursor, "baseActivitySummaryId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActivityKind = CursorUtil.getColumnIndexOrThrow(_cursor, "activityKind");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
      final int _cursorIndexOfMinHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "minHeartRate");
      final int _cursorIndexOfMaxHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHeartRate");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfDiaryID = CursorUtil.getColumnIndexOrThrow(_cursor, "diaryID");
      final List<RecordedWorkout> _result = new ArrayList<RecordedWorkout>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RecordedWorkout _item;
        _item = new RecordedWorkout();
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
        final long _tmpBaseActivitySummaryId;
        _tmpBaseActivitySummaryId = _cursor.getLong(_cursorIndexOfBaseActivitySummaryId);
        _item.setBaseActivitySummaryId(_tmpBaseActivitySummaryId);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _item.setCreatedAt(_tmpCreatedAt);
        final int _tmpActivityKind;
        _tmpActivityKind = _cursor.getInt(_cursorIndexOfActivityKind);
        _item.setActivityKind(_tmpActivityKind);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpAvgHeartRate;
        _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
        _item.setAvgHeartRate(_tmpAvgHeartRate);
        final int _tmpMinHeartRate;
        _tmpMinHeartRate = _cursor.getInt(_cursorIndexOfMinHeartRate);
        _item.setMinHeartRate(_tmpMinHeartRate);
        final int _tmpMaxHeartRate;
        _tmpMaxHeartRate = _cursor.getInt(_cursorIndexOfMaxHeartRate);
        _item.setMaxHeartRate(_tmpMaxHeartRate);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _item.setCaloriesBurnt(_tmpCaloriesBurnt);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _item.setDistance(_tmpDistance);
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        _item.setTimeStamp(_tmpTimeStamp);
        final int _tmpDiaryID;
        _tmpDiaryID = _cursor.getInt(_cursorIndexOfDiaryID);
        _item.setDiaryID(_tmpDiaryID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public RecordedWorkout getRecordedWorkoutById(final int id) {
    final String _sql = "SELECT * FROM recordedworkout WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBaseActivitySummaryId = CursorUtil.getColumnIndexOrThrow(_cursor, "baseActivitySummaryId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActivityKind = CursorUtil.getColumnIndexOrThrow(_cursor, "activityKind");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
      final int _cursorIndexOfMinHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "minHeartRate");
      final int _cursorIndexOfMaxHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHeartRate");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfDiaryID = CursorUtil.getColumnIndexOrThrow(_cursor, "diaryID");
      final RecordedWorkout _result;
      if (_cursor.moveToFirst()) {
        _result = new RecordedWorkout();
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
        final long _tmpBaseActivitySummaryId;
        _tmpBaseActivitySummaryId = _cursor.getLong(_cursorIndexOfBaseActivitySummaryId);
        _result.setBaseActivitySummaryId(_tmpBaseActivitySummaryId);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _result.setCreatedAt(_tmpCreatedAt);
        final int _tmpActivityKind;
        _tmpActivityKind = _cursor.getInt(_cursorIndexOfActivityKind);
        _result.setActivityKind(_tmpActivityKind);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _result.setEndTime(_tmpEndTime);
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpAvgHeartRate;
        _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
        _result.setAvgHeartRate(_tmpAvgHeartRate);
        final int _tmpMinHeartRate;
        _tmpMinHeartRate = _cursor.getInt(_cursorIndexOfMinHeartRate);
        _result.setMinHeartRate(_tmpMinHeartRate);
        final int _tmpMaxHeartRate;
        _tmpMaxHeartRate = _cursor.getInt(_cursorIndexOfMaxHeartRate);
        _result.setMaxHeartRate(_tmpMaxHeartRate);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _result.setCaloriesBurnt(_tmpCaloriesBurnt);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _result.setDistance(_tmpDistance);
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        _result.setTimeStamp(_tmpTimeStamp);
        final int _tmpDiaryID;
        _tmpDiaryID = _cursor.getInt(_cursorIndexOfDiaryID);
        _result.setDiaryID(_tmpDiaryID);
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
  public RecordedWorkout getRecordedWorkoutByTimeStamp(final long timestamp) {
    final String _sql = "SELECT * FROM recordedworkout WHERE timeStamp=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, timestamp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfBaseActivitySummaryId = CursorUtil.getColumnIndexOrThrow(_cursor, "baseActivitySummaryId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActivityKind = CursorUtil.getColumnIndexOrThrow(_cursor, "activityKind");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfAvgHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "avgHeartRate");
      final int _cursorIndexOfMinHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "minHeartRate");
      final int _cursorIndexOfMaxHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHeartRate");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
      final int _cursorIndexOfDiaryID = CursorUtil.getColumnIndexOrThrow(_cursor, "diaryID");
      final RecordedWorkout _result;
      if (_cursor.moveToFirst()) {
        _result = new RecordedWorkout();
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
        final long _tmpBaseActivitySummaryId;
        _tmpBaseActivitySummaryId = _cursor.getLong(_cursorIndexOfBaseActivitySummaryId);
        _result.setBaseActivitySummaryId(_tmpBaseActivitySummaryId);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _result.setCreatedAt(_tmpCreatedAt);
        final int _tmpActivityKind;
        _tmpActivityKind = _cursor.getInt(_cursorIndexOfActivityKind);
        _result.setActivityKind(_tmpActivityKind);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _result.setEndTime(_tmpEndTime);
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpAvgHeartRate;
        _tmpAvgHeartRate = _cursor.getInt(_cursorIndexOfAvgHeartRate);
        _result.setAvgHeartRate(_tmpAvgHeartRate);
        final int _tmpMinHeartRate;
        _tmpMinHeartRate = _cursor.getInt(_cursorIndexOfMinHeartRate);
        _result.setMinHeartRate(_tmpMinHeartRate);
        final int _tmpMaxHeartRate;
        _tmpMaxHeartRate = _cursor.getInt(_cursorIndexOfMaxHeartRate);
        _result.setMaxHeartRate(_tmpMaxHeartRate);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _result.setCaloriesBurnt(_tmpCaloriesBurnt);
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        _result.setDistance(_tmpDistance);
        final long _tmpTimeStamp;
        _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
        _result.setTimeStamp(_tmpTimeStamp);
        final int _tmpDiaryID;
        _tmpDiaryID = _cursor.getInt(_cursorIndexOfDiaryID);
        _result.setDiaryID(_tmpDiaryID);
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
