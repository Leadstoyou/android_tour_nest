package com.example.tour_nest.test;

import com.example.tour_nest.model.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllTest {
    // Khởi tạo DatabaseReference
    private DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");

    // Phương thức để thêm các danh mục vào Firebase
    public void addCategories() {
        categoriesRef.child("cat1").setValue(new Category("Du lịch nội địa"));
        categoriesRef.child("cat2").setValue(new Category("Du lịch quốc tế"));
        categoriesRef.child("cat3").setValue(new Category("Du lịch khám phá"));
    }

    // Main method để chạy thử (nếu bạn muốn test trực tiếp)
    public static void main(String[] args) {
        AllTest test = new AllTest();
        test.addCategories();
    }
}