package com.jikexueyuan.accountingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class ListViewAdapter  extends BaseAdapter {

    private LinkedList<RecordBean> records = new LinkedList<>();

    private LayoutInflater mInflater;
    private Context mContext;

    public ListViewAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<RecordBean> records){
        this.records = records;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null){
            view = mInflater.inflate(R.layout.cell_list_view,null);

            RecordBean recordBean = (RecordBean) getItem(i);
            holder = new ViewHolder(view,recordBean);

            view.setTag(holder);

        }else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}

class ViewHolder{


    TextView remarkTV;
    TextView amountTV;
    TextView timeTV;
    ImageView categoryIcon;


    public ViewHolder(View itemView, RecordBean record){
        remarkTV = (TextView) itemView.findViewById(R.id.textView_remark);
        amountTV = (TextView) itemView.findViewById(R.id.textView_amount);
        timeTV = (TextView) itemView.findViewById(R.id.textView_time);
        categoryIcon = (ImageView) itemView.findViewById(R.id.imageView_category);

        remarkTV.setText(record.getRemark());

        if (record.getType() == 1){
            amountTV.setText("- " + record.getAmount());
        }else {
            amountTV.setText("+ " + record.getAmount());
        }

        timeTV.setText(DateUtil.getFormattedTime(record.getTimeStamp()));

        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
    }


}
