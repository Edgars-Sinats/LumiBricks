package com.lumibricks.db;

import android.provider.BaseColumns;

public final class BrickContract {

    public static class BrickTable implements BaseColumns {
        public static final String TABLE_NAME = "Bricks";

        public static final String COLUMN_NAME = "Nosaukums";   //Name of brick
        public static final String COLUMN_SELL_TYPE = "Sell_type";
        public static final String COLUMN_GB_M2 = "Gb_m2";
        public static final String COLUMN_M2_IN_PALLET = "m2_palete";
        public static final String COLUMN_GB_IN_PALLET = "Gb_palete";
        public static final String COLUMN_GB_IN_ROW = "Gb_rinda";
        public static final String COLUMN_SIZE_G = "Izmers_G";
        public static final String COLUMN_SIZE_A = "Izmers_A";
        public static final String COLUMN_SIZE_B = "Izmers_B";
        public static final String COLUMN_PRICE = "Cena";
        public static final String COLUMN_PRICE_LUMI = "Cena_Lumi";
        public static final String COLUMN_PRICE_GRAY = "Cena_Peleks";
        public static final String COLUMN_PRICE_GREEN = "Cena_Zals";
        public static final String COLUMN_PRICE_YELLOW = "Cena_Dzeltens";
        public static final String COLUMN_PRICE_ORANGE = "Cena_Orandzs";
        public static final String COLUMN_PRICE_RED = "Cena_Sarkans";
        public static final String COLUMN_PRICE_BLACK = "Cena_Melns";
        public static final String COLUMN_PRICE_BROWN = "Cena_Bruns";
        public static final String COLUMN_PRICE_WHITE = "Cena_Balts";

    }

    public static class ItemsIrOrderTable implements BaseColumns {
        public static final String TABLE_NAME = "ItemsInOrder";

        public static final String COLUMN_ORDER_ID = "OrderID";
        public static final String COLUMN_ITEM_NAME = "ItemName";
        public static final String COLUMN_ITEM_AMOUNT = "ItemAmount";
        public static final String COLUMN_ITEM_PRICE = "ItemPrice";
        public static final String COLUMN_ITEM_PALETTES = "ItemPalettes";
        public static final String COLUMN_IN_STOCK = "InStock";

    }

    public static class UserTable implements BaseColumns {

        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USER_NAME = "Username";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_SURNAME = "Surname";
        public static final String COLUMN_E_MAIL = "eMail";
        public static final String COLUMN_MOBILE = "Mobile";
        public static final String COLUMN_BANK_NR = "BankNr";

    }
    public static class OrderTable implements BaseColumns {
        public static final String TABLE_NAME = "Orders";

        public static final String COLUMN_USER_ID = "userID";
        public static final String COLUMN_PALETTES = "Palettes";
        public static final String COLUMN_ORDER_PRICE = "OrderPrice";
        public static final String COLUMN_TIMESTAMP = "TimeStamp";
        public static final String COLUMN_ADDRESS_ID = "AddressID";

    }

    public static class AddressTable implements BaseColumns {
        public static final String TABLE_NAME = "Address";

        public static final String COLUMN_ADDRESS_NAME = "AddressName";
        public static final String COLUMN_COUNTRY = "Country";
        public static final String COLUMN_REGION = "Region";
        public static final String COLUMN_CITY = "City";
        public static final String COLUMN_STREET = "Street";
        public static final String COLUMN_HOUSE = "House";
        public static final String COLUMN_GEO_LOCATION = "GeoLocation";

    }


}