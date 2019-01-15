package rekaafrika.techease.com.reka.dateModels;

import org.json.JSONArray;

import java.util.ArrayList;

public class AllProductsModel {

    public String product_id,title,price,image;
    public  ArrayList<AllProductsModel> arrayList;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<AllProductsModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<AllProductsModel> arrayList) {
        this.arrayList = arrayList;
    }
}
