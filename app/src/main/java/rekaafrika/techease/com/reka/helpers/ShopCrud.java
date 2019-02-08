package rekaafrika.techease.com.reka.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ShopCrud {

    private static SQLiteDatabase sqLiteDatabase;
    private Context context;

    public ShopCrud(Context context) {
        ShopDatabase database = new ShopDatabase(context);
        sqLiteDatabase = database.getWritableDatabase();
        this.context = context;
    }

    //inserting single products to cart
    public void insertSingleProduct(String product_id,String product_name,String product_image,String product_price,String quantity) {

        if (!checkProduct(product_id)) {
            ContentValues values = new ContentValues();
            values.put("PRODUCT_ID", product_id);
            values.put("PRODUCT_NAME",product_name);
            values.put("PRODUCT_IMAGE", product_image);
            values.put("PRODUCT_PRICE", product_price);
            values.put("PRODUCT_QUANTITY",quantity);
            sqLiteDatabase.insert("CART_TABLE", null, values);
            Toast.makeText(context, "Successful added to cart", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "already exist in cart", Toast.LENGTH_SHORT).show();
        }
    }



    //check for single products
    public boolean checkProduct(String product_id) {
        Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM CART_TABLE WHERE PRODUCT_ID = '" + product_id + "' ", null);
        boolean isItemAddChart = false;
        if (cursor.moveToFirst()) {
            isItemAddChart = true;
        }
        return isItemAddChart;

    }

    //fetching all products
    public Cursor getProducts() {
        String query = "SELECT * FROM CART_TABLE";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return cursor;
    }

    //deleting item from cart table
    public void delete(String productID) {
        this.sqLiteDatabase.delete("CART_TABLE", "PRODUCT_ID = '" + productID + "'", null);
        Toast.makeText(context, "delete item successful", Toast.LENGTH_SHORT).show();
    }

    public void UpDateTable(String strSerialNumber, String strItemsPrice,String strQuantity) {
        ContentValues cv = new ContentValues();
        cv.put("PRODUCT_PRICE", strItemsPrice);
        cv.put("PRODUCT_QUANTITY",strQuantity);
        String whereClause = "PRODUCT_ID = '" + strSerialNumber + "'";
        sqLiteDatabase.update("CART_TABLE", cv, whereClause, null);
        Toast.makeText(context, "value updated", Toast.LENGTH_SHORT).show();
    }
}
