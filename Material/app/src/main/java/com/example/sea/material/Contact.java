package com.example.sea.material;


import java.util.ArrayList;

/**
 * Created by Sea on 2017-09-23.
 */
/*개별 아이템들에 관한 정보 리턴*/
public class Contact {
    private String mName;
    private boolean mOnline;
    public Contact (String name){
        this.mName=name;
    }

    public String getName() {
        return mName;
    }

    private static int lastContactID=0;

    public static void setLastContactID(int lastContactID) {
        Contact.lastContactID = lastContactID;
    }

    public static int getLastContactID() {
        return lastContactID;
    }

    /*샘플데이터 생성*/
    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        for (int i =0; i<numContacts; i++){
            contacts.add(new Contact("Person"+ ++lastContactID +" "));
        }
        lastContactID--;
        return contacts;
    }
}
