package com.example.cobalagi;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Grafik_data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grafik_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi LineChart
        LineChart lineChart = findViewById(R.id.chart1);

        // Data untuk LineChart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1f, 2f));
        entries.add(new Entry(2f, 3f));
        entries.add(new Entry(3f, 5f));
        entries.add(new Entry(4f, 7f));
        entries.add(new Entry(5f, 11f));

        // Membuat LineDataSet
        LineDataSet dataSet = new LineDataSet(entries, "Data Set");
        dataSet.setColor(android.graphics.Color.BLUE);
        dataSet.setValueTextColor(android.graphics.Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Mengatur LineData
        LineData lineData = new LineData(dataSet);

        // Mengatur Data ke LineChart
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }
}







