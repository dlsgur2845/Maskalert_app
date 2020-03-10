package hyuk.com.maskalert_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hyuk.com.maskalert_app.R;
import hyuk.com.maskalert_app.object.Notice;

public class NoticeAdpater extends BaseAdapter {
    private List<Notice> list;
    private Context context;

    public NoticeAdpater(List<Notice> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.notice, null);
        TextView time = (TextView)v.findViewById(R.id.time);
        TextView summary = (TextView)v.findViewById(R.id.summary);

        time.setText(list.get(i).getDate()+"");
        summary.setText(list.get(i).getSummary());

        v.setTag(list.get(i).getDate()+"");
        return v;
    }
}
