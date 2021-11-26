package com.agent.cashmoovui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agent.cashmoovui.R;


public class RecordAdapter extends BaseAdapter {
    Context context;
    String[] recordArray;
    LayoutInflater inflter;
    SharedPreferences preferences;

    public RecordAdapter(Context applicationContext, String[] recordArray) {
        this.context = applicationContext;
        this.recordArray = recordArray;
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
        return recordArray.length;
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
        view = inflter.inflate(R.layout.record_item, null);
        TextView names = (TextView) view.findViewById(R.id.textViewName);
        names.setText(recordArray[i]);
//        names.setTextColor(context.getResources().getColor(R.color.textcolor_login));
        return view;
    }
}

