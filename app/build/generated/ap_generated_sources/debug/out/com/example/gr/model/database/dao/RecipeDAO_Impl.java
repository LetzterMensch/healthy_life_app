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
import com.example.gr.model.Recipe;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RecipeDAO_Impl implements RecipeDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Recipe> __insertionAdapterOfRecipe;

  private final EntityDeletionOrUpdateAdapter<Recipe> __deletionAdapterOfRecipe;

  private final EntityDeletionOrUpdateAdapter<Recipe> __updateAdapterOfRecipe;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecipe;

  public RecipeDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipe = new EntityInsertionAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recipe` (`id`,`foodID`,`foodUUID`,`name`,`image`,`description`,`carbs`,`protein`,`fat`,`calories`,`ingredients`,`url`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Recipe entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFoodID());
        if (entity.getFoodUUID() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFoodUUID());
        }
        if (entity.getName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getName());
        }
        if (entity.getImage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getImage());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindDouble(7, entity.getCarbs());
        statement.bindDouble(8, entity.getProtein());
        statement.bindDouble(9, entity.getFat());
        statement.bindLong(10, entity.getCalories());
        if (entity.getIngredients() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getIngredients());
        }
        if (entity.getUrl() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getUrl());
        }
        statement.bindLong(13, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfRecipe = new EntityDeletionOrUpdateAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recipe` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Recipe entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecipe = new EntityDeletionOrUpdateAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recipe` SET `id` = ?,`foodID` = ?,`foodUUID` = ?,`name` = ?,`image` = ?,`description` = ?,`carbs` = ?,`protein` = ?,`fat` = ?,`calories` = ?,`ingredients` = ?,`url` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Recipe entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFoodID());
        if (entity.getFoodUUID() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFoodUUID());
        }
        if (entity.getName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getName());
        }
        if (entity.getImage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getImage());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        statement.bindDouble(7, entity.getCarbs());
        statement.bindDouble(8, entity.getProtein());
        statement.bindDouble(9, entity.getFat());
        statement.bindLong(10, entity.getCalories());
        if (entity.getIngredients() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getIngredients());
        }
        if (entity.getUrl() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getUrl());
        }
        statement.bindLong(13, entity.getTimestamp());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllRecipe = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from recipe";
        return _query;
      }
    };
  }

  @Override
  public long insertRecipe(final Recipe recipe) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRecipe.insertAndReturnId(recipe);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRecipe(final Recipe recipe) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRecipe.handle(recipe);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int updateRecipe(final Recipe recipe) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfRecipe.handle(recipe);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllRecipe() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecipe.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllRecipe.release(_stmt);
    }
  }

  @Override
  public List<Recipe> getAllRecipe() {
    final String _sql = "SELECT * FROM recipe LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFoodID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodID");
      final int _cursorIndexOfFoodUUID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodUUID");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Recipe _item;
        _item = new Recipe();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpFoodID;
        _tmpFoodID = _cursor.getInt(_cursorIndexOfFoodID);
        _item.setFoodID(_tmpFoodID);
        final String _tmpFoodUUID;
        if (_cursor.isNull(_cursorIndexOfFoodUUID)) {
          _tmpFoodUUID = null;
        } else {
          _tmpFoodUUID = _cursor.getString(_cursorIndexOfFoodUUID);
        }
        _item.setFoodUUID(_tmpFoodUUID);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        _item.setImage(_tmpImage);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        final float _tmpCarbs;
        _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
        _item.setCarbs(_tmpCarbs);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        _item.setProtein(_tmpProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        _item.setFat(_tmpFat);
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        _item.setCalories(_tmpCalories);
        final String _tmpIngredients;
        if (_cursor.isNull(_cursorIndexOfIngredients)) {
          _tmpIngredients = null;
        } else {
          _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
        }
        _item.setIngredients(_tmpIngredients);
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        _item.setUrl(_tmpUrl);
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
  public Recipe getRecipeByTimestamp(final long timestamp) {
    final String _sql = "Select * from recipe where timestamp = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, timestamp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFoodID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodID");
      final int _cursorIndexOfFoodUUID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodUUID");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Recipe _result;
      if (_cursor.moveToFirst()) {
        _result = new Recipe();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpFoodID;
        _tmpFoodID = _cursor.getInt(_cursorIndexOfFoodID);
        _result.setFoodID(_tmpFoodID);
        final String _tmpFoodUUID;
        if (_cursor.isNull(_cursorIndexOfFoodUUID)) {
          _tmpFoodUUID = null;
        } else {
          _tmpFoodUUID = _cursor.getString(_cursorIndexOfFoodUUID);
        }
        _result.setFoodUUID(_tmpFoodUUID);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        _result.setImage(_tmpImage);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _result.setDescription(_tmpDescription);
        final float _tmpCarbs;
        _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
        _result.setCarbs(_tmpCarbs);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        _result.setProtein(_tmpProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        _result.setFat(_tmpFat);
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        _result.setCalories(_tmpCalories);
        final String _tmpIngredients;
        if (_cursor.isNull(_cursorIndexOfIngredients)) {
          _tmpIngredients = null;
        } else {
          _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
        }
        _result.setIngredients(_tmpIngredients);
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        _result.setUrl(_tmpUrl);
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
  public List<Recipe> findRecipeByName(final String searchSeq) {
    final String _sql = "SELECT * FROM recipe WHERE name LIKE ?";
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
      final int _cursorIndexOfFoodID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodID");
      final int _cursorIndexOfFoodUUID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodUUID");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Recipe _item;
        _item = new Recipe();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpFoodID;
        _tmpFoodID = _cursor.getInt(_cursorIndexOfFoodID);
        _item.setFoodID(_tmpFoodID);
        final String _tmpFoodUUID;
        if (_cursor.isNull(_cursorIndexOfFoodUUID)) {
          _tmpFoodUUID = null;
        } else {
          _tmpFoodUUID = _cursor.getString(_cursorIndexOfFoodUUID);
        }
        _item.setFoodUUID(_tmpFoodUUID);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        _item.setImage(_tmpImage);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        final float _tmpCarbs;
        _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
        _item.setCarbs(_tmpCarbs);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        _item.setProtein(_tmpProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        _item.setFat(_tmpFat);
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        _item.setCalories(_tmpCalories);
        final String _tmpIngredients;
        if (_cursor.isNull(_cursorIndexOfIngredients)) {
          _tmpIngredients = null;
        } else {
          _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
        }
        _item.setIngredients(_tmpIngredients);
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        _item.setUrl(_tmpUrl);
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
  public Recipe getRecipeById(final int id) {
    final String _sql = "SELECT * FROM recipe WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFoodID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodID");
      final int _cursorIndexOfFoodUUID = CursorUtil.getColumnIndexOrThrow(_cursor, "foodUUID");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
      final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
      final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
      final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
      final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final Recipe _result;
      if (_cursor.moveToFirst()) {
        _result = new Recipe();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpFoodID;
        _tmpFoodID = _cursor.getInt(_cursorIndexOfFoodID);
        _result.setFoodID(_tmpFoodID);
        final String _tmpFoodUUID;
        if (_cursor.isNull(_cursorIndexOfFoodUUID)) {
          _tmpFoodUUID = null;
        } else {
          _tmpFoodUUID = _cursor.getString(_cursorIndexOfFoodUUID);
        }
        _result.setFoodUUID(_tmpFoodUUID);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpImage;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmpImage = null;
        } else {
          _tmpImage = _cursor.getString(_cursorIndexOfImage);
        }
        _result.setImage(_tmpImage);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _result.setDescription(_tmpDescription);
        final float _tmpCarbs;
        _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
        _result.setCarbs(_tmpCarbs);
        final float _tmpProtein;
        _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
        _result.setProtein(_tmpProtein);
        final float _tmpFat;
        _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
        _result.setFat(_tmpFat);
        final int _tmpCalories;
        _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
        _result.setCalories(_tmpCalories);
        final String _tmpIngredients;
        if (_cursor.isNull(_cursorIndexOfIngredients)) {
          _tmpIngredients = null;
        } else {
          _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
        }
        _result.setIngredients(_tmpIngredients);
        final String _tmpUrl;
        if (_cursor.isNull(_cursorIndexOfUrl)) {
          _tmpUrl = null;
        } else {
          _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        }
        _result.setUrl(_tmpUrl);
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
