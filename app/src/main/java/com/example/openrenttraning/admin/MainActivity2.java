package com.example.openrenttraning.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.openrenttraning.MainActivity;
import com.example.openrenttraning.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    CardView add, viewSubmission;

    private int mYear, mMonth, mDay;
    int strtHour;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        add = findViewById(R.id.add);
        viewSubmission = findViewById(R.id.viewsubmission);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
                builder.setTitle("Add Session Details");

                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
                builder.setView(customLayout);
                CardView add = customLayout.findViewById(R.id.add);
                CardView adddate = customLayout.findViewById(R.id.adddate);
                TextView date = customLayout.findViewById(R.id.date);

                CardView addstime = customLayout.findViewById(R.id.addstime);
                TextView stime = customLayout.findViewById(R.id.stime);
                CardView addetime = customLayout.findViewById(R.id.addetime);
                TextView etime = customLayout.findViewById(R.id.etime);

                AlertDialog dialog = builder.create();

                adddate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity2.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                                        date.setText(format.format(calendar.getTime()));
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                addstime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar c = Calendar.getInstance();
                        int mHour, mMinute;
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity2.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        strtHour = hourOfDay;
                                        String hour = String.valueOf(hourOfDay), min = String.valueOf(minute);
                                        if(String.valueOf(hourOfDay).length() < 2){
                                            hour  = "0" + hour ;
                                        }if(String.valueOf(minute).length() < 2){
                                            min  = "0" + min ;
                                        }
                                        stime.setText(hour + ":" + min);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });

                addetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();
                        int mHour, mMinute;
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity2.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Log.e("SAS",  String.valueOf(hourOfDay));
                                        if(strtHour<hourOfDay){
                                            String hour = String.valueOf(hourOfDay), min = String.valueOf(minute);
                                            if(String.valueOf(hourOfDay).length() < 2){
                                                hour  = "0" + hour ;
                                            }if(String.valueOf(minute).length() < 2){
                                                min  = "0" + min ;
                                            }
                                            etime.setText(hour + ":" + min);
                                        }else{
                                            Toast.makeText(MainActivity2.this, "There Should be at least one Hour Gap", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dates = date.getText().toString();
                        String stimes = stime.getText().toString();
                        String etimes = etime.getText().toString();
                        Log.e(stimes, etimes);
                        if(stimes.equals("00:00")||etimes.equals("00:00")||dates.equals("00/00/00")){
                            Toast.makeText(MainActivity2.this, "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent i = new Intent(MainActivity2.this, Add_Sessions.class);
                            i.putExtra("StartTime", stimes);
                            i.putExtra("EndTime", etimes);
                            i.putExtra("Dates", dates);
                            startActivity(i);
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();
            }
        });



        viewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialoga = new DatePickerDialog(MainActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                Intent i = new Intent(MainActivity2.this, SubmissionActivity.class);
                                i.putExtra("DATE", format.format(calendar.getTime()));
                                startActivity(i);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialoga.show();
            }
        });
    }
};

