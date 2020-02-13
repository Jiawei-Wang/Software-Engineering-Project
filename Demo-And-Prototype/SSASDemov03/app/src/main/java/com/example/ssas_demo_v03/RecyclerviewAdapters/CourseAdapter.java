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

import com.example.ssas_demo_v03.CourseEditActivity;
import com.example.ssas_demo_v03.CoursePageActivity;
import com.example.ssas_demo_v03.CourseViewClassActivity;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.R;
import com.example.ssas_demo_v03.UserViewCourseActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements Filterable {

    private List<CourseItem> mlist = new ArrayList<>();

    private List<CourseItem> mfulllist = new ArrayList<>();

    private Context mContext;

    private Cursor mCursor;

    public CourseAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

        while (mCursor.moveToNext()) {

            int id = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_ID));
            int teacherid = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_TEACHER_ID));
            String name = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_NAME));
            String status = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_STATUS));
            int classnumber = mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_NUMBER_OF_CLASS));

            mlist.add(new CourseItem(id, teacherid, name, status, classnumber));
        }
        if (!mlist.isEmpty()) {
            mfulllist = new ArrayList<>(mlist);

        }

    }


    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView idview;
        public TextView teacheridview;
        public TextView coursenameview;
        public TextView statusview;
        public TextView classnumberview;

        public Button mButtonEditCourse;
        public Button mButtonViewCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            idview = itemView.findViewById(R.id.textview_course_id);
            teacheridview = itemView.findViewById(R.id.textview_course_teacher_id);
            coursenameview = itemView.findViewById(R.id.textview_course_name);
            statusview = itemView.findViewById(R.id.textview_course_status);
            classnumberview = itemView.findViewById(R.id.textview_class_number);

            mButtonEditCourse = itemView.findViewById(R.id.button_edit_course);
            mButtonViewCourse = itemView.findViewById(R.id.button_view_course);
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }


    //TODO Check if any problem caused by setting position to final.
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, final int position) {
        if (position >= mlist.size()) {
            return;
        }
        CourseItem item = mlist.get(position);


        holder.idview.setText(String.valueOf(item.CourseId));
        holder.teacheridview.setText(String.valueOf(item.TeacherId));
        holder.coursenameview.setText(item.CourseName);
        holder.statusview.setText(item.CourseStatus);
        holder.classnumberview.setText(String.valueOf(item.ClassNumber));

        holder.mButtonViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CourseViewClassActivity.class);
                intent.putExtra("CourseId", mlist.get(position).CourseId);
                intent.putExtra("TeacherId", mlist.get(position).TeacherId);
                intent.putExtra("CourseName", mlist.get(position).CourseName);
                intent.putExtra("CourseStatus", mlist.get(position).CourseStatus);
                intent.putExtra("ClassNumber", mlist.get(position).ClassNumber);
                mContext.startActivity(intent);
            }
        });

        holder.mButtonEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CourseEditActivity.class);
                intent.putExtra("CourseId", mlist.get(position).CourseId);
                intent.putExtra("TeacherId", mlist.get(position).TeacherId);
                intent.putExtra("CourseName", mlist.get(position).CourseName);
                intent.putExtra("CourseStatus", mlist.get(position).CourseStatus);
                intent.putExtra("ClassNumber", mlist.get(position).ClassNumber);
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
        return courseFilter;
    }

    private Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CourseItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mfulllist);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CourseItem item : mfulllist) {
                    if (item.CourseName.toLowerCase().contains(filterPattern) || String.valueOf(item.CourseId).contains(filterPattern)) {
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

class CourseItem {
    int CourseId;
    int TeacherId;
    String CourseName;
    String CourseStatus;
    int ClassNumber;

    CourseItem(int id, int teacherid, String name, String status, int classnumber) {
        CourseId = id;
        TeacherId = teacherid;
        CourseName = name;
        CourseStatus = status;
        ClassNumber = classnumber;
    }
}
