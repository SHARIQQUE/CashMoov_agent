package com.agent.cashmoovui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agent.cashmoovui.adapter.Contact;
import com.agent.cashmoovui.adapter.ContactListAdapter;
import com.agent.cashmoovui.listeners.ContactListLisners;

import java.util.ArrayList;
import java.util.HashSet;

public class AddContact extends AppCompatActivity implements View.OnClickListener, ContactListLisners {

    public static AddContact addbeneficiaryC;
    ImageView imgBack,imgHome;
    TextView tvGo;
    RecyclerView rvContact;
    SearchView searchView;
    public String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beneficiary);
        addbeneficiaryC=this;
        setBackMenu();
        getIds();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void getIds() {
        rvContact = findViewById(R.id.rvContact);
        searchView = findViewById(R.id.searchView);
        tvGo = findViewById(R.id.tvGo);
        tvGo.setOnClickListener(addbeneficiaryC);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText.toString();
               // if (newText.length() >= 9) {
                    setSearchResult(newText);
               // }
                return false;
            }

        });

        searchView.setOnCloseListener(() -> {
            getContactList();
            tvGo.setVisibility(View.GONE);
            return false;
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        getContactList();
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvGo:
                if(searchText.isEmpty()) {
                    MyApplication.showErrorToast(addbeneficiaryC, getString(R.string.enter_phone_no));
                    return;
                }
                if(searchText.trim().length()<9) {
                    MyApplication.showErrorToast(addbeneficiaryC, getString(R.string.enter_phone_no_val));
                    return;
                }

                    intent = getIntent();
                    intent.putExtra("PHONE", searchText);
                    setResult(RESULT_OK, intent);
                    finish();

                break;

        }
    }

    ContactListAdapter contactListAdapter;

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    public static ArrayList<Contact> contactList=new ArrayList<>();
    private void getContactList() {
        ContentResolver cr = getContentResolver();

        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null) {
            HashSet<String> mobileNoSet = new HashSet<String>();
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name, number;
                contactList.clear();
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    number = number.replace(" ", "");
                    if (!mobileNoSet.contains(number)) {
                        contactList.add(new Contact(name, number));
                        mobileNoSet.add(number);
                        Log.d("hvy", "onCreaterrView  Phone Number: name = " + name
                                + " No = " + number);

                        contactListAdapter = new ContactListAdapter(addbeneficiaryC,contactList);
                        rvContact.setHasFixedSize(true);
                        rvContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                        rvContact.setAdapter(contactListAdapter);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    public void setSearchResult(String searchText) {
        tvGo.setVisibility(View.VISIBLE);
        ArrayList<Contact> searchModels = new ArrayList<>();
        for (Contact item : contactList) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getPhoneNumber().toLowerCase().contains(searchText.toLowerCase())) {
                searchModels.add(item);
            }
        }

        contactListAdapter.onDataChange(searchModels);
    }



    @Override
    public void onContactViewItemClick(String name, String number) {
        if(number.trim().length()<9) {
            MyApplication.showErrorToast(addbeneficiaryC, getString(R.string.enter_phone_no_val));
            return;
        }


            Intent intent = getIntent();
            intent.putExtra("PHONE", number);
            setResult(RESULT_OK, intent);
            finish();


    }
}