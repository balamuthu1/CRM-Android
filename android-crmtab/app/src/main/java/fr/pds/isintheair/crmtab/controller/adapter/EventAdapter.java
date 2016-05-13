package fr.pds.isintheair.crmtab.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.Data;

/**
 * Created by Muthu on 06/02/2016.
 */
public class EventAdapter extends ArrayAdapter<Data> {

    private Context context;
    private List<Data> objects;
    TextView title;
    public EventAdapter(Context context, List<Data> objects) {
        super(context, R.layout.event_list_layout, objects);
        this.context = context;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.event_list_layout, parent, false);

        //get views
        title = (TextView) rowView.findViewById(R.id.textTitle);


        title.setText(" " + objects.get(position).getName());




        return  rowView;
    }
}