package hyuk.com.maskalert_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hyuk.com.maskalert_app.adapter.NoticeAdpater;
import hyuk.com.maskalert_app.object.Notice;

public class NoticeActivity extends Activity {

    private Button close;
    private ListView listView;
    private List<Notice> noticeList;
    private NoticeAdpater adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        close = (Button)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.listview);
        noticeList = new ArrayList<Notice>();

        noticeList.add(new Notice(getText(R.string.static_notice).toString(), ""));
        noticeList.add(new Notice(getText(R.string.gps_user_guide).toString(), ""));
        noticeList.add(new Notice(getText(R.string.gps_err).toString(), ""));

        adpater = new NoticeAdpater(noticeList, this);
        listView.setAdapter(adpater);
    }
}
