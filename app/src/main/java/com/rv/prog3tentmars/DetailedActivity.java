package com.rv.prog3tentmars;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private SQLiteDatabase myDb;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "DetailedActivity onCreate Called");
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_detailed);
        DetailedActivity.ViewHolder viewHolder;
        viewHolder = new DetailedActivity.ViewHolder();

        String q = (String) extras.getSerializable(MainActivity.EXTRA_IDENTIFIER);

        DictionaryOpenhelper dictionaryOpenhelper = new DictionaryOpenhelper(this);
        myDb = dictionaryOpenhelper.getReadableDatabase();
        String query = "SELECT * FROM rover_db WHERE _id = " + q;
        cursor = myDb.rawQuery(query, null);

        if (!cursor.moveToPosition(0)) {
            Log.e(TAG, "Error cursor position 0");
            return;
        }

        String _id = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_IDENTIFIER));
        String fullPic = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_PIC_URL));
        String fullName = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_FULLNAME));

        viewHolder.fullName.setText(fullName);
        Picasso.get().load(fullPic).into(viewHolder.fullImage);

    }

    public class ViewHolder {
        public ImageView fullImage = (ImageView) findViewById(R.id.fullImage);
        public TextView fullName = (TextView) findViewById(R.id.fullName);
    }
}