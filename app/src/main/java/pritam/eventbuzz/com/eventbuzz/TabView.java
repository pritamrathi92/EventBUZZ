package pritam.eventbuzz.com.eventbuzz;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabView extends TabActivity {
    private static final String NEWS_FEED = "News Feed";
    private static final String Explore = "Explore";
    private static final String Me = "Me";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        TabHost tabHost =   getTabHost();

        TabSpec newsFeed = tabHost.newTabSpec(NEWS_FEED);
        newsFeed.setIndicator(NEWS_FEED);
        Intent newsFeedIntent = new Intent(this,InterestEventActivity.class);
        newsFeed.setContent(newsFeedIntent);

        TabSpec explore = tabHost.newTabSpec(Explore);
        explore.setIndicator(Explore);
        Intent exploreIntent = new Intent(this,MyListActivity.class);
        explore.setContent(exploreIntent);

        TabSpec me = tabHost.newTabSpec(Me);
        me.setIndicator(Me);
        Intent meIntent = new Intent(this,MeActivity.class);
        me.setContent(meIntent);

        tabHost.addTab(newsFeed);
        tabHost.addTab(explore);
        tabHost.addTab(me);
    }
}
