package com.killi8n.contentresolver.contentresolverexample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CRActivity extends AppCompatActivity {

    SimpleCursorAdapter ca;
    ContentResolver cr;
    Cursor cursor;
    ListView listView;
    String COLUMN_ID;
    String COLUMN_COUNTRY;
    String COLUMN_CAPITAL;

    private static final Uri CONTENT_URI = Uri.parse("content://com.sample.contentproviderexample/world");

    private static final int MENU_INSERT= Menu.FIRST + 1;
    private static final int MENU_UPDATE= Menu.FIRST + 2;
    private static final int MENU_DELETE= Menu.FIRST + 3;

    private static final int RQ_INSERT = 1;
    private static final int RQ_UPDATE = 2;
    private static final int RQ_DELETE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr);

        cr = getContentResolver();
        cursor = cr.query(CONTENT_URI, null, null, null, null);

        if(cursor != null) {
            COLUMN_ID = cursor.getColumnName(0);
            COLUMN_COUNTRY = cursor.getColumnName(1);
            COLUMN_CAPITAL = cursor.getColumnName(2);

            ca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{COLUMN_COUNTRY, COLUMN_CAPITAL}, new int[]{android.R.id.text1, android.R.id.text2});
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(ca);
            registerForContextMenu(listView);
        } else {
            new AlertDialog.Builder(this)
                            .setTitle("error")
                            .setMessage("데이터를 가져올수 없습니다.\n 프로바이더를확인하세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .show();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_INSERT, Menu.NONE, "입력하기");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_INSERT:
                Intent intent = new Intent(CRActivity.this, InsertActivity.class);
                startActivityForResult(intent, RQ_INSERT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, MENU_UPDATE, Menu.NONE,"업데이트");
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE,"삭제하기");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        cursor.moveToPosition(position);

        int id = cursor.getInt(0);
        String str1 = cursor.getString(1);
        String str2 = cursor.getString(2);


        switch (item.getItemId()) {
            case MENU_UPDATE:
                Intent i = new Intent(CRActivity.this, InsertActivity.class);
                i.putExtra(COLUMN_ID, id);
                i.putExtra(COLUMN_COUNTRY, str1);
                i.putExtra(COLUMN_CAPITAL, str2);
                startActivityForResult(i, RQ_UPDATE);
                break;
            case MENU_DELETE:
                cr.delete(CONTENT_URI, COLUMN_ID + "=" + id, null);
                cursor.requery();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode) {
            ContentValues row = new ContentValues();
            row.put(COLUMN_COUNTRY, data.getStringExtra(COLUMN_COUNTRY));
            row.put(COLUMN_CAPITAL, data.getStringExtra(COLUMN_CAPITAL));
            switch(requestCode) {
                case RQ_INSERT:
                    cr.insert(CONTENT_URI, row);
                    break;
                case RQ_UPDATE:
                    cr.update(CONTENT_URI, row, COLUMN_ID + "=" + data.getIntExtra(COLUMN_ID, 0), null);
                    break;
            }
            cursor.requery();
        }
    }
}
