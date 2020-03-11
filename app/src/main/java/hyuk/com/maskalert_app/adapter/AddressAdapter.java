package hyuk.com.maskalert_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hyuk.com.maskalert_app.R;
import hyuk.com.maskalert_app.object.Addr;
import hyuk.com.maskalert_app.object.Notice;

public class AddressAdapter extends BaseAdapter {
    private List<Addr> addrList;
    private Context context;

    public AddressAdapter(List<Addr> addrList, Context context) {
        this.addrList = addrList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return addrList.size();
    }

    @Override
    public Object getItem(int i) {
        return addrList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.addr, null);
        TextView jibunaddress = (TextView)v.findViewById(R.id.jibunaddress);

        jibunaddress.setText(addrList.get(i).getJibunAddress());

        v.setTag(addrList.get(i).getJibunAddress());
        return v;
    }
}
