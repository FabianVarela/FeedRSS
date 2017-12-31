package com.developer.fabian.feedrss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.developer.fabian.feedrss.entities.Item;

import java.util.HashMap;
import java.util.List;

public class FeedDatabase extends SQLiteOpenHelper {

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TITLE = 1;
    private static final int COLUMN_DESCRIPTION = 2;
    private static final int COLUMN_URL = 3;

    private static final String TAG = FeedDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "Feed.db";
    private static final int DATABASE_VERSION = 1;

    private static FeedDatabase singleton;

    private FeedDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptDatabase.CREATE_ENTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabase.ENTER_TABLE_NAME);
        onCreate(db);
    }

    public static synchronized FeedDatabase getInstance(Context context) {
        if (singleton == null)
            singleton = new FeedDatabase(context.getApplicationContext());

        return singleton;
    }

    public Cursor getEntries() {
        return getWritableDatabase().rawQuery("SELECT * FROM " + ScriptDatabase.ENTER_TABLE_NAME, null);
    }

    public void syncEntries(List<Item> entries) {
        HashMap<String, Item> entryMap = new HashMap<>();

        for (Item e : entries)
            entryMap.put(e.getTitle(), e);

        Log.i(TAG, "Consultar items actualmente almacenados");

        Cursor c = getEntries();
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " entradas, computando...");

        int id;
        String titulo;
        String descripcion;
        String url;

        while (c.moveToNext()) {
            id = c.getInt(COLUMN_ID);
            titulo = c.getString(COLUMN_TITLE);
            descripcion = c.getString(COLUMN_DESCRIPTION);
            url = c.getString(COLUMN_URL);
            Item match = entryMap.get(titulo);

            if (match != null) {
                entryMap.remove(titulo);

                if ((match.getTitle() != null && !match.getTitle().equals(titulo)) ||
                        (match.getDescription() != null && !match.getDescription().equals(descripcion)) ||
                        (match.getLink() != null && !match.getLink().equals(url))) {

                    updateEntries(id, match.getTitle(), match.getDescription(), match.getLink(), match.getContent().getUrl());
                }
            }
        }

        c.close();

        for (Item e : entryMap.values()) {
            Log.i(TAG, "Insertado: titulo =" + e.getTitle());
            insertEntries(e.getTitle(), e.getDescription(), e.getLink(), e.getContent().getUrl());
        }

        Log.i(TAG, "Se actualizaron los registros");
    }

    private void insertEntries(String titulo, String descripcion, String url, String thumb_url) {
        ContentValues values = new ContentValues();

        values.put(ScriptDatabase.EnterColumns.TITLE, titulo);
        values.put(ScriptDatabase.EnterColumns.DESCRIPTION, descripcion);
        values.put(ScriptDatabase.EnterColumns.URL, url);
        values.put(ScriptDatabase.EnterColumns.THUMB_URL, thumb_url);

        getWritableDatabase().insert(ScriptDatabase.ENTER_TABLE_NAME, null, values);
    }

    private void updateEntries(int id, String titulo, String descripcion, String url, String thumb_url) {
        ContentValues values = new ContentValues();

        values.put(ScriptDatabase.EnterColumns.TITLE, titulo);
        values.put(ScriptDatabase.EnterColumns.DESCRIPTION, descripcion);
        values.put(ScriptDatabase.EnterColumns.URL, url);
        values.put(ScriptDatabase.EnterColumns.THUMB_URL, thumb_url);

        getWritableDatabase().update(ScriptDatabase.ENTER_TABLE_NAME, values, ScriptDatabase.EnterColumns.ID + "=?", new String[]{String.valueOf(id)});
    }
}
