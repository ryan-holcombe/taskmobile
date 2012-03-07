package com.honeydothis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.honeydothis.json.JsonTask;
import com.honeydothis.json.JsonTaskGroup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class TaskGroupActivity extends Activity
{

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
      setContentView(R.layout.taskgroup);

      getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
      TextView titleView = (TextView) findViewById(R.id.title);
      titleView.setText("Holcombe Tasks");

      findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener()
      {
         public void onClick(final View viewIn)
         {
            Log.i("refresh", "refresh adapter");
            new TaskGroupAsyncData().execute("holcombe");
         }
      });

      new TaskGroupAsyncData().execute("holcombe");
   }

   private class TaskGroupAsyncData extends AsyncTask<String, Integer, List<JsonTaskGroup>>
   {

      private final ProgressDialog dialog;
      private TextView titleText;
      private HorizontalPager taskPager;
      private List<JsonTaskGroup> taskGroups;

      public TaskGroupAsyncData()
      {
         dialog = new ProgressDialog(TaskGroupActivity.this);
      }

      @Override
      protected void onPreExecute()
      {
         dialog.setMessage("Retrieving data...");
         dialog.setIndeterminate(true);
         dialog.setCancelable(false);
         dialog.show();
      }

      @Override
      protected void onPostExecute(final List<JsonTaskGroup> result)
      {
         taskGroups = result;
         taskPager = (HorizontalPager) findViewById(R.id.horizontal_pager);

         if (!result.isEmpty())
         {
            titleText = (TextView) findViewById(R.id.taskGroupText);
            titleText.setText(taskGroups.get(0).getName());

            taskPager = (HorizontalPager) findViewById(R.id.horizontal_pager);
            taskPager.setOnScreenSwitchListener(onScreenSwitchListener);
            for (JsonTaskGroup taskGroup : taskGroups)
            {

               List<JsonTask> tasks = taskGroup.getTasks();
               if (!tasks.isEmpty())
               {
                  ListView listView = new ListView(TaskGroupActivity.this);
                  listView.setTextFilterEnabled(true);

                  TaskAdapter adapter = new TaskAdapter(TaskGroupActivity.this, R.layout.tasklist);
                  for (JsonTask task : taskGroup.getTasks())
                  {
                     adapter.add(task);
                  }
                  listView.setAdapter(adapter);
                  listView.setOnItemClickListener(adapter);
                  taskPager.addView(listView);
               }
               else
               {
                  TextView noResultsView = new TextView(TaskGroupActivity.this);
                  noResultsView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
                  noResultsView.setText("No tasks found");
                  noResultsView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                  taskPager.addView(noResultsView);
               }

            }
         }

         dialog.dismiss();
      }

      @Override
      protected List<JsonTaskGroup> doInBackground(final String... stringsIn)
      {
         try
         {
            URL url = new URL("http://senbotsu.com/web/tasks/" + stringsIn[0] + "/");
//            URL url = new URL("http://192.168.1.101:8080/web/tasks/" + stringsIn[0] + "/");
            Log.i("restQuery", "opening connection");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(2000);
            con.setConnectTimeout(2000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

            Log.i("restQuery", "parsing response");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String json = reader.readLine();
            reader.close();

            con.disconnect();

            Log.i("restQuery", json);

            JsonTaskGroup[] taskGroups = new Gson().fromJson(json, JsonTaskGroup[].class);
            Log.i("jsonResultSize", String.valueOf(taskGroups.length));

            return Arrays.asList(taskGroups);
         }
         catch (IOException e)
         {
            Log.e("restQuery", e.getMessage(), e);
         }
         return Collections.emptyList();
      }

      private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener = new HorizontalPager.OnScreenSwitchListener()
      {
         public void onScreenSwitched(final int screen)
         {
            titleText.setText(taskGroups.get(screen).getName());
            Log.d("HorizontalPager", "switched to screen: " + screen);
         }
      };
   }
}