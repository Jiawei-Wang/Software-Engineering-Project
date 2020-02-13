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

import com.example.ssas_demo_v03.ClassEditActivity;
import com.example.ssas_demo_v03.ClassViewActivity;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.R;
import com.example.ssas_demo_v03.StudentEditActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> implements Filterable {
    private List<ClassItem> mlist = new ArrayList<>();

    private List<ClassItem> mfulllist = new ArrayList<>();

    private Context mContext;

    private Cursor mCursor;

    public ClassAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

        while (mCursor.moveToNext()) {

            int classid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_ID));
            int courseid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_COURSE_ID));
            String coursename = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_COURSE_NAME));
            String datetime = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_DATETIME));
            String classroom = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_CLASSROOM));

            mlist.add(new ClassItem(classid, courseid, coursename, datetime, classroom));
        }
        if (!mlist.isEmpty()) {
            mfulllist = new ArrayList<>(mlist);

        }

    }


    public class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView coursenameview;
        public TextView classdatetimeview;
        public TextView classroomview;

        public Button editclassbutton;
        public Button viewattendancebutton;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            coursenameview = itemView.findViewById(R.id.textview_class_course_name);
            classdatetimeview = itemView.findViewById(R.id.textview_class_datetime);
            classroomview = itemView.findViewById(R.id.textview_class_classroom);

            viewattendancebutton= itemView.findViewById(R.id.button_to_view_class);
            editclassbutton = itemView.findViewById(R.id.button_to_edit_attendance_for_class);
        }
    }

    @NonNull
    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_class, parent, false);
        return new ClassAdapter.ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ClassViewHolder holder, final int position) {
        if (position >= mlist.size()) {
            return;
        }
        ClassItem item = mlist.get(position);


        holder.coursenameview.setText(String.valueOf(item.CourseName));
        holder.classdatetimeview.setText(String.valueOf(item.DateTime));
        holder.classroomview.setText(item.Classroom);


        holder.viewattendancebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ClassViewActivity.class);
                intent.putExtra("ClassId", mlist.get(position).ClassId);
                intent.putExtra("CourseId", mlist.get(position).CourseId);
                intent.putExtra("CourseName", mlist.get(position).CourseName);
                intent.putExtra("DateTime", mlist.get(position).DateTime);
                intent.putExtra("Classroom", mlist.get(position).Classroom);
                mContext.startActivity(intent);
            }
        });

        holder.editclassbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ClassEditActivity.class);
                intent.putExtra("ClassId", mlist.get(position).ClassId);
                intent.putExtra("CourseId", mlist.get(position).CourseId);
                intent.putExtra("CourseName", mlist.get(position).CourseName);
                intent.putExtra("DateTime", mlist.get(position).DateTime);
                intent.putExtra("Classroom", mlist.get(position).Classroom);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setTag(item.CourseId);
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
        return classFilter;
    }

    private Filter classFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ClassItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mfulllist);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ClassItem item : mfulllist) {
                    if (item.CourseName.toLowerCase().contains(filterPattern)
                            || String.valueOf(item.CourseId).contains(filterPattern)
                            || String.valueOf(item.DateTime).contains(filterPattern)
                            || String.valueOf(item.Classroom).contains(filterPattern)) {
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

class ClassItem {
    int ClassId;
    int CourseId;
    String CourseName;
    String DateTime;
    String Classroom;

    ClassItem(int classid, int courseid, String coursename, String datetime, String classroom) {
        ClassId = classid;
        CourseId = courseid;
        CourseName = coursename;
        DateTime = datetime;
        Classroom = classroom;
    }
}

