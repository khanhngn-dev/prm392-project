package com.example.project.helper;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.project.R;
import com.example.project.model.Product;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertProduct(Product item) {
        ArrayList<Product> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
        int count = 0;
        for (Product product : listfood) {
            count += product.getNumberInCart();
        }
        sendNotification(count);
    }

    public ArrayList<Product> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<Product> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberInCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
        // Loop over listFood and get the total number of items
        int count = 0;
        for (Product product : listfood) {
            count += product.getNumberInCart();
        }
        sendNotification(count);
    }

    public void clearCart(ArrayList<Product> listCart) {
        listCart.clear();
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListObject("CartList", listCart);
        sendNotification(0);
    }

    public void plusItem(ArrayList<Product> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
        int count = 0;
        for (Product product : listfood) {
            count += product.getNumberInCart();
        }
        sendNotification(count);
    }

    public Double getTotalFee() {
        ArrayList<Product> listfood2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getPrice() * listfood2.get(i).getNumberInCart());
        }
        return fee;
    }

    private void sendNotification(int count) {
        if (!NotificationManagerCompat.from(this.context).areNotificationsEnabled()) {
            Toast.makeText(this.context, "Notification is disabled", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "cart")
                .setSmallIcon(R.drawable.baseline_shopping_cart_24)
                .setContentTitle("Cart")
                .setContentText(count == 0 ? "Your cart is empty" : "You have " + count + " items in your cart")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId("cart")
                .setNumber(count);

        NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }
}
