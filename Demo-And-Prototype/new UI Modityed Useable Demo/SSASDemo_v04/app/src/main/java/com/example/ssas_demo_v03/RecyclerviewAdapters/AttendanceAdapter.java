package com.example.ssas_demo_v03.RecyclerviewAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.DataBaseConnectors.DataBaseContract;
import com.example.ssas_demo_v03.R;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> implements Filterable {

    private List<AttendanceItem> mlist = new ArrayList<>();

    private List<AttendanceItem> mfulllist = new ArrayList<>();

    private Context mContext;

    private Cursor mCursor;

    public AttendanceAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

        while (mCursor.moveToNext()) {

            int recordid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.StudentClassEntry.COLUMN_ID));
            int studentid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.StudentClassEntry.COLUMN_STUDENT_ID));
            String studentname = mCursor.getString(mCursor.getColumnIndex("StudentName"));
            int classid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.StudentClassEntry.COLUMN_CLASS_ID));
            String attendanceStatus = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.StudentClassEntry.COLUMN_STATUS));

            mlist.add(new AttendanceItem(recordid, studentid, classid, studentname, attendanceStatus));
        }
        if (!mlist.isEmpty()) {
            mfulllist = new ArrayList<>(mlist);

        }

    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        public TextView studentnameview;
        public TextView attendanceview;


        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            studentnameview = itemView.findViewById(R.id.testView_student_name_in_attendance);
            attendanceview = itemView.findViewById(R.id.testView_attendance_status);
        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_attendance, parent, false);
        return new AttendanceAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, final int position) {
        if (position >= mlist.size()) {
            return;
        }
        AttendanceItem item = mlist.get(position);


        holder.studentnameview.setText(String.valueOf(item.StudentName));
        holder.attendanceview.setText(String.valueOf(item.AttendanceStatus));


        holder.itemView.setTag(item.Recordid);
    }

    @Override
    public int getItemCount() {

        return mlist.size();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }


    @Override
    public Filter getFilter() {
        return attendanceFilter;
    }

    private Filter attendanceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AttendanceItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mfulllist);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AttendanceItem item : mfulllist) {
                    if (item.StudentName.toLowerCase().contains(filterPattern) || String.valueOf(item.Studentid).contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mlist.clear();
            mlist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}

class AttendanceItem {
    int Recordid;
    int Studentid;
    int Classid;
    String StudentName;
    String AttendanceStatus;

    AttendanceItem(int recordid, int studentid, int classid, String studentName, String attendanceStatus) {
        Recordid = recordid;
        Studentid = studentid;
        Classid = classid;
        StudentName = studentName;
        AttendanceStatus = attendanceStatus;
    }

}