package com.rv.prog3tentmars;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements DataRequestAPI.DataAvailable {
    private final String TAG = this.getClass().getSimpleName();
    public final static String EXTRA_PIC_URL = "URL";
    public final static String EXTRA_FULLNAME = "fullName";
    public final static String EXTRA_ID = "camId";
    public final static String EXTRA_IDENTIFIER = "_id";
    private Cursor cursor;
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity onCreate Called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.mcontext = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to retrieve the latest rover photos? (Takes longer / press 'yes' if its first time launch the app)").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onDataAvailable(Cursor cursor) {
        Log.i(TAG, "MainActivity onDataAvailable Called");
        RecyclerView rvView = findViewById(R.id.RecycleAuth);
        RoverAdapter adapter = new RoverAdapter(cursor, new RoverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });

        rvView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rvView.setLayoutManager(new LinearLayoutManager(this));
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i(TAG, "DialogInterface OnClickListerer Called");
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Log.i(TAG, "OnDataAvailable case Positive Called");
                    String[] urls = new String[]{"https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=A5YHH4gu3YWJjdDDqveFiF4rWbYesKae8HVSYRNt"};
                    DataRequestAPI getAllPhotos = new DataRequestAPI(MainActivity.this, mcontext);
                    getAllPhotos.execute(urls);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.i(TAG, "OnDataAvailable case Negative Called");
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(mcontext.getDatabasePath("rover_db").getPath(),
                            null,
                            SQLiteDatabase.OPEN_READWRITE);

                    String query = "SELECT * FROM rover_db";
                    cursor = db.rawQuery(query, null);

                    RecyclerView rvView = (RecyclerView) findViewById(R.id.RecycleAuth);
                    RoverAdapter adapter = new RoverAdapter(cursor, new RoverAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                        }
                    });

                    rvView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    rvView.setLayoutManager(new LinearLayoutManager(mcontext));
                    break;
            }
        }
    };
}
