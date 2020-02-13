package com.example.ssas_demo_v03.RecyclerviewAdapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.DataBaseConnectors.DataBaseContract;
import com.example.ssas_demo_v03.R;

import java.util.ArrayList;
import java.util.List;

public class AttendanceEditorAdapter extends RecyclerView.Adapter<AttendanceEditorAdapter.AttendanceEditorViewHolder> implements Filterable {

    private List<AttendanceItem> mlist = new ArrayList<>();

    private List<AttendanceItem> mfulllist = new ArrayList<>();

    private Context mContext;

    private Cursor mCursor;

    public AttendanceEditorAdapter(Context context, Cursor cursor) {

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


    public class AttendanceEditorViewHolder extends RecyclerView.ViewHolder {
        public TextView studentnameview;
        public Spinner spinnerattendance;


        public AttendanceEditorViewHolder(@NonNull View itemView) {
            super(itemView);
            studentnameview = itemView.findViewById(R.id.testView_student_name_in_edit);
            spinnerattendance = itemView.findViewById(R.id.spinner_student_attendance);
        }
    }

    @NonNull
    @Override
    public AttendanceEditorAdapter.AttendanceEditorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_attendance_edit, parent, false);
        return new AttendanceEditorAdapter.AttendanceEditorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceEditorAdapter.AttendanceEditorViewHolder holder, final int position) {
        if (position >= mlist.size()) {
            return;
        }
        AttendanceItem item = mlist.get(position);


        holder.studentnameview.setText(String.valueOf(item.StudentName));



        ArrayList<String>list=new ArrayList<String>();
        list.add("Attend");
        list.add("Not Attend");
        SpinnerAdapter adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,list);
        holder.spinnerattendance.setAdapter(adapter);
        if (item.AttendanceStatus.equals("Attend")){
            holder.spinnerattendance.setSelection(0);
        }
        else {
            holder.spinnerattendance.setSelection(1);
        }

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
