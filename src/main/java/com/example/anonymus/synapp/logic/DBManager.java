package com.example.anonymus.synapp.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.anonymus.synapp.data.Synonym;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by anonymus on 06.03.17.
 */

public class DBManager extends SQLiteOpenHelper {

    private Synonym synonym = null;
    private Context myContext = null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_synapp";

    /*
    path to database
     */
    private static String DB_PATH = ""; //"/data/data/com.example.anonymus.synapp/logic/my_synonyms.sql";


    // Contacts table name
    private static final String TABLE_SYNONYMUS = "synonyms";

    // Shops Table Columns names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ALT1 = "alt1";
    private static final String COLUMN_ALT2 = "alt2";
    private static final String COLUMN_ALT3 = "alt3";
    private static final String COLUMN_DESC = "desc";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;

        //Log.e("DBManager", DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE synonyms (\n" +
                "  `_id` bigint(20) NOT NULL PRIMARY KEY,\n" +
                "  `synset_id` bigint(20) NOT NULL,\n" +
                "  `word` varchar(255) NOT NULL\n" +
                ")";
        //Log.e("DBManager", DB_PATH);
        db.execSQL(CREATE_TABLE);
        Log.e("DBManager", "Datenbank erstellt du Dödel");

        fillDatabase(db);
        //db.close();

       /* Log.e("DBManager", "Comes to here");
        DB_PATH = myContext.getFilesDir().getPath() + "/databases/my_synonyms";

        //Open your local db as the input stream
        InputStream myInput = null;

        //Open the empty db as the output stream
        OutputStream myOutput = null;
        try {
            myInput = myContext.getAssets().open("my_synonyms");
            // Path to the just created empty db
            String outFileName = DB_PATH;
            myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

            Log.e("DBManager", "Database created");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNONYMUS);

        // Creating tables again
        onCreate(db);
    }

    public void fillDatabase(SQLiteDatabase db) {

        //DB_PATH =  "/data/data/com.example.anonymus.synapp/files/logic/my_synonyms.txt";
        //String path = myContext.getDir().getPath() + "logic";
        //String path = ;
        //File file = new File();

        //Read text from file
        //StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(myContext.getAssets().open("synonyms.db")));
            String line;

            while ((line = br.readLine()) != null) {
                //Log.e("DBManager", line);
                db.execSQL(line);
            }
            br.close();
            Log.e("Fill", "Database filled");
        }
        catch (IOException e) {
            Log.e("DBManager", "File not found");
            //You'll need to add proper error handling here
        }
        /*synonym = new Synonym("dekadent", "ausschweifend", "verschwenderisch", "großkotzig", "im kulturellen Niedergang begriffen");
        addFirstTime(synonym, db);
        synonym = new Synonym("subtil", "fein strukturiert", "unterschwellig", "präzise", "bis ins Detail geplant");
        addFirstTime(synonym, db);*/

        /*
        synonym = new Synonym("pragmatisch", "sachbezogen", "", "", "");
        addSynonym(synonym);
        synonym = new Synonym("dito", "ebenfalls", "gleichfalls", "", "");
        addSynonym(synonym);
        synonym = new Synonym("obsolet", "veraltet", "überholt", "", "");
        addSynonym(synonym);*/

        //Log.e("DBManager", "Datenbank gefüllt!");
    }

    public void addFirstTime(Synonym synonym, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, synonym.getName());
        values.put(COLUMN_ALT1, synonym.getAlt1());
        values.put(COLUMN_ALT2, synonym.getAlt2());
        values.put(COLUMN_ALT3, synonym.getAlt3());
        values.put(COLUMN_DESC, synonym.getDesc());

        // Inserting Row
        db.insert(TABLE_SYNONYMUS, null, values);
    }

    /*
    *   Method for adding synonyms to database
    */
    public void addSynonym(Synonym synonym) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, synonym.getName());
        values.put(COLUMN_ALT1, synonym.getAlt1());
        values.put(COLUMN_ALT2, synonym.getAlt2());
        values.put(COLUMN_ALT3, synonym.getAlt3());
        values.put(COLUMN_DESC, synonym.getDesc());

        // Inserting Row
        db.insert(TABLE_SYNONYMUS, null, values);

        // Closing database connection
        db.close();
    }

    /*
    *   Method for reading synonyms of database
    */
    public Synonym getSynonym(String name) {

        //Log.e("DBManager", "Comes to here");
        Synonym synonym = null;
        SQLiteDatabase db = this.getReadableDatabase();

        /*Cursor cursor = db.rawQuery("SELECT name, alt1, alt2, alt3, desc FROM "
                + TABLE_SYNONYMUS + " WHERE name = ?", new String[] {name});*/

        Cursor cursor = db.rawQuery("SELECT DISTINCT synonyms2.word FROM synonyms synonyms2\n" +
                "JOIN synonyms ON synonyms2.synset_id = synonyms.synset_id\n" +
                "WHERE synonyms.word = ? AND synonyms2.word != ?", new String[] {name, name});

        if (cursor != null && cursor.moveToFirst()) {

            int i = 0;
            String[] result = new String[5];
            try {
                while (cursor.moveToNext()) {
                    if (i < 5) {
                        result[i] = cursor.getString(0);
                    }
                    i++;
                }
            } finally {
                cursor.close();
            }
            synonym = new Synonym(result[0], result[1], result[2], result[3], result[4]);
        }
        else {
            synonym = new Synonym("No search results found", "", "", "", "");
        }
        //cursor.close();
        db.close();
        return synonym;
    }

    /*
    *   Method for counting all synonyms of database
    */
    public int getSynonymCount() {

        String countQuery = "SELECT * FROM " + TABLE_SYNONYMUS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    /*
    *   Method for updating single synonym of database
    */
    public int updateSynonym(Synonym synonym) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALT1, synonym.getAlt1());
        values.put(COLUMN_ALT2, synonym.getAlt2());
        values.put(COLUMN_ALT3, synonym.getAlt3());
        values.put(COLUMN_DESC, synonym.getDesc());

        return db.update(TABLE_SYNONYMUS, values, COLUMN_NAME + " = ?",
                new String[]{ synonym.getName() });
    }

    /*
    *   Method for deleting single synonym of database
    */
    public void deleteSynonym(Synonym synonym) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SYNONYMUS, COLUMN_NAME + " = ?",
                new String[] { synonym.getName() });
        db.close();
    }
}
