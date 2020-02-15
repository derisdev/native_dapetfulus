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
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_PAYMENT = "payments";
    private static final String TABLE_QA = "qas";

    // Paymnet Table Columns
    private static final String KEY_PAYMENT_ID = "id";
    private static final String KEY_PAYMENT_VIA = "via";
    private static final String KEY_PAYMENT_AMOUNT = "amount";
    private static final String KEY_PAYMENT_STATUS = "status";
    private static final String KEY_PAYMENT_TIME = "time";

    // User Table Columns
    private static final String KEY_QA_ID = "id";
    private static final String KEY_QA_QUESTION = "question";
    private static final String KEY_QA_ANSWER = "answer";

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

        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_QA_TABLE);
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

    // Insert a paymnet into the database
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
            db.insertOrThrow(TABLE_PAYMENT, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get all posts in the database
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


    // Delete all posts and users in the database
    public void deleteAllPayments() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PAYMENT, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    private void deleteAllQA() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_QA, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}
