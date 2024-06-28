package com.example.gr.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.gr.database.dao.DiaryDAO;
import com.example.gr.database.dao.DiaryDAO_Impl;
import com.example.gr.database.dao.ExerciseDAO;
import com.example.gr.database.dao.ExerciseDAO_Impl;
import com.example.gr.database.dao.FoodDAO;
import com.example.gr.database.dao.FoodDAO_Impl;
import com.example.gr.database.dao.FoodLogDAO;
import com.example.gr.database.dao.FoodLogDAO_Impl;
import com.example.gr.database.dao.RecipeDAO;
import com.example.gr.database.dao.RecipeDAO_Impl;
import com.example.gr.database.dao.RecordedWorkoutDAO;
import com.example.gr.database.dao.RecordedWorkoutDAO_Impl;
import com.example.gr.database.dao.WorkoutDAO;
import com.example.gr.database.dao.WorkoutDAO_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LocalDatabase_Impl extends LocalDatabase {
  private volatile FoodDAO _foodDAO;

  private volatile DiaryDAO _diaryDAO;

  private volatile ExerciseDAO _exerciseDAO;

  private volatile FoodLogDAO _foodLogDAO;

  private volatile WorkoutDAO _workoutDAO;

  private volatile RecipeDAO _recipeDAO;

  private volatile RecordedWorkoutDAO _recordedWorkoutDAO;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `food` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `numberOfServings` REAL NOT NULL, `servingSize` REAL NOT NULL, `calories` INTEGER NOT NULL, `carb` REAL NOT NULL, `protein` REAL NOT NULL, `fat` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `diary` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `caloriesGoal` INTEGER NOT NULL, `burntCalories` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `remainingCalories` INTEGER NOT NULL, `date` TEXT, `intakeProtein` INTEGER NOT NULL, `intakeCarb` INTEGER NOT NULL, `intakeFat` INTEGER NOT NULL, `intakeCalories` INTEGER NOT NULL, `totalSteps` INTEGER NOT NULL, `carbGoal` INTEGER NOT NULL, `proteinGoal` INTEGER NOT NULL, `fatGoal` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `diaryID` INTEGER NOT NULL, `exerciseId` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `caloriesBurnt` INTEGER NOT NULL, `createdAt` TEXT, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `foodlog` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `totalCarb` REAL NOT NULL, `totalProtein` REAL NOT NULL, `totalFat` REAL NOT NULL, `totalCalories` INTEGER NOT NULL, `meal` INTEGER NOT NULL, `numberOfServings` REAL NOT NULL, `foodId` INTEGER NOT NULL, `diaryId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recipe` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `foodID` INTEGER NOT NULL, `name` TEXT, `image` TEXT, `description` TEXT, `carbs` REAL NOT NULL, `protein` REAL NOT NULL, `fat` REAL NOT NULL, `calories` INTEGER NOT NULL, `ingredients` TEXT, `url` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `met` REAL NOT NULL, `category` TEXT, `defaultDuration` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recordedworkout` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `baseActivitySummaryId` INTEGER NOT NULL, `createdAt` TEXT, `activityKind` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `avgHeartRate` INTEGER NOT NULL, `minHeartRate` INTEGER NOT NULL, `maxHeartRate` INTEGER NOT NULL, `caloriesBurnt` INTEGER NOT NULL, `distance` REAL NOT NULL, `timeStamp` INTEGER NOT NULL, `diaryID` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ff417b4b683d0ddfa4f9c7305c7ef2fc')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `food`");
        db.execSQL("DROP TABLE IF EXISTS `diary`");
        db.execSQL("DROP TABLE IF EXISTS `workout`");
        db.execSQL("DROP TABLE IF EXISTS `foodlog`");
        db.execSQL("DROP TABLE IF EXISTS `recipe`");
        db.execSQL("DROP TABLE IF EXISTS `exercise`");
        db.execSQL("DROP TABLE IF EXISTS `recordedworkout`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFood = new HashMap<String, TableInfo.Column>(8);
        _columnsFood.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("numberOfServings", new TableInfo.Column("numberOfServings", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("servingSize", new TableInfo.Column("servingSize", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("calories", new TableInfo.Column("calories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("carb", new TableInfo.Column("carb", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("protein", new TableInfo.Column("protein", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("fat", new TableInfo.Column("fat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFood = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFood = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFood = new TableInfo("food", _columnsFood, _foreignKeysFood, _indicesFood);
        final TableInfo _existingFood = TableInfo.read(db, "food");
        if (!_infoFood.equals(_existingFood)) {
          return new RoomOpenHelper.ValidationResult(false, "food(com.example.gr.model.Food).\n"
                  + " Expected:\n" + _infoFood + "\n"
                  + " Found:\n" + _existingFood);
        }
        final HashMap<String, TableInfo.Column> _columnsDiary = new HashMap<String, TableInfo.Column>(14);
        _columnsDiary.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("caloriesGoal", new TableInfo.Column("caloriesGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("burntCalories", new TableInfo.Column("burntCalories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("remainingCalories", new TableInfo.Column("remainingCalories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("intakeProtein", new TableInfo.Column("intakeProtein", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("intakeCarb", new TableInfo.Column("intakeCarb", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("intakeFat", new TableInfo.Column("intakeFat", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("intakeCalories", new TableInfo.Column("intakeCalories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("totalSteps", new TableInfo.Column("totalSteps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("carbGoal", new TableInfo.Column("carbGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("proteinGoal", new TableInfo.Column("proteinGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiary.put("fatGoal", new TableInfo.Column("fatGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiary = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiary = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiary = new TableInfo("diary", _columnsDiary, _foreignKeysDiary, _indicesDiary);
        final TableInfo _existingDiary = TableInfo.read(db, "diary");
        if (!_infoDiary.equals(_existingDiary)) {
          return new RoomOpenHelper.ValidationResult(false, "diary(com.example.gr.model.Diary).\n"
                  + " Expected:\n" + _infoDiary + "\n"
                  + " Found:\n" + _existingDiary);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkout = new HashMap<String, TableInfo.Column>(7);
        _columnsWorkout.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("diaryID", new TableInfo.Column("diaryID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("caloriesBurnt", new TableInfo.Column("caloriesBurnt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("createdAt", new TableInfo.Column("createdAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkout.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkout = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkout = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkout = new TableInfo("workout", _columnsWorkout, _foreignKeysWorkout, _indicesWorkout);
        final TableInfo _existingWorkout = TableInfo.read(db, "workout");
        if (!_infoWorkout.equals(_existingWorkout)) {
          return new RoomOpenHelper.ValidationResult(false, "workout(com.example.gr.model.Workout).\n"
                  + " Expected:\n" + _infoWorkout + "\n"
                  + " Found:\n" + _existingWorkout);
        }
        final HashMap<String, TableInfo.Column> _columnsFoodlog = new HashMap<String, TableInfo.Column>(9);
        _columnsFoodlog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("totalCarb", new TableInfo.Column("totalCarb", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("totalProtein", new TableInfo.Column("totalProtein", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("totalFat", new TableInfo.Column("totalFat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("totalCalories", new TableInfo.Column("totalCalories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("meal", new TableInfo.Column("meal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("numberOfServings", new TableInfo.Column("numberOfServings", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("foodId", new TableInfo.Column("foodId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodlog.put("diaryId", new TableInfo.Column("diaryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFoodlog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFoodlog = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoodlog = new TableInfo("foodlog", _columnsFoodlog, _foreignKeysFoodlog, _indicesFoodlog);
        final TableInfo _existingFoodlog = TableInfo.read(db, "foodlog");
        if (!_infoFoodlog.equals(_existingFoodlog)) {
          return new RoomOpenHelper.ValidationResult(false, "foodlog(com.example.gr.model.FoodLog).\n"
                  + " Expected:\n" + _infoFoodlog + "\n"
                  + " Found:\n" + _existingFoodlog);
        }
        final HashMap<String, TableInfo.Column> _columnsRecipe = new HashMap<String, TableInfo.Column>(11);
        _columnsRecipe.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("foodID", new TableInfo.Column("foodID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("image", new TableInfo.Column("image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("carbs", new TableInfo.Column("carbs", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("protein", new TableInfo.Column("protein", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("fat", new TableInfo.Column("fat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("calories", new TableInfo.Column("calories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("ingredients", new TableInfo.Column("ingredients", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipe.put("url", new TableInfo.Column("url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecipe = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecipe = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecipe = new TableInfo("recipe", _columnsRecipe, _foreignKeysRecipe, _indicesRecipe);
        final TableInfo _existingRecipe = TableInfo.read(db, "recipe");
        if (!_infoRecipe.equals(_existingRecipe)) {
          return new RoomOpenHelper.ValidationResult(false, "recipe(com.example.gr.model.Recipe).\n"
                  + " Expected:\n" + _infoRecipe + "\n"
                  + " Found:\n" + _existingRecipe);
        }
        final HashMap<String, TableInfo.Column> _columnsExercise = new HashMap<String, TableInfo.Column>(5);
        _columnsExercise.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("met", new TableInfo.Column("met", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercise.put("defaultDuration", new TableInfo.Column("defaultDuration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercise = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExercise = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercise = new TableInfo("exercise", _columnsExercise, _foreignKeysExercise, _indicesExercise);
        final TableInfo _existingExercise = TableInfo.read(db, "exercise");
        if (!_infoExercise.equals(_existingExercise)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise(com.example.gr.model.Exercise).\n"
                  + " Expected:\n" + _infoExercise + "\n"
                  + " Found:\n" + _existingExercise);
        }
        final HashMap<String, TableInfo.Column> _columnsRecordedworkout = new HashMap<String, TableInfo.Column>(14);
        _columnsRecordedworkout.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("baseActivitySummaryId", new TableInfo.Column("baseActivitySummaryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("createdAt", new TableInfo.Column("createdAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("activityKind", new TableInfo.Column("activityKind", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("avgHeartRate", new TableInfo.Column("avgHeartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("minHeartRate", new TableInfo.Column("minHeartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("maxHeartRate", new TableInfo.Column("maxHeartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("caloriesBurnt", new TableInfo.Column("caloriesBurnt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("distance", new TableInfo.Column("distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("timeStamp", new TableInfo.Column("timeStamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecordedworkout.put("diaryID", new TableInfo.Column("diaryID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecordedworkout = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecordedworkout = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecordedworkout = new TableInfo("recordedworkout", _columnsRecordedworkout, _foreignKeysRecordedworkout, _indicesRecordedworkout);
        final TableInfo _existingRecordedworkout = TableInfo.read(db, "recordedworkout");
        if (!_infoRecordedworkout.equals(_existingRecordedworkout)) {
          return new RoomOpenHelper.ValidationResult(false, "recordedworkout(com.example.gr.model.RecordedWorkout).\n"
                  + " Expected:\n" + _infoRecordedworkout + "\n"
                  + " Found:\n" + _existingRecordedworkout);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ff417b4b683d0ddfa4f9c7305c7ef2fc", "6785e5ae28d8f0af054a8097a5722e22");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "food","diary","workout","foodlog","recipe","exercise","recordedworkout");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `food`");
      _db.execSQL("DELETE FROM `diary`");
      _db.execSQL("DELETE FROM `workout`");
      _db.execSQL("DELETE FROM `foodlog`");
      _db.execSQL("DELETE FROM `recipe`");
      _db.execSQL("DELETE FROM `exercise`");
      _db.execSQL("DELETE FROM `recordedworkout`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FoodDAO.class, FoodDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(DiaryDAO.class, DiaryDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseDAO.class, ExerciseDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodLogDAO.class, FoodLogDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutDAO.class, WorkoutDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecipeDAO.class, RecipeDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecordedWorkoutDAO.class, RecordedWorkoutDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FoodDAO foodDAO() {
    if (_foodDAO != null) {
      return _foodDAO;
    } else {
      synchronized(this) {
        if(_foodDAO == null) {
          _foodDAO = new FoodDAO_Impl(this);
        }
        return _foodDAO;
      }
    }
  }

  @Override
  public DiaryDAO diaryDAO() {
    if (_diaryDAO != null) {
      return _diaryDAO;
    } else {
      synchronized(this) {
        if(_diaryDAO == null) {
          _diaryDAO = new DiaryDAO_Impl(this);
        }
        return _diaryDAO;
      }
    }
  }

  @Override
  public ExerciseDAO exerciseDAO() {
    if (_exerciseDAO != null) {
      return _exerciseDAO;
    } else {
      synchronized(this) {
        if(_exerciseDAO == null) {
          _exerciseDAO = new ExerciseDAO_Impl(this);
        }
        return _exerciseDAO;
      }
    }
  }

  @Override
  public FoodLogDAO foodLogDAO() {
    if (_foodLogDAO != null) {
      return _foodLogDAO;
    } else {
      synchronized(this) {
        if(_foodLogDAO == null) {
          _foodLogDAO = new FoodLogDAO_Impl(this);
        }
        return _foodLogDAO;
      }
    }
  }

  @Override
  public WorkoutDAO workoutDAO() {
    if (_workoutDAO != null) {
      return _workoutDAO;
    } else {
      synchronized(this) {
        if(_workoutDAO == null) {
          _workoutDAO = new WorkoutDAO_Impl(this);
        }
        return _workoutDAO;
      }
    }
  }

  @Override
  public RecipeDAO recipeDAO() {
    if (_recipeDAO != null) {
      return _recipeDAO;
    } else {
      synchronized(this) {
        if(_recipeDAO == null) {
          _recipeDAO = new RecipeDAO_Impl(this);
        }
        return _recipeDAO;
      }
    }
  }

  @Override
  public RecordedWorkoutDAO recordedWorkoutDAO() {
    if (_recordedWorkoutDAO != null) {
      return _recordedWorkoutDAO;
    } else {
      synchronized(this) {
        if(_recordedWorkoutDAO == null) {
          _recordedWorkoutDAO = new RecordedWorkoutDAO_Impl(this);
        }
        return _recordedWorkoutDAO;
      }
    }
  }
}
