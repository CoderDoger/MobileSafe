package hxm.com.mobilesafe;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseContactActivity extends Activity {
    private ListView listv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contact);
        listv = findViewById(R.id.list_contact);
        List<Map<String,String>> data = getContacts();

       /* listv.setAdapter(new SimpleAdapter(this,data,
                R.layout.contact_item,
                new String[]{"name","num"},
                new int[]{R.id.name,R.id.num}));
        */
        listv.setAdapter(new MyAdapter(this,R.layout.contact_item,data));

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(R.id.num);
                String phoneNum = tv.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("phoneNum",phoneNum);
                setResult(200,intent);
                finish();
            }
        });
    }

    //读取联系人
    private List<Map<String,String>>  getContact(){
        List<Map<String,String>> listData = new ArrayList<Map<String, String>>();
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = resolver.query(uri,null,null,null,null);

        while (cursor.moveToNext()){
            //获取联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (!name.isEmpty()&&!num.isEmpty()){
                Map<String,String> map = new HashMap<String, String>();
                map.put("name",name);
                map.put("num",num);
                listData.add(map);
            }
        }
        cursor.close();
        return  listData;
    }
    private List<Map<String,String>> getContacts(){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        ContentResolver resolver = getContentResolver();

        // raw_contacts uri
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");

        Cursor cursorId = resolver.query(uri,new String[]{"contact_id"},null,null,null);
        while (cursorId.moveToNext()){
           String contact_id =  cursorId.getString(0);
           if(contact_id != null){
               Map<String,String> map = new HashMap<String, String>();
               Cursor cursorData = resolver.query(uriData,new String[]{"data1","mimetype"},"contact_id=?",new String[]{contact_id},null);

               while (cursorData.moveToNext()){
                  String data1  =  cursorData.getString(0);
                  String type = cursorData.getString(1);
                  /*
                  int index = 0;
                  int c = cursorData.getColumnCount();
                  while (index<c){
                      Log.i("hxm","name=="+cursorData.getColumnName(index)+"mdata=="+cursorData.getString(index));
                      index++;
                  }
                  */

                  if ("vnd.android.cursor.item/name".equals(type)){
                    map.put("name",data1);
                  }else if("vnd.android.cursor.item/phone_v2".equals(type)){
                      if (data1 != null)
                          map.put("num",data1);
                  }else if("vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile".equals(type)){
                      if (data1 != null)
                          map.put("num",data1);
                  }
               }
               list.add(map);
               cursorData.close();
           }
        }
        cursorId.close();
        return  list;
    }

    class MyAdapter extends ArrayAdapter{
        int res ;

        public MyAdapter(Context context, int resource, List<Map<String,String>> objects) {
            super(context, resource, objects);
            this.res = resource;
        }

        @Override
        public View getView(int position, View convertView,ViewGroup parent) {
            Map<String,String> map = (Map<String, String>) getItem(position);
            View view;
            if (convertView == null){
                view = LayoutInflater.from(getApplicationContext()).inflate(res,parent,false);
            }else {
                view = convertView;
            }
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView num = (TextView)view.findViewById(R.id.num);
            name.setText(map.get("name"));
            num.setText(map.get("num"));
            return  view;
        }
    }
}
