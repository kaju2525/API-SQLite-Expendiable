package com.demo.option2;

import java.util.ArrayList;
import java.util.Iterator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.demo.R;
import com.demo.option2.model.Child;
import com.demo.option2.model.Group;


public class MainActivity extends Activity {
	String url = "https://raw.githubusercontent.com/karunkumar25/Api_Test/master/flag.json";
	ProgressDialog PD;

	private ExpandListAdapter ExpAdapter;
	private ExpandableListView ExpandList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ExpandList = (ExpandableListView) findViewById(R.id.simple_expandable_listview);

		PD = new ProgressDialog(this);
		PD.setMessage("Loading.....");
		PD.setCancelable(false);

		makejsonobjreq();
	}

	private void makejsonobjreq() {
		PD.show();

		AndroidNetworking.get(url).build().getAsJSONObject(new JSONObjectRequestListener() {
			@Override
			public void onResponse(JSONObject response) {

				ArrayList<Group> list = new ArrayList<Group>();
				ArrayList<Child> ch_list;

				try {
					Iterator<String> key = response.keys();
					while (key.hasNext()) {
						String k = key.next();

						Group gru = new Group();
						gru.setName(k);
						ch_list = new ArrayList<Child>();

						JSONArray ja = response.getJSONArray(k);

						for (int i = 0; i < ja.length(); i++) {

							JSONObject jo = ja.getJSONObject(i);

							Child ch = new Child();
							ch.setName(jo.getString("name"));
							ch.setImage(jo.getString("flag"));

							ch_list.add(ch);
						} // for loop end
						gru.setItems(ch_list);
						list.add(gru);
					} // while loop end

					ExpAdapter = new ExpandListAdapter(MainActivity.this, list);
					ExpandList.setAdapter(ExpAdapter);

					PD.dismiss();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(ANError anError) {

			}
		});
	}
}
