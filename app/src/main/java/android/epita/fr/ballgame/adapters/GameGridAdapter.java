package android.epita.fr.ballgame.adapters;

import android.app.Activity;
import android.content.Context;
import android.epita.fr.ballgame.R;
import android.epita.fr.ballgame.models.GridModel;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GameGridAdapter  extends ArrayAdapter<GridModel>
{
    //These are adapter to populate the values to the grid as per Android development guidelines
    Context context;
    int layoutResourceId;
    ArrayList<GridModel> data = new ArrayList<GridModel>();

    public GameGridAdapter(Context context, int layoutResourceId,
                                 ArrayList<GridModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.txtTitle.setBackgroundColor(Color.WHITE);
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.txtTitle.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtTitle.setEnabled(false);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        GridModel item = data.get(position);
        holder.txtTitle.setText(item.getGridValue()+" ");
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
    }
}
