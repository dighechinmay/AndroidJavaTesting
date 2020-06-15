package com.techyourchance.testdrivendevelopment.exercise6;


import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.SUCCESS;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTest {

    //region constants-------------------------------
    private static final String USER_ID = "userid";
    private static final String USERNAME = "username";
    private static final User USER = new User(USER_ID,USERNAME);
    //endregion constants-----------------------------


    //region helper fields--------------------------
    @Mock FetchUserHttpEndpointSync mFetchUserHttpEndPointSync;
    @Mock UsersCache mUsercache;
    //endregion helper fields-----------------------

    FetchUserUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchUserUseCaseSyncImplementation(mFetchUserHttpEndPointSync,mUsercache);
    }

    //userid passed to endpoint successfully
    @Test
    public void fetchUserSync_success_userIdPassedToEndPoint() throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        verify(mFetchUserHttpEndPointSync).fetchUserSync(ac.capture());
        //Assert
        assertThat(ac.getValue(),is(USER_ID));
    }


    //user id passed to endpoint and user is found
    @Test
    public void fetchUserSync_success_usernameFound() throws NetworkErrorException {
        success();
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        User result = verify(mUsercache).getUser(ac.capture());
        //Assert
        assertThat(result.getUsername(),is(USERNAME));
    }



    //user id passed to endpoint and user is not found
    @Test
    public void fetchUserSync_success_usernameNotFound() throws NetworkErrorException {
        success();
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        User result = verify(mUsercache).getUser(ac.capture());
        //Assert
        assertThat(result,is(nullValue()));
    }


    // success and success returned
    @Test
    public void fetchUserSync_success_successReturned() {
        //Arrange
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(result.getStatus(),is(FetchUserUseCaseSync.Status.SUCCESS));
    }


    //there was failure and user not returned (null returned)
    @Test
    public void fetchUserSync_failure_usernameNotFound() throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        User result = verify(mUsercache).getUser(ac.capture());
        //Assert
        assertThat(result,is(nullValue()));
    }


    //general error and user is null
    @Test
    public void fetchUserSync_generalError_usernameNotFound() throws NetworkErrorException {
        //Arrange
        generalError();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        User result = verify(mUsercache).getUser(ac.capture());
        //Assert
        assertThat(result,is(nullValue()));
    }

    //general error and user is null
    @Test
    public void fetchUserSync_authError_usernameNotFound() throws NetworkErrorException {
        //Arrange
        authError();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        User result = verify(mUsercache).getUser(ac.capture());
        //Assert
        assertThat(result,is(nullValue()));
    }


    // general error & failure returned

    @Test
    public void fetchUserSync_generalError_failureReturned() throws NetworkErrorException {
        //Arrange
        generalError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(result.getStatus(),is(FetchUserUseCaseSync.Status.FAILURE));
    }
    
    // auth error & failure returned

    @Test
    public void fetchUserSync_authError_failureReturned() throws NetworkErrorException {
        //Arrange
        authError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(result.getStatus(),is(FetchUserUseCaseSync.Status.FAILURE));
    }
    
    
    // userid is passed and user is cached

    @Test
    public void fetchUserSync_success_userIsCached() throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        verify(mFetchUserHttpEndPointSync).fetchUserSync(USER_ID);
        //Assert
        assertThat(ac.getValue(),is(USER));
    }


    // general error and user is not cached
    @Test
    public void fetchUserSync_generalError_userIsNotCached() throws NetworkErrorException {
        //Arrange
        generalError();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        verify(mFetchUserHttpEndPointSync).fetchUserSync(USER_ID);
        //Assert
        assertThat(ac.getValue(),is(nullValue()));
    }


    //auth error and user is not cached
    @Test
    public void fetchUserSync_auth_userIsNotCached() throws NetworkErrorException {
        //Arrange
        authError();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.fetchUserSync(USER_ID);
        //Act
        verify(mFetchUserHttpEndPointSync).fetchUserSync(USER_ID);
        //Assert
        assertThat(ac.getValue(),is(nullValue()));
    }


    //network error and returns NeTwork error
    @Test
    public void fetchUserSync_networkError_NetworkErrorReturned() throws NetworkErrorException {
        //Arrange
        networkError();
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(result.getStatus(),is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
    }




    //region helper methods----------------------------

    private void generalError() throws NetworkErrorException {
        when(mFetchUserHttpEndPointSync.fetchUserSync(any(String.class))).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(GENERAL_ERROR,"",""));
    }

    private void authError() throws NetworkErrorException {
        when(mFetchUserHttpEndPointSync.fetchUserSync(any(String.class))).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(AUTH_ERROR,"",""));
    }

    private void networkError() throws NetworkErrorException {
        when(mFetchUserHttpEndPointSync.fetchUserSync(any(String.class))).thenThrow(new NetworkErrorException());
    }

    private void success() throws NetworkErrorException {
        when(mFetchUserHttpEndPointSync.fetchUserSync(any(String.class))).thenReturn(new FetchUserHttpEndpointSync.EndpointResult(SUCCESS,USER_ID,USERNAME));
    }

    

    //endregion helper methods-------------------------


    //region helper classess---------------------------
    //endregion helper classes-------------------------
}