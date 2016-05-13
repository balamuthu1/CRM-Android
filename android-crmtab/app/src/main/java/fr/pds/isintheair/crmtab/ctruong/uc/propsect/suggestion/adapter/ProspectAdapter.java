package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.Prospect;

/**
 * Created by Truong on 3/1/2016.
 */
public class ProspectAdapter extends ArrayAdapter<Prospect> {

    TextView textView;
    private Context        context;
    private List<Prospect> prospects;

    public ProspectAdapter(Context context, int resource, List<Prospect> object) {
        super(context, resource, object);
        this.context = context;
        this.prospects = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           view     = inflater.inflate(R.layout.listview_prospect, parent, false);
        Prospect       prospect = prospects.get(position);
        textView = (TextView) view.findViewById(R.id.tv_prospect);
        textView.setText(prospect.getName());
        return view;
    }
}
