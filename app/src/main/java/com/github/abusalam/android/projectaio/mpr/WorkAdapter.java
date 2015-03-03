package com.github.abusalam.android.projectaio.mpr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.abusalam.android.projectaio.R;

import java.util.ArrayList;

public class WorkAdapter  extends ArrayAdapter<Work> {
    ArrayList<Work> Works;
    int resource;

    public WorkAdapter(Context context, int resource, ArrayList<Work> Works) {
        super(context, resource, Works);
        this.resource = resource;
        this.Works = Works;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout WorkView;
        Work mWork = getItem(position);

        if (convertView == null) {
            WorkView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, WorkView, true);
        } else {
            WorkView = (LinearLayout) convertView;
        }

        Long lngWorkID = mWork.getWorkID();
        String txtWorkName = mWork.getWorkName();
        Long lngBalance=mWork.getBalance();

        TextView tvSchemeID = (TextView) WorkView.findViewById(R.id.tvWorkID);
        TextView tvSchemeName = (TextView) WorkView.findViewById(R.id.tvWork);

        tvSchemeID.setText(lngBalance.toString()+"%");
        tvSchemeName.setText(lngWorkID.toString() + ":" + txtWorkName);

        return WorkView;
    }

}
