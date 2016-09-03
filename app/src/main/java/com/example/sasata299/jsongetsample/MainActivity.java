package com.example.sasata299.jsongetsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private final String uri = "http://api.atnd.org/events/?keyword=android&format=json";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            AsyncJsonLoader asyncJsonLoader = new AsyncJsonLoader(new AsyncJsonLoader.AsyncCallback() {

                public void preExecute() {
                }

                public void postExecute(JSONObject result) {
                    if (result == null) {
                        showLoadError();
                        return;
                    }
                    try {
                        // 各Atndイベントのタイトルを配列に格納
                        ArrayList<String> list = new ArrayList<>();
                        JSONArray eventArray = result.getJSONArray("events");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject eventObj = eventArray.getJSONObject(i);
                            JSONObject event = eventObj.getJSONObject("event");
                            //Log.d("title", event.getString("title"));
                            list.add(event.getString("title"));
                        }

                        // ListView用のアダプタを作成
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                getActivity(), android.R.layout.simple_list_item_1, list
                        );

                        // ListViewにアダプタをセット
                        ListView listView = (ListView)getActivity().findViewById(R.id.listView);
                        listView.setAdapter(arrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showLoadError();
                    }
                }

                public void progressUpdate(int process) {
                }

                public void cancel() {
                }
            });

            asyncJsonLoader.execute(uri);
        }

        public void showLoadError() {
            Toast toast = Toast.makeText(getActivity(), "データを取得できませんでした。", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
