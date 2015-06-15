package com.example.alilachhab.medical_visionapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.alilachhab.medical_visionapp.db.TaskContract;
import com.example.alilachhab.medical_visionapp.db.TaskDBHelper;

public class TasksGenerator extends ActionBarActivity {

    private MainActivity main ;

    public TasksGenerator (MainActivity m)
    {
        main = m;
    }




}
