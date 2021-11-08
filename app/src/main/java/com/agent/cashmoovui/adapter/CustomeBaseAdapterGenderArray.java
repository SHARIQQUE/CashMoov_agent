package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agent.cashmoovui.R;

import java.util.ArrayList;


public class CustomeBaseAdapterGenderArray extends BaseAdapter {

    Context context;
    String[] arrayName_genderName;
    LayoutInflater inflter;

    public CustomeBaseAdapterGenderArray(Context applicationContext, String[] arrayName_genderName) {


        this.context = applicationContext;
        this.arrayName_genderName = arrayName_genderName;

//        this.preferences = preferences;
//        Collections.sort(arrayList);
//        if(arrayList.size()>0) {
//            HashSet<ReChargeAmountModel> onlineMart = new HashSet<ReChargeAmountModel>(arrayList);
//            Iterator<ReChargeAmountModel> strIterator = onlineMart.iterator();
//            while (strIterator.hasNext()) {
//                System.out.println(strIterator.next());
//            }
//            TreeSet<ReChargeAmountModel> ts = new TreeSet<ReChargeAmountModel>(onlineMart);
//            Iterator<ReChargeAmountModel> ascSorting = ts.iterator();
//            while (ascSorting.hasNext()) {
//                System.out.println(ascSorting.next());
//            }
//        }
//                new ArrayList<ReChargeAmountModel>();
//        Collections.sort(arrayList, new StudentDateComparator());

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayName_genderName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.row_item_simple, null);
        TextView names = (TextView) view.findViewById(R.id.textViewName);

        names.setText(arrayName_genderName[i]);

        return view;
    }
}

