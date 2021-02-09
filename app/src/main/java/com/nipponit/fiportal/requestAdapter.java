package com.nipponit.fiportal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class requestAdapter extends ArrayAdapter<requestModel> {

    public static class ViewHolder{
        TextView cusname;
        TextView crdlimt;
        TextView status;
        TextView datetime;
        TextView Rtype;
        TextView action;
    }

    public requestAdapter(@NonNull Context context, ArrayList<requestModel> data) {
        super(context, R.layout.request_model,data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        requestModel data = getItem(position);
        ViewHolder viewHolder = null;

        if(convertView == null) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.request_model,parent,false);

        viewHolder.cusname = (TextView)convertView.findViewById(R.id.lblcusname);
        viewHolder.crdlimt = (TextView)convertView.findViewById(R.id.lblenhance);
        viewHolder.status = (TextView)convertView.findViewById(R.id.lblstatus);
        viewHolder.datetime = (TextView)convertView.findViewById(R.id.lbldatetime);
        viewHolder.Rtype = (TextView)convertView.findViewById(R.id.lblRtype);
        viewHolder.action = (TextView)convertView.findViewById(R.id.lblaction);

        convertView.setTag(viewHolder);
    }else{
        viewHolder = (ViewHolder)convertView.getTag();
    }

        viewHolder.cusname.setText(data.getCusname());
        viewHolder.crdlimt.setText(data.getCrdlimit());
        viewHolder.status.setText(data.getStatus());
        viewHolder.datetime.setText(data.getDatetime());
        viewHolder.Rtype.setText(data.getType());
        viewHolder.action.setText(data.getAction());
        return convertView;
    }



}
