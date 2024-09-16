package com.example.cobalagi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView kelembapanTextView, suhuTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Menginisialisasi referensi database Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Mengatur layout activity
        setContentView(R.layout.activity_main);

        // Menghubungkan TextView dengan tampilan di XML
        kelembapanTextView = findViewById(R.id.kelembapanTextView);
        suhuTextView = findViewById(R.id.suhuTextView);

        // Mengambil data dari Firebase
        ambilDataDariFirebase();

        // Mengatur padding untuk tampilan dengan insets sistem (jika diperlukan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fungsi untuk mengambil data dari Firebase
    private void ambilDataDariFirebase() {
        // Mendengarkan perubahan data di node "Motion"
        mDatabase.child("Motion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Memastikan node "Motion" ada sebelum mengambil datanya
                if (dataSnapshot.exists()) {
                    // Mengambil nilai kelembapan dan suhu
                    Integer kelembapan = dataSnapshot.child("kelembapan").getValue(Integer.class);
                    Integer suhu = dataSnapshot.child("suhu").getValue(Integer.class);

                    // Menampilkan data di TextView
                    if (kelembapan != null) {
                        kelembapanTextView.setText(kelembapan + "%");
                    } else {
                        kelembapanTextView.setText("Kelembapan: Data tidak tersedia");
                    }

                    if (suhu != null) {
                        suhuTextView.setText(suhu + "°C");
                    } else {
                        suhuTextView.setText("Suhu: Data tidak tersedia");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log jika ada kesalahan dalam mengambil data
                Log.w("MainActivity", "Gagal mengambil data.", databaseError.toException());
            }
        });
    }

    public void onCLikgoToGrafik (View view) {
        Intent intent = new Intent(this, grafik_data.class);
        startActivity(intent);
    }

}