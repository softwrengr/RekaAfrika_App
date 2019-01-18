package rekaafrika.techease.com.reka.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShopDatabase extends SQLiteOpenHelper {

    private static String DB_NAME = "SHOP_DB";
    public static int DB_VERSION = 1;

    public ShopDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE CART_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCT_ID,PRODUCT_NAME,PRODUCT_IMAGE,PRODUCT_PRICE)";
        sqLiteDatabase.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CART_TABLE");
        onCreate(db);
    }
}
