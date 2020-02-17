package id.deris.dapetfulus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "dapetfulusdb";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PAYMENT = "payments";
    private static final String TABLE_QA = "qas";
    private static final String TABLE_NOTIF = "notif";
    private static final String TABLE_HISTORY = "histories";

    // Payment Table Columns
    private static final String KEY_PAYMENT_ID = "id";
    private static final String KEY_PAYMENT_VIA = "via";
    private static final String KEY_PAYMENT_AMOUNT = "amount";
    private static final String KEY_PAYMENT_STATUS = "status";
    private static final String KEY_PAYMENT_TIME = "time";

    // QA Table Columns
    private static final String KEY_QA_ID = "id";
    private static final String KEY_QA_QUESTION = "question";
    private static final String KEY_QA_ANSWER = "answer";

    // Notif Table Columns
    private static final String KEY_NOTIF_ID = "id";
    private static final String KEY_NOTIF_TITLE = "title";
    private static final String KEY_NOTIF_DESCRIPTION = "description";
    private static final String KEY_NOTIF_TIME = "time";

    //History Table Column
    private static final String KEY_HISTORY_ID = "id";
    private static final String KEY_HISTORY_TITLE = "title";
    private static final String KEY_HISTORY_TIME = "time";
    private static final String KEY_HISTORY_COIN = "coin";


    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PAYMENT_TABLE = "CREATE TABLE " + TABLE_PAYMENT + "("
                + KEY_PAYMENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_PAYMENT_VIA + " TEXT,"
                + KEY_PAYMENT_AMOUNT + " TEXT,"
                + KEY_PAYMENT_STATUS + " TEXT,"
                + KEY_PAYMENT_TIME + " TEXT" + ")";

        String CREATE_QA_TABLE = "CREATE TABLE " + TABLE_QA + "("
                + KEY_QA_ID + " INTEGER PRIMARY KEY,"
                + KEY_QA_QUESTION + " TEXT,"
                + KEY_QA_ANSWER + " TEXT" + ")";

        String CREATE_NOTIF_TABLE = "CREATE TABLE " + TABLE_NOTIF + "("
                + KEY_NOTIF_ID+ " INTEGER PRIMARY KEY,"
                + KEY_NOTIF_TITLE + " TEXT,"
                + KEY_NOTIF_DESCRIPTION + " TEXT,"
                + KEY_NOTIF_TIME + " TEXT" + ")";

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_HISTORY_ID + " INTEGER PRIMARY KEY,"
                + KEY_HISTORY_TITLE + " TEXT,"
                + KEY_HISTORY_TIME + " TEXT,"
                + KEY_HISTORY_COIN + " TEXT" + ")";

        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_QA_TABLE);
        db.execSQL(CREATE_NOTIF_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIF);

            onCreate(db);
        }
    }

    // Insert a paymnet into the database
    public void addPayment(WithdrawModel payment) {

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            ContentValues values = new ContentValues();
            values.put(KEY_PAYMENT_VIA, payment.getVia());
            values.put(KEY_PAYMENT_AMOUNT, payment.getAmount());
            values.put(KEY_PAYMENT_STATUS, payment.getStatus());
            values.put(KEY_PAYMENT_TIME, payment.getTime());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_PAYMENT, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addHistory(HistoryModel history) {

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            ContentValues values = new ContentValues();
            values.put(KEY_HISTORY_TITLE, history.getTitle());
            values.put(KEY_HISTORY_TIME, history.getTime());
            values.put(KEY_HISTORY_COIN, history.getCoin());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_HISTORY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a qa into the database
    public void addQA(QAModel qaModel) {

        deleteAllQA();

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            ContentValues values = new ContentValues();
            values.put(KEY_QA_QUESTION, qaModel.getQuestion());
            values.put(KEY_QA_ANSWER, qaModel.getAnswer());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_QA, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Insert a qa into the database
    public void addNotif(NotifModel notifModel) {

        deleteAllQA();

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // The user might already exist in the database (i.e. the same user created multiple posts).
            ContentValues values = new ContentValues();
            values.put(KEY_NOTIF_TITLE, notifModel.getTitle());
            values.put(KEY_NOTIF_DESCRIPTION, notifModel.getDes());
            values.put(KEY_NOTIF_TIME, notifModel.getTime());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_NOTIF, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add notif to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get all payment in the database
    public List<WithdrawModel> getAllPayment() {
        List<WithdrawModel> payment = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String PAYMENT_SELECT_QUERY =
                "SELECT * FROM "+TABLE_PAYMENT + " ORDER BY "+KEY_PAYMENT_ID+" DESC";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PAYMENT_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    WithdrawModel withdrawModel= new WithdrawModel();
                    withdrawModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_PAYMENT_ID)));
                    withdrawModel.setVia(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_VIA)));
                    withdrawModel.setAmount(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_AMOUNT)));
                    withdrawModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_STATUS)));
                    withdrawModel.setTime(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_TIME)));

                    payment.add(withdrawModel);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return payment;
    }

    // Get all history in the database
    public List<HistoryModel> getAllHistory() {
        List<HistoryModel> history = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String HISTORY_SELECT_QUERY =
                "SELECT * FROM "+TABLE_HISTORY + " ORDER BY "+KEY_HISTORY_ID+" DESC";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(HISTORY_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_ID)));
                    historyModel.setTitle(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_TITLE)));
                    historyModel.setTime(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_TIME)));
                    historyModel.setCoin(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_COIN)));

                    history.add(historyModel);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return history;
    }

    // Get all qa in the database
    public List<QAModel> getAllQA() {
        List<QAModel> qa = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String QA_SELECT_QUERY =
                "SELECT * FROM "+TABLE_QA + " ORDER BY "+KEY_QA_ID+" DESC";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QA_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    QAModel qaModel = new QAModel();
                    qaModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_QA_ID)));
                    qaModel.setQuestion(cursor.getString(cursor.getColumnIndex(KEY_QA_QUESTION)));
                    qaModel.setAnswer(cursor.getString(cursor.getColumnIndex(KEY_QA_ANSWER)));

                    qa.add(qaModel);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return qa;
    }

    // Get all qa in the database
    public List<NotifModel> getAllNotif() {
        List<NotifModel> notif = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String NOTIF_SELECT_QUERY =
                "SELECT * FROM "+TABLE_NOTIF + " ORDER BY "+KEY_NOTIF_ID+" DESC";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(NOTIF_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    NotifModel notifModel = new NotifModel();
                    notifModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_NOTIF_ID)));
                    notifModel.setTitle(cursor.getString(cursor.getColumnIndex(KEY_NOTIF_TITLE)));
                    notifModel.setDes(cursor.getString(cursor.getColumnIndex(KEY_NOTIF_DESCRIPTION)));
                    notifModel.setTime(cursor.getString(cursor.getColumnIndex(KEY_NOTIF_TIME)));

                    notif.add(notifModel);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get notif from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notif;
    }


    // Delete all payment in the database
    public void deleteAllPayments() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PAYMENT, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all payment");
        } finally {
            db.endTransaction();
        }
    }

        public void deleteAllQA() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_QA, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all qa");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllNotif() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_NOTIF, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all notifF");
        } finally {
            db.endTransaction();
        }
    }
}
