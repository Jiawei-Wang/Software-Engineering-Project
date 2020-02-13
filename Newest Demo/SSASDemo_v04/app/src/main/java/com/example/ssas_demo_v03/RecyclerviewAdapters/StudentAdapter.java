package com.example.ssas_demo_v03.RecyclerviewAdapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.CourseViewClassActivity;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

import com.example.ssas_demo_v03.R;
import com.example.ssas_demo_v03.StudentEditActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> implements Filterable {

    private List<StudentItem> mlist = new ArrayList<>();

    private List<StudentItem> mfulllist = new ArrayList<>();

    private Context mContext;

    private Cursor mCursor;

    public StudentAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

        while(mCursor.moveToNext()){

            int id =mCursor.getInt(mCursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_ID));
            String name = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_NAME));
            String email = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_EMAIL));
            String status = mCursor.getString(mCursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_STATUS));
//            byte[] in = mCursor.getBlob(mCursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_PIC));
//            Bitmap pic = BitmapFactory.decodeByteArray(in,0,in.length);
            mlist.add(new StudentItem(id,name,email,status));
        }
        if(!mlist.isEmpty()){
            mfulllist = new ArrayList<>(mlist);

        }

    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView idview;
        public TextView nameview;
        public TextView emailview;
        public TextView statusview;
        public ImageView picview;

        public Button mButtonEditStudent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            idview =itemView.findViewById(R.id.textview_student_id);
            nameview = itemView.findViewById(R.id.textview_student_name);
            emailview = itemView.findViewById(R.id.textview_student_email);
            statusview = itemView.findViewById(R.id.textview_student_status);
            //picview = itemView.findViewById(R.id.imageView_student);

            mButtonEditStudent =itemView.findViewById(R.id.button_edit_student);
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {
        if(position>=mlist.size()){
            return;
        }
        StudentItem item = mlist.get(position);


        holder.idview.setText(String.valueOf(item.Studentid));
        holder.nameview.setText(item.StudentName);
        holder.emailview.setText(item.StudentEmail);
        holder.statusview.setText(item.StudentStatus);
        //holder.picview.setImageBitmap(item.StudentPic);
        holder.mButtonEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, StudentEditActivity.class);
                intent.putExtra("StudentId", mlist.get(position).Studentid);
                intent.putExtra("StudentName", mlist.get(position).StudentName);
                intent.putExtra("StudentEmail", mlist.get(position).StudentEmail);
                intent.putExtra("StudentStatus", mlist.get(position).StudentStatus);
                mContext.startActivity(intent);
            }
        });


        holder.itemView.setTag(item.Studentid);
    }

    @Override
    public int getItemCount() {

        return mlist.size();
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


    @Override
    public Filter getFilter() {
        return studentFilter;
    }

    private Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StudentItem> filteredList = new ArrayList<>();

            if (constraint ==null || constraint.length()==0){
                filteredList.addAll(mfulllist);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (StudentItem item : mfulllist){
                    if(item.StudentName.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results =new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mlist.clear();
            mlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
class StudentItem{
    int Studentid;
    String StudentName;
    String StudentEmail;
    String StudentStatus;
//    Bitmap StudentPic;

    StudentItem(int id,String name,String email,String status){
        Studentid=id;
        StudentName=name;
        StudentEmail=email;
        StudentStatus=status;
//        StudentPic=pic;
    }
}
