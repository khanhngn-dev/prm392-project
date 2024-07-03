package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project.adapter.CartAdapter;
import com.example.project.databinding.ActivityCartBinding;
import com.example.project.helper.ChangeNumberItemsListener;
import com.example.project.helper.ManagmentCart;

public class Cart extends Base {
    ActivityCartBinding binding;
    private ManagmentCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
//        Comment 2 cai duoi no se hien
        calculatorCart();
        initCartList();
    }

    private void initCartList() {
        if(managementCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double shopping = 10;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100 ) / 100 ;
        double total = Math.round((managementCart.getTotalFee() + shopping) * 100) / 100 ;

        binding.subTotalTxt.setText("VND" + itemTotal);
        binding.shoppingTxt.setText("VND" + shopping);
        binding.totalTxt.setText("VND" + total);
    }
}