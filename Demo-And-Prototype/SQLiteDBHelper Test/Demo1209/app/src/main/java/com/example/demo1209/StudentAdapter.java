package com.example.demo1209;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context mContext;

    private Cursor mCursor;

    public StudentAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        public TextView idview;
        public TextView nameview;
        public TextView emailview;
        public TextView statusview;
        public TextView picview;

        public StudentViewHolder(View itemView){
            super(itemView);
            idview =itemView.findViewById(R.id.textview_student_id);
            nameview = itemView.findViewById(R.id.textview_student_name);
            emailview = itemView.findViewById(R.id.textview_student_email);
            statusview = itemView.findViewById(R.id.textview_student_status);
            picview = itemView.findViewById(R.id.textview_student_pic);

        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME));

        String email = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_EMAIL));
        String status = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_STATUS));
        String pic = mCursor.getString(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_PIC));


        long id = mCursor.getLong(mCursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_ID));


        holder.idview.setText(String.valueOf(id));
        holder.nameview.setText(name);
        holder.emailview.setText(email);
        holder.statusview.setText(status);
        holder.picview.setText(pic);


        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount()  {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor!=null){
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor!=null){
            notifyDataSetChanged();
        }
    }
}
