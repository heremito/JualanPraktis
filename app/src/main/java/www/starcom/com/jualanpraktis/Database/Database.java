package www.starcom.com.jualanpraktis.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import www.starcom.com.jualanpraktis.SubKategori.order;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "pesan.db";
    private static final int DB_VER = 1 ;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<order> getPesan(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","IdProduk","NamaProduk","jumlah","harga","berat"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c =  qb.query(db,sqlSelect,null,null,null,null,null,null);

        final List<order> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do{
                result.add(new order(c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("IdProduk")),
                        c.getString(c.getColumnIndex("NamaProduk")),
                        c.getString(c.getColumnIndex("harga")),
                        c.getString(c.getColumnIndex("jumlah")),
                        c.getString(c.getColumnIndex("berat"))
                        ));
            }while (c.moveToNext());
        }

        return result;
    }

    public void addToChart(order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(IdProduk,NamaProduk,harga,jumlah,berat) VALUES ('%s','%s','%s','%s','%s');",
                order.getIdProduk(),
                order.getNamaProduk(),
                order.getHarga(),
                order.getJumlah(),
                order.getBerat());
        db.execSQL(query);
    }

    public void cleanChart(String id){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ID="+id);
        db.execSQL(query);
    }

    public void cleanAllChart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail ");
        db.execSQL(query);
    }
}
