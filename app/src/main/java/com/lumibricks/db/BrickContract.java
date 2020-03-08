package com.lumibricks.db;

import android.provider.BaseColumns;

public final class BrickContract {

    public static class BrickTable implements BaseColumns {
        public static final String TABLE_NAME = "Brick";

        public static final String COLUMN_NAME = "Nosaukums";
        public static final String COLUMN_SELL_TYPE = "Sell_type";
        public static final String COLUMN_GB_M2 = "Gb_m2";
        public static final String COLUMN_M2_IN_PALLET = "m2_palete";
        public static final String COLUMN_GB_IN_PALLET = "Gb_palete";
        public static final String COLUMN_SIZE_G = "Izmers_G";
        public static final String COLUMN_SIZE_A = "Izmers_A";
        public static final String COLUMN_SIZE_B = "Izmers_B";
        public static final String COLUMN_PRICE = "Cena";
        public static final String COLUMN_PRICE_COLOR = "CenaKrasai";
    }

}