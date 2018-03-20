package com.rv.prog3tentmars;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by rickv on 13-3-2018.
 */

public class RoverAdapter extends RecyclerView.Adapter<RoverAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final OnItemClickListener listener;
    private static Cursor cursor;

    public RoverAdapter(Cursor cursor, OnItemClickListener listener) {
        Log.i(TAG, "RoverAdapter Constructor Called");
        this.listener = listener;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "RoverAdapter onCreateViewHolder Called");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "RoverAdapter onBindViewHolder Called");
        holder.bind(listener, position, holder);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "RoverAdapter getItemCount Called");
        return cursor.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewList;
        private TextView textViewList;

        //private ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            this.imageViewList = (ImageView) itemView.findViewById(R.id.photoRow);
            this.textViewList = (TextView) itemView.findViewById(R.id.textRow);
        }


        public void bind(final OnItemClickListener listener, final int position, final ViewHolder holder) {
            if (!cursor.moveToPosition(position)) {
                return;
            }

            String fullName = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_FULLNAME));
            String URL = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_PIC_URL));
            final String id = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_ID));
            final String _id = cursor.getString(cursor.getColumnIndex(MainActivity.EXTRA_IDENTIFIER));


            holder.textViewList.setText("IMAGE ID: " + id);
            Picasso.get().load(URL).into(holder.imageViewList);


            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                    Intent intent = new Intent(itemView.getContext(), DetailedActivity.class);
                    intent.putExtra(MainActivity.EXTRA_IDENTIFIER, _id);
                    itemView.getContext().startActivity(intent);
                }

            });

        }

    }
}