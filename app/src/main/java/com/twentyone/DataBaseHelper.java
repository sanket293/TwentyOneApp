package com.twentyone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static SQLiteDatabase sqliteDb;
    private static DataBaseHelper instance;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TwentyOneDB.db";

    private Context context;


    DataBaseHelper(Context context, String name, CursorFactory factory,
                   int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    private static void initialize(Context context, String databaseName) {
        if (instance == null) {

            if (!checkDatabase(context, databaseName)) {

                try {
                    copyDataBase(context, databaseName);
                } catch (IOException e) {

                    System.out.println(databaseName
                            + " does not exists ");
                }
            }

            instance = new DataBaseHelper(context, databaseName, null,
                    DATABASE_VERSION);
            sqliteDb = instance.getWritableDatabase();

            System.out.println("instance of  " + databaseName + " created ");
        }
    }

    public static final DataBaseHelper getInstance(Context context) {
        initialize(context, DATABASE_NAME);
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return sqliteDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void copyDataBase(Context aContext, String databaseName)
            throws IOException {

        InputStream myInput = aContext.getAssets().open(databaseName);

        String outFileName = getDatabasePath(aContext, databaseName);

        File f = new File("/data/data/" + aContext.getPackageName()
                + "/databases/");
        if (!f.exists())
            f.mkdir();

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        System.out.println(databaseName + " copied");
    }

    public static boolean checkDatabase(Context aContext, String databaseName) {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = getDatabasePath(aContext, databaseName);
            try {
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READWRITE);
                checkDB.close();

            } catch (Exception e) {
            }
        } catch (SQLiteException e) {

            System.out.println(databaseName + " does not exists");
        }

        return checkDB != null ? true : false;
    }

    private static String getDatabasePath(Context aContext, String databaseName) {
        return "/data/data/" + aContext.getPackageName() + "/databases/"
                + databaseName;
    }


    // User Define Functions

    //Enter User registration
    public static boolean addRegistrationDetails(UserFields user) {
        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }

            if (user == null) {
                return false;
            }

            String name = user.getName();
            String password = user.getPassword();
            String email = user.getEmail();
            int roleId = user.getRoleId();
            int phone = user.getPhone();

            String query = "insert into User(Name, Password, Email, Phone,RoleId) values(" + name + "," + password + "," + email + "," + phone + "," + roleId + ");";

            sqliteDb = instance.getWritableDatabase();
            sqliteDb.execSQL(query);

        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;

    }


    public boolean addGoalString(String name ,String goalStr) {


        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }

            if (goalStr.equalsIgnoreCase("")||name.equalsIgnoreCase("")) {
                return false;
            }

            String query = "insert into UserGoalList(Name,Goal) values(" + name + "," + goalStr + ");";
            sqliteDb = instance.getWritableDatabase();
            sqliteDb.execSQL(query);

        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }










    ////////    SELECT



    public static UserFields checkCredentials(String email, String password) {

        UserFields userFields = null;
        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }
            sqliteDb = instance.getWritableDatabase();

            String query = "select * from User where  Email=" + email + " and password=" + password + ";";

            Cursor cursor = sqliteDb.rawQuery(query, null);

            if (cursor != null) {
                userFields = new UserFields();

                while (cursor.moveToNext()) {
                    if (cursor.moveToFirst()) {

                        //     Name, Password, Email, Phone,RoleId)

                        userFields.setName(cursor.getString(1));
                        userFields.setPassword(cursor.getString(2));
                        userFields.setEmail(cursor.getString(3));
                        userFields.setPhone(cursor.getInt(4));
                        userFields.setRoleId(cursor.getInt(5));
                    }
                }
                cursor.close();
            } else {
                Log.e("error in db", "cursor is null at checkcredential function");
            }

        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
        }
        return userFields;

    }


    public static void rawQuery(String query) {
//        try {
//            if (sqliteDb.isOpen()) {
//                sqliteDb.close();
//            }
//            sqliteDb = instance.getWritableDatabase();
//
//            cursor = null;
//            cursor = sqliteDb.rawQuery(query, null);
//        } catch (Exception e) {
//            System.out.println("DB ERROR  " + e.getMessage());
//            e.printStackTrace();
//        }
//        return cursor;

    }

    public static void execute(String query) {
        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }
            sqliteDb = instance.getWritableDatabase();
            sqliteDb.execSQL(query);
        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
        }
    }


}
