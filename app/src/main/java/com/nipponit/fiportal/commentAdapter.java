package com.nipponit.fiportal;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class commentAdapter extends ArrayAdapter<commentModel> {

    public static class ViewHolder{
        TextView username;
        TextView crdlimt;
        TextView crdlimitlbl;
        TextView comment;
        TextView reason;
        TextView datetime;
        TextView level;

    }

    public commentAdapter(@NonNull Context context, ArrayList<commentModel> data) {
        super(context,R.layout.comment_model, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        commentModel data = getItem(position);
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_model,parent,false);

            viewHolder.username = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.crdlimt = (TextView) convertView.findViewById(R.id.lblcrdlimt);
            viewHolder.crdlimitlbl = (TextView) convertView.findViewById(R.id.lblcrdlimtx);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.lblcomment);
            viewHolder.reason = (TextView) convertView.findViewById(R.id.lblreason);
            viewHolder.datetime = (TextView) convertView.findViewById(R.id.lbldatetime);
            viewHolder.level = (TextView) convertView.findViewById(R.id.txtlevel);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.username.setText(data.getUsername());

        String level = data.getLEVEL();
        if(level.equals("CO")){
            viewHolder.crdlimitlbl.setText("Existing Credit Limit : ");
        }else if(level.equals("REP")){
            viewHolder.crdlimitlbl.setText("Requested Credit Limit : ");
        }else {
            viewHolder.crdlimitlbl.setText("Recommended Credit Limit : ");
        }

        viewHolder.crdlimt.setText(data.getCrdlimit());
        viewHolder.comment.setText(data.getComment());
        viewHolder.reason.setText(data.getReason());
        viewHolder.datetime.setText(data.getDatetime());
        viewHolder.level.setText(data.getLEVEL());

        return convertView;
    }
}
