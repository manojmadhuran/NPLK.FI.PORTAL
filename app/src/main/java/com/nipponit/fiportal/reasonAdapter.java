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
import java.util.concurrent.TimeoutException;

public class reasonAdapter extends ArrayAdapter<reasonModel> {

    public static class ViewHolder{
        TextView reasonid;
        TextView reasontext;
    }

    public reasonAdapter(@NonNull Context context, ArrayList<reasonModel> data) {
        super(context,R.layout.reason_model,data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        reasonModel data = getItem(position);
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new
                    ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reason_model,parent,false);

            viewHolder.reasontext = convertView.findViewById(R.id.lbl_reason_text);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.reasontext.setText(data.getReasonText());

        return convertView;
    }
}
