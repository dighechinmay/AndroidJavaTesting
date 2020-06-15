package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactUseCase {


    private final List<Listener> mListeners = new ArrayList<>();
    private GetContactsHttpEndpoint mGetContactsHttpEndpoint;

    public FetchContactUseCase(GetContactsHttpEndpoint mGetContactsHttpEndpoint) {
        this.mGetContactsHttpEndpoint = mGetContactsHttpEndpoint;
    }

    public void fetchContactsAndNotify(String filter) {

        mGetContactsHttpEndpoint.getContacts(filter, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contacts) {

                for(Listener listener: mListeners){
                    listener.onContactsFetched(contactsFromSchemas(contacts));
                }

            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {

            }
        });
    }

    private List<Contact> contactsFromSchemas(List<ContactSchema> contactSchemas){
        List<Contact> contacts = new ArrayList<>();
        for(ContactSchema schema: contactSchemas){
            contacts.add(new Contact(schema.getId(),schema.getFullName(),schema.getFullPhoneNumber(),schema.getImageUrl(),schema.getAge()));
        }

        return contacts;
    }
    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public interface Listener {
        void onContactsFetched(List<Contact> capture);
    }
}
