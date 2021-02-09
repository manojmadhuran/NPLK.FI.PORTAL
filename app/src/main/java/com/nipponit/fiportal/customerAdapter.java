package com.nipponit.fiportal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class customerAdapter extends ArrayAdapter<CustomerModel> {

    ArrayList<CustomerModel> orgList;
    ArrayList<CustomerModel> NeworgList;
    ListFilter filter;

    public static class ViewHolder{
        TextView cuscode;
        TextView cusname;
        TextView crdlimit;
    }



    public customerAdapter(@NonNull Context context, ArrayList<CustomerModel> model) {
        super(context, R.layout.customer,model);
        this.orgList = new ArrayList<CustomerModel>();
        this.orgList.addAll(model);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        CustomerModel model = getItem(position);
        ViewHolder viewHolder = null;

        if(ConvertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ConvertView = inflater.inflate(R.layout.customer, parent, false);

            viewHolder.cuscode = (TextView) ConvertView.findViewById(R.id.lblcuscode);
            viewHolder.cusname
                    = (TextView) ConvertView.findViewById(R.id.lblcusname);
            viewHolder.crdlimit = (TextView) ConvertView.findViewById(R.id.lblcurrlimit);

            ConvertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) ConvertView.getTag();
        }

        viewHolder.cuscode.setText(model.getCcode());
        viewHolder.cusname.setText(model.getCname());
        viewHolder.crdlimit.setText(model.getClimit());
        return ConvertView;
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new ListFilter();
        }
        return filter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toUpperCase();
            FilterResults results=new FilterResults();
            if(constraint !=null && constraint.toString().length()>0){
                ArrayList<CustomerModel> filteredItems=new ArrayList<>();
                for(int i=0 , l = orgList.size();i<l;i++){
                    if((orgList.get(i).getCname().toUpperCase()).contains(constraint.toString().toUpperCase())){
                        CustomerModel List=new CustomerModel(
                                orgList.get(i).getCcode(),orgList.get(i).getCname(), orgList.get(i).getClimit());
                        filteredItems.add(List);
                    }
                }

                results.count=filteredItems.size();
                results.values=filteredItems;
            }
            else{
                synchronized (this){
                    results.values=orgList;
                    results.count=orgList.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            NeworgList=(ArrayList<CustomerModel>)results.values;
            notifyDataSetChanged();
            clear();
            for (int i=0,l=NeworgList.size();i<l;i++){
                add(NeworgList.get(i));
                notifyDataSetInvalidated();
            }
        }
    }

}
