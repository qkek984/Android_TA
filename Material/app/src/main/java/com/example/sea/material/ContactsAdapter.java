package com.example.sea.material;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Sea on 2017-09-23.
 */

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    /*뷰를 담는 상자*/
    class ViewHolder0 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nameTaxtView;
        public Button messageButton;
        private ContactsAdapter mContacts;
        private Context context;

        public void setmContacts(ContactsAdapter mContacts) {
            this.mContacts = mContacts;
        }

        public void setContext(Context context) {
            this.context = context;
        }
        public ViewHolder0(Context context, View itemView,ContactsAdapter contacts) {
            super(itemView);
            nameTaxtView= (TextView)itemView.findViewById(R.id.contact_name);
            messageButton= (Button)itemView.findViewById(R.id.message_button);
            this.context = context;
            this.mContacts=contacts;
            messageButton.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            mContacts.removeItem(position);//아이템 삭제
            //Toast.makeText(context,nameTaxtView.getText() + Integer.toString(position),Toast.LENGTH_SHORT).show();

        }
    }
    class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView nameTaxtView;
        public Button messageButton;
        private ContactsAdapter mContacts;
        private Context context;

        public void setmContacts(ContactsAdapter mContacts) {
            this.mContacts = mContacts;
        }

        public void setContext(Context context) {
            this.context = context;
        }
        public ViewHolder2(Context context, View itemView,ContactsAdapter contacts) {
            super(itemView);
            nameTaxtView= (TextView)itemView.findViewById(R.id.contact_name);
            this.context = context;
            this.mContacts=contacts;
        }
    }

    /*어댑터*/

    private List<Contact> mContacts;
    private  Context mContext;

    public ContactsAdapter(Context context, List<Contact> contacts) {//생성자
        mContext= context;
        mContacts = contacts;
    }

    public void removeItem(int p){//삭제함수 구성
        mContacts.remove(p);
        Contact.setLastContactID(Contact.getLastContactID()-1);//인덱스 줄임
        notifyItemRemoved(p);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);//레이아웃 인플레이터 선언

        //RecyclerView viewHolder = new RecyclerView(context,contactView,this);//뷰홀더에 연결
        if (viewType==0){
            View contactView = inflater.inflate(R.layout.item_contact,parent,false);//뷰에 xml연결
            Log.i("z","1");
            return new ViewHolder0(context,contactView,this);
        }else{
            View contactView = inflater.inflate(R.layout.item_contact2,parent,false);//뷰에 xml연결
            Log.i("h","2");
            return new ViewHolder2(context,contactView,this);
        }





    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int item = getItemViewType(position);
        if (item==0){
            Contact contact = mContacts.get(position);
            TextView textview = ((ViewHolder0)viewHolder).nameTaxtView;
            textview.setText(contact.getName());
            Button button = ((ViewHolder0)viewHolder).messageButton;
            button.setText("message");
        }else{
            Contact contact = mContacts.get(position);
            TextView textview = ((ViewHolder2)viewHolder).nameTaxtView;
            textview.setText(contact.getName());
        }



    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }




}
