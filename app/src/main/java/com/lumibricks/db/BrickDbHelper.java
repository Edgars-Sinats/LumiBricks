package com.lumibricks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lumibricks.FilterDialogFragment;
import com.lumibricks.model.Brick;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BrickDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "brick.db"; //Change name if you want to test
    private static final int DATABASE_VERSION = 9; //increase (version start from 1)
    private static BrickDbHelper instance;
    public SQLiteDatabase db;


    public BrickDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Only once in time you can use on Instance
    public static synchronized BrickDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BrickDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    public static synchronized BrickDbHelper getInstance(FilterDialogFragment filterDialogFragment) {
        if (instance == null) {
            instance = new BrickDbHelper(filterDialogFragment.getContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_BRICK_TABLES = "CREATE TABLE " +
                BrickContract.BrickTable.TABLE_NAME + "( " +
                BrickContract.BrickTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BrickContract.BrickTable.COLUMN_NAME + " TEXT, " +
                BrickContract.BrickTable.COLUMN_SELL_TYPE + " TEXT, " +
                BrickContract.BrickTable.COLUMN_SIZE_G + " TEXT, " +
                BrickContract.BrickTable.COLUMN_SIZE_A + " TEXT, " +
                BrickContract.BrickTable.COLUMN_SIZE_B + " TEXT, " +
                BrickContract.BrickTable.COLUMN_GB_M2 + " TEXT, " +
                BrickContract.BrickTable.COLUMN_GB_IN_ROW + " TEXT, " +
                BrickContract.BrickTable.COLUMN_GB_IN_PALLET + " TEXT, " +
                BrickContract.BrickTable.COLUMN_M2_IN_PALLET + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_LUMI + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_GRAY + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_GREEN + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_YELLOW + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_ORANGE + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_RED+ " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_BLACK + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_BROWN + " TEXT, " +
                BrickContract.BrickTable.COLUMN_PRICE_WHITE + " TEXT " +
                ")";

        //Create Brick Table
        db.execSQL(SQL_CREATE_BRICK_TABLES);
        //Fill brick Table
        fillBrickCTable();

    }

    //If Db is updated...
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //We could clean old table
        db.execSQL("DROP TABLE IF EXISTS " + BrickContract.BrickTable.TABLE_NAME);

        //Rebuild database
        onCreate(db);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    //TODO
//    Yelow orange extra price
    private void fillBrickCTable(){

        //Set individual color
        Brick b1 = new Brick(
                "PRIZMA 5" ,
                "m2,gb,rinda",
                50,
                11.2,
                560,
                35,
                200,
                50,
                100,
                5.2,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b1);

        Brick b2 = new Brick(
                "NAST 5",
                "m2,gb,rinda",
                46,
                11.1,
                0,
                32,
                180,
                50,
                120,
                5.2,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b2);

        Brick b3 = new Brick(
                "NAST 6",
                "m2,gb,rinda",
                46,
                9.0,
                0,
                32,
                180,
                60,
                120,
                6.5,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b3);

        Brick b4 = new Brick(
                "PRIZMA 6" ,
                "m2,gb,rinda",
                50,
                9.1,
                0,
                35,
                200,
                60,
                100,
                6.5,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);

        Brick b5 = new Brick(
                "PRIZMA 6(bez fāz.)" ,
                "m2,gb,rinda",
                50,
                9.1,
                0,
                35,
                200,
                60,
                100,
                6.5,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b5);

        //TODO vienā paletesrindā 0.7m2
        Brick b6 = new Brick(
                "MOZAĪKA 5" ,
                "m2,rinda",
                0,
                9,
                0,
                0,
                240,
                60,
                60,
                6.5,
                0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);

        Brick b7 = new Brick(
                "ASORTI" ,
                "m2,rinda",
                0,
                8.8,
                0,
                0,
                240,
                60,
                60,
                6.5,
                0.9,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b7);

        Brick b8 = new Brick(
                "NAST 8" ,
                "m2,gb,rinda",
                46,
                6.9,
                0,
                32,
                180,
                80,
                80,
                6.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b8);

        Brick b9 = new Brick(
                "NAST 8 PUSE" ,
                "m2,gb,rinda",
                92,
                6.9,
                0,
                64,
                90,
                80,
                120,
                6.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b9);

        Brick b10 = new Brick(
                "NAST 8 MINI" ,
                "m2,gb,rinda",
                138,
                6.9,
                0,
                95,
                120,
                60,
                80,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b10);

        Brick b11 = new Brick(
                "PRIZMA 8" ,
                "m2,gb,rinda",
                50,
                7.0,
                35,
                0,
                200,
                80,
                100,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b11);

        Brick b12 = new Brick(
                "PRIZMA 8 (bez fāzes)",
                "m2,gb,rinda",
                50,
                7.0,
                0,
                35,
                200,
                80,
                60,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b12);

        Brick b13= new Brick(
                "NAST G" ,
                "m2,gb,rinda",
                33,
                6.3,
                0,
                21,
                300,
                80,
                100,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b13);

        Brick b14 = new Brick(
                "SIGMA 8" ,
                "m2,gb,rinda",
                42,
                6.4,
                0,
                26,
                223,
                80,
                107,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b14);

        Brick b15 = new Brick(
                "TAVR" ,
                "m2,gb,rinda",
                36,
                5.55,
                200,
                20,
                200,
                80,
                165,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b15);

//        Brick b16 = new Brick(
//                "TAVR" ,
//                "m2,gb,rinda",
//                36,
//                5.55,
//                7.5,
//                200,
//                80,
//                165,
//                7.5,
//                0.0,
//                0,
//                0.7,
//                1.7,
//                1.7,
//                0.7,
//                0.7,
//                0.7,
//                1.0);
//        addBrick(b16);

        Brick b17 = new Brick(
                "POLYGONAL 8" ,
                "m2,gb,rinda",
                8,
                7.32,
                60,
                8,
                350,
                80,
                302,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b17);

        Brick b18 = new Brick(
                "KONUS-1" ,
                "m2,gb,rinda",
                92,
                6.5,
                598,
                60,
                105,
                80,
                75,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b18);

        Brick b19 = new Brick(
                "KONUS-2" ,
                "m2,gb,rinda",
                86,
                6.5,
                600,
                60,
                125,
                80,
                50,
                7.5,
                0.0,
                0,
                0.7,
                1.7,
                1.7,
                0.7,
                0.7,
                0.7,
                1.0);
        addBrick(b19);

        Brick b20 = new Brick(
                "TAKTILAS pump" ,
                "m2,gb,rinda",
                46,
                9.0,
                0,
                32,
                180,
                60,
                120,
                9.0,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b20);

        Brick b21 = new Brick(
                "TAKTILAS line" ,
                "m2,gb,rinda",
                46,
                9.0,
                0,
                32,
                180,
                60,
                120,
                9.0,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b21);

        //TODO apmales

        Brick b22 = new Brick(
                "TEKNE" ,
                "m2,gb,rinda",
                0,
                0,
                30,
                3,
                800,
                80,
                200,
                2.5,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b22);

        Brick b23 = new Brick(
                "Ietvju apmale 0.6" ,
                "gb,m",
                0,
                30,
                50,
                5,
                600,
                70,
                200,
                1.2,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b23);

        Brick b24 = new Brick(
                "Ietvju apmale 1" ,
                "gb",
                0,
                0,
                32,
                0,
                1000,
                80,
                200,
                1.85,
                0.0,
                0,
                0,
                0,
                0,
                0,
                3.5,
                0,
                0);
        addBrick(b24);

        Brick b25 = new Brick(
                "BORD" ,
                "gb",
                0,
                0,
                12,
                0,
                1000,
                300,
                150,
                4.3,
                0.0,
                0,
                0,
                0,
                0,
                0,
                6.5,
                0,
                0);
        addBrick(b25);

        Brick b26 = new Brick(
                "BORD (iebraucamā)" ,
                "gb",
                0,
                0,
                12,
                0,
                1000,
                220,
                150,
                4.0,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b26);

        Brick b27 = new Brick(
                "Ceļa apmale ar rādiusu R-5 (r.40)" ,
                "gb",
                0,
                0,
                12,
                0,
                0,
                0,
                0,
                6.3,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b27);

        Brick b28 = new Brick(
                "Ceļa apmale ar rādiusu R-3 (r.24)" ,
                "gb",
                0,
                0,
                12,
                0,
                0,
                0,
                0,
                6.3,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b28);

        Brick b29 = new Brick(
                "Ceļa apmale ar rādiusu R-1 (r.8)" ,
                "gb",
                0,
                0,
                12,
                0,
                0,
                0,
                0,
                6.3,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b29);

        Brick b30 = new Brick(
                "BORD (labā/kreisā)" ,
                "gb",
                0,
                0,
                12,
                0,
                1000,
                220,
                300,
                6.7,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b30);

//        Brick b31 = new Brick(
//                "Betona plāksne" ,
//                "gb",
//                0,
//                10,
//                52,
//                500,
//                60,
//                400,
//                1.5,
//                0);
//        addBrick(b31);

        Brick b32 = new Brick(
                "Betona plāksne 5x4 x.6" ,
                "gb,m2",
                5,
                10,
                52,
                0,
                500,
                60,
                400,
                1.5,
                2.2,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b32);

        Brick b33 = new Brick(
                "Betona plāksne 3x3 x.7" ,
                "gb,m2",
                11,
                5.4,
                60,
                0,
                300,
                70,
                300,
                0.75,
                1.45,
                0.05,
                0.05,
                0.05,
                0.05,
                0.05,
                0.05,
                0.05,
                0.05);
        addBrick(b33);

        Brick b34 = new Brick(
                "Betona plāksne 4.5x3 x.7" ,
                "gb,m2",
                7,
                5.4,
                40,
                0,
                450,
                70,
                300,
                1.3,
                2.0,
                0.1,
                0.1,
                0.1,
                0.1,
                0.1,
                0.1,
                0.1,
                0.1);
        addBrick(b34);

        Brick b35 = new Brick(
                "Betona plāksne 7.5x3 x.7" ,
                "gb,m2",
                4,
                6.75,
                30,
                0,
                750,
                70,
                300,
                1.8,
                2.5,
                0.16,
                0.16,
                0.16,
                0.16,
                0.16,
                0.16,
                0.16,
                0.16);
        addBrick(b35);

        //TODO check data: Zemžoga plāksne
        Brick b36 = new Brick(
                "Zemžoga plāksne" ,
                "gb",
                4,
                6.75,
                30,
                0,
                750,
                70,
                300,
                1.8,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b36);

        //ToDO betona lego bloks
        Brick b37 = new Brick(
                "LEGO 180",
                "gb",
                0,
                0,
                0,
                0,
                1800,
                600,
                600,
                60,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b37);

        Brick b38 = new Brick(
                "LEGO 120" ,
                "gb",
                0,
                0,
                0,
                0,
                1200,
                600,
                600,
                45,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b38);

        Brick b39 = new Brick(
                "LEGO 60" ,
                "gb",
                0,
                0,
                0,
                0,
                600,
                600,
                600,
                25,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
        addBrick(b39);

    }

    private void addBrick(Brick brick){
        ContentValues cv = new ContentValues();
        cv.put(BrickContract.BrickTable.COLUMN_NAME, brick.getName());
        cv.put(BrickContract.BrickTable.COLUMN_SELL_TYPE, brick.getSellType());
        cv.put(BrickContract.BrickTable.COLUMN_GB_M2, brick.getGb_m2());
        cv.put(BrickContract.BrickTable.COLUMN_M2_IN_PALLET, brick.getM2_InPallet());
        cv.put(BrickContract.BrickTable.COLUMN_GB_IN_PALLET, brick.getGb_InPallet());
        cv.put(BrickContract.BrickTable.COLUMN_GB_IN_ROW, brick.getGbRinda());
        cv.put(BrickContract.BrickTable.COLUMN_SIZE_G, brick.getSizeG());
        cv.put(BrickContract.BrickTable.COLUMN_SIZE_A, brick.getSizeA());
        cv.put(BrickContract.BrickTable.COLUMN_SIZE_B, brick.getSizeB());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE, brick.getPrice());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_LUMI, brick.getPriceLumi());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_GRAY, brick.getPriceGray());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_GREEN, brick.getPriceGreen());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_YELLOW, brick.getPriceYellow());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_ORANGE, brick.getPriceOrange());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_RED, brick.getPriceRed());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_BLACK, brick.getPriceBlack());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_BROWN, brick.getPriceBrown());
        cv.put(BrickContract.BrickTable.COLUMN_PRICE_WHITE, brick.getPriceWhite());
        db.insert(BrickContract.BrickTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Brick> getBricks(){

        ArrayList<Brick> brickList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + BrickContract.BrickTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Brick brick = new Brick();
                brick.setId(c.getInt(c.getColumnIndex(BrickContract.BrickTable._ID)));
                try {
                    brick.setName(c.getString(c.getColumnIndex(BrickContract.BrickTable.COLUMN_NAME)));
                    brick.setSellType(c.getString(c.getColumnIndex(BrickContract.BrickTable.COLUMN_SELL_TYPE)));
                    brick.setGb_m2(c.getInt(c.getColumnIndex(BrickContract.BrickTable.COLUMN_GB_M2)));
                    brick.setM2_InPallet(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_M2_IN_PALLET)));
                    brick.setGb_InPallet(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_GB_IN_PALLET)));
                    brick.setGbRinda(c.getInt(c.getColumnIndex(BrickContract.BrickTable.COLUMN_GB_IN_ROW)));
                    brick.setSizeG(c.getInt(c.getColumnIndex(BrickContract.BrickTable.COLUMN_SIZE_G)));
                    brick.setSizeA(c.getInt(c.getColumnIndex(BrickContract.BrickTable.COLUMN_SIZE_A)));
                    brick.setSizeB(c.getInt(c.getColumnIndex(BrickContract.BrickTable.COLUMN_SIZE_B)));
                    brick.setPrice(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE)));
                    brick.setPriceLumi(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_LUMI)));
                    brick.setPriceGray(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_GRAY)));
                    brick.setPriceGreen(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_GREEN)));
                    brick.setPriceYellow(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_YELLOW)));
                    brick.setPriceOrange(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_ORANGE)));
                    brick.setPriceRed(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_RED)));
                    brick.setPriceBlack(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_BLACK)));
                    brick.setPriceBrown(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_BROWN)));
                    brick.setPriceWhite(c.getDouble(c.getColumnIndex(BrickContract.BrickTable.COLUMN_PRICE_WHITE)));

                } catch (Exception e) {
                    Log.e(TAG, "Try Cache Error(read rows ):\n " + e);
                }

                brickList.add(brick);
            } while (c.moveToNext());
        }

        c.close();
        return brickList;

//        Cursor c = db.query(
//                BrickContract.BrickTable.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null);
    }




}
