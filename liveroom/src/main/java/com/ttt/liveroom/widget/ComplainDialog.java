package com.ttt.liveroom.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ttt.liveroom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘景 on 2017/02/15.
 */

public class ComplainDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button mCancel, mCommit;
    private Context context;
    private MessageDialogListener listener;
    private ListView mlv;
    private ListAdapter adapter;
    public List<String> list = new ArrayList<String>();
    public int selectedPosition = -1;// 默认选中的位置

    public ComplainDialog(Context context, List<String> stringList) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.list = stringList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.complain_dialog);
        findView();
        initLayout();
        init();
    }
    private void init() {
        mCancel.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        adapter = new ListAdapter(context);
        mlv.setAdapter(adapter);
        mlv.setOnItemClickListener(this);
    }

    private void initLayout() {

        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();

        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(50, 0, 50, 0); //消除边距

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private void findView() {
        mlv = (ListView) findViewById(R.id.dialog_complain_lv);
        mCancel = (Button) findViewById(R.id.dialog_comlain_cancel);
        mCommit = (Button) findViewById(R.id.dialog_comlain_commit);
    }

    public void setMessageDialogListener(ComplainDialog.MessageDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            if (listener != null) {
                listener.onCancelClick(this);
            }
        }
    }

    public interface MessageDialogListener {
        void onCancelClick(ComplainDialog dialog);

        void onCommitClick(ComplainDialog dialog);
    }

    class ListAdapter extends BaseAdapter {

        private Context context;

        public ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.complain_dialog_item, null);
                holder.tv = (TextView) convertView.findViewById(R.id.complain_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String string = list.get(position);
            holder.tv.setText(string);
            if (position % 2 == 0) {
                if (selectedPosition == position) {
                    convertView.setSelected(true);
                    convertView.setPressed(true);
                    listener.onCommitClick(ComplainDialog.this);
                    holder.tv.setTextColor(context.getResources().getColor(R.color.color_text_green));
                } else {
                    convertView.setSelected(false);
                    convertView.setPressed(false);
                    holder.tv.setTextColor(context.getResources().getColor(R.color.color_text_gray));
                }
            } else {
                if (selectedPosition == position) {
                    convertView.setSelected(true);
                    convertView.setPressed(true);
                    listener.onCommitClick(ComplainDialog.this);
                    holder.tv.setTextColor(context.getResources().getColor(R.color.color_text_green));
                } else {
                    convertView.setSelected(false);
                    convertView.setPressed(false);
                    holder.tv.setTextColor(context.getResources().getColor(R.color.color_text_gray));
                }
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectedPosition(position);
        adapter.notifyDataSetInvalidated();
    }
}
