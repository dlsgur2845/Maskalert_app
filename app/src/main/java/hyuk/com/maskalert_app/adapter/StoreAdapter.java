package hyuk.com.maskalert_app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import hyuk.com.maskalert_app.R;
import hyuk.com.maskalert_app.object.Store;

public class StoreAdapter extends BaseAdapter {
    private List<Store> storeList;
    private Context context;

    public StoreAdapter(List<Store> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int i) {
        return storeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.store, null);
        //
        //
        //
        return v;
    }
}
