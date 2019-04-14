package com.gyk.gyk301;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomVoiceListAdapter extends BaseAdapter {
    private List<String> fileNameList;
    private LayoutInflater layoutInflater;

    public CustomVoiceListAdapter(List<String> fileNameList, Context context) {
        this.fileNameList = fileNameList;
        this.layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fileNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row_view;
        row_view = layoutInflater.inflate(R.layout.custom_row_voice_layout,null);

        TextView textViewFileName = (TextView) row_view.findViewById(R.id.textViewVoiceFileName);
        textViewFileName.setText(fileNameList.get(position));
        return row_view;
    }
}
