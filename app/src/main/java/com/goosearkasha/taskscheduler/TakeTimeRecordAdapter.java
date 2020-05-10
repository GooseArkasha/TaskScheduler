package com.goosearkasha.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class TakeTimeRecordAdapter extends RecyclerView.Adapter<TakeTimeRecordAdapter.ViewHolder> {
    public static final String TAG = "RecordAdapter";

    private LayoutInflater inflater;
    private List<TakeTimeRecord> takeTimeRecords;
    private Context context;

    DBHelper dbHelper;

    TakeTimeRecordAdapter(Context context, List<TakeTimeRecord> takeTimeRecords) {
        Log.d(TAG, "QQ from constructor");
        this.takeTimeRecords = takeTimeRecords;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public TakeTimeRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "QQ from onCreateViewHolder");
        View view = inflater.inflate(R.layout.records_list_item, parent, false);
        return new TakeTimeRecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "QQ from onBindViewHolder");
        final TakeTimeRecord record = takeTimeRecords.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        holder.timeTextView.setText(dateFormat.format(record.getTime().getTime()));
        holder.commentTextView.setText(record.getComment());

        holder.updateRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddOrChangeRecord.class);
                intent.putExtra(TakeTimeRecord.class.getSimpleName(), record);
                intent.putExtra("mode", AddOrChangeRecord.UPDATE_RECORD);
                context.startActivity(intent);
            }
        });

        holder.removeRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                String where =  DBHelper.COLUMN_ID + " = " + record.getID();

                database.delete(DBHelper.TABLE_TAKE_TIME, where, null);

                Intent intent = new Intent(context, TaskActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return takeTimeRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView commentTextView;
        Button updateRecordButton;
        Button removeRecordButton;

        ViewHolder(View view) {
            super(view);
            Log.d(TAG, "QQ from ViewHolder Constructor");
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            commentTextView = (TextView) view.findViewById(R.id.commentTextView);
            updateRecordButton = (Button) view.findViewById(R.id.updateRecord);
            removeRecordButton = (Button) view.findViewById(R.id.removeRecord);

        }
    }
}
