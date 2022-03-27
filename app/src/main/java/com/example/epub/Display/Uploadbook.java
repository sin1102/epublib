package com.example.epub.Display;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.epub.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import ru.bartwell.exfilepicker.ui.dialog.SortingDialog;

public class Uploadbook extends AppCompatActivity {

    MaterialCardView selectCard;
    TextView tvCourses;
    boolean [] selectCourse;
    ArrayList<Integer> courseList = new ArrayList<>();
    String [] courseArray = {"Action and adventure","Classic","Detective and Mystery","Horror","Romance","Science Fiction","Memoir","Self"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadbook);

        // initialize all views
        selectCard = findViewById(R.id.txtGenreUp);
        tvCourses = findViewById(R.id.tvCourse);
        selectCourse = new boolean[courseArray.length];

        selectCard.setOnClickListener(v ->{
            showCoursesDialog();
        });
    }

    void showCoursesDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Uploadbook.this);

        builder.setTitle("Select Courses");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(courseArray, selectCourse, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    courseList.add(which);
                }else {
                 courseList.remove(which);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //create string builder
                StringBuilder stringBuilder = new StringBuilder();
                for (int i=0; i < courseList.size(); i++){
                    stringBuilder.append(courseArray[courseList.get(i)]);

                    //check condition
                    if (i != courseList.size() -1){

                        //when i value not equal to course list size
                        //then add a comma
                        stringBuilder.append(", ");
                    }

                    //setting selected course to textview
                    tvCourses.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //clearing all selected course on clear all click
                for (int i = 0; i< selectCourse.length; i++){

                    selectCourse[i] = false;

                    courseList.clear();
                    tvCourses.setText("");
                }
            }
        });

        builder.show();
    }

}