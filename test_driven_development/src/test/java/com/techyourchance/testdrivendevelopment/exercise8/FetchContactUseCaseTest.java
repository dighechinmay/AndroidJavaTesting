package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactUseCaseTest {
    public static final String FILTER = "filter";
    public static final String ID = "id";
    public static final String FULLNAME = "fullname";
    public static final String IMAGEURL = "imageurl";
    public static final String PHONENUMBER = "phonenumber";
    public static final double AGE = 40;


    //region constants-------------------------------
    //endregion constants-----------------------------


    //region helper fields--------------------------
    //endregion helper fields-----------------------

    @Mock
    GetContactsHttpEndpoint mGetContactsHttpEndpoint;
    @Mock
    FetchContactUseCase.Listener mListnerMock1;
    @Mock
    FetchContactUseCase.Listener mListnerMock2;

    @Captor
    ArgumentCaptor<List<Contact>> mAcListContact;

    FetchContactUseCase SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchContactUseCase(mGetContactsHttpEndpoint);
        success();
    }

    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsSucceeded(getContactSchemes());
                return null;
            }
        }).when(mGetContactsHttpEndpoint).getContacts(any(String.class), any(Callback.class));
    }

    private List<ContactSchema> getContactSchemes() {
        List<ContactSchema> schemas = new ArrayList<>();
        schemas.add(new ContactSchema(ID, FULLNAME, PHONENUMBER, IMAGEURL, AGE));
        return schemas;
    }

    //correct filter term reached to the endpoint

    @Test
    public void fetchContacts_filterTermReachedToEndpoint() {
        //Arrange
        ArgumentCaptor<String> acInt = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.fetchContactsAndNotify(FILTER);
        verify(mGetContactsHttpEndpoint).getContacts(acInt.capture(), any(Callback.class));
        //Assert
        assertThat(acInt.getValue(), is(FILTER));
    }


    //success -  all observers notified with correct data

    @Test
    public void fetchContact_success_observersNotifedWithRightData() {
        //Arrange
        // ArgumentCaptor<List<Contact>> ac = ArgumentCaptor.forClass(List<Contact>.class);
        //Act
        SUT.registerListener(mListnerMock1);
        SUT.registerListener(mListnerMock2);
        SUT.fetchContactsAndNotify(FILTER);
        //Assert
        verify(mListnerMock1).onContactsFetched(mAcListContact.capture());
        verify(mListnerMock2).onContactsFetched(mAcListContact.capture());

        List<List<Contact>> captures = mAcListContact.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);
        assertThat(capture1, is(getContact()));
        assertThat(capture2, is(getContact()));

    }

    private List<Contact> getContact() {
        {
            List<Contact> contact = new ArrayList<>();
            contact.add(new Contact(ID, FULLNAME, PHONENUMBER, IMAGEURL, AGE));
            return contact;
        }


        //success - unsubscribed observers not notified
        //general error - observers notified of failure.
        //network error - observers notified of failure.

        //region helper methods----------------------------
        //endregion helper methods-------------------------


        //region helper classess---------------------------
        //endregion helper classes-------------------------
    }
}