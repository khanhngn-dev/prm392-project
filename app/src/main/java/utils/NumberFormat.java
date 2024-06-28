package utils;

import android.annotation.SuppressLint;

public class NumberFormat {
    @SuppressLint("DefaultLocale")
    public static String thousandSeparator(int number) {
        return String.format("%,d", number);
    }

    @SuppressLint("DefaultLocale")
    public static String vndMoney(int number) {
        return String.format("%,d VND", number);
    }
}
