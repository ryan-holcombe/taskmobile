package com.honeydothis;

import com.honeydothis.json.JsonTask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class TaskAdapter extends ArrayAdapter<JsonTask> implements AdapterView.OnItemClickListener
{

   private LayoutInflater inflater;

   public TaskAdapter(Context context, int textViewResourceId)
   {
      super(context, textViewResourceId);
      inflater = LayoutInflater.from(context);
   }

   @Override
   public View getView(final int position, final View convertView, final ViewGroup parent)
   {
      JsonTask task = getItem(position);
      View v = convertView;

      //Inflate the view
      if (v == null)
      {
         v = inflater.inflate(R.layout.tasklist, null);
      }

      CheckedTextView textView = (CheckedTextView) v.findViewById(R.id.taskText);
      if (task != null && task.getTitle() != null)
      {
         textView.setText(task.getTitle());
      }

      return v;
   }

   public void onItemClick(final AdapterView<?> adapterViewIn, final View viewIn, final int i, final long l)
   {
      Log.i("TaskAdapter", "onItemClick " + i);
      CheckedTextView checkedTextView = (CheckedTextView) viewIn;
      checkedTextView.setChecked(!checkedTextView.isChecked());
   }
}
