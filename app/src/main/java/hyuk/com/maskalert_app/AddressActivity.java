package hyuk.com.maskalert_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hyuk.com.maskalert_app.adapter.AddressAdapter;
import hyuk.com.maskalert_app.object.Addr;

public class AddressActivity extends Activity {

    private ListView addressListview;
    private AddressAdapter addressAdpater;

    private List<Addr> addrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Intent intent = getIntent();
        addrList = (ArrayList<Addr>) intent.getSerializableExtra("addrList");

        addressListview = (ListView)findViewById(R.id.addressListview);
        addressAdpater = new AddressAdapter(addrList, this);

        addressListview.setAdapter(addressAdpater);
        addressListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent return_intent = new Intent();
                return_intent.putExtra("lat", addrList.get(i).getLat());
                return_intent.putExtra("lng", addrList.get(i).getLng());
                setResult(RESULT_OK, return_intent);
                finish();
            }
        });
    }
}
