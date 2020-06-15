package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    private static final String USERID = "userid";
    private static final String USERNAME = "username";

    UpdateUsernameUseCaseSync SUT;

    @Mock UpdateUsernameHttpEndpointSync mUpdateUsernameUseCaseSyncMock;
    @Mock UsersCache mUsersCacheMock;
    @Mock EventBusPoster  mEventBusPosterMock;



    @Before
    public void setUp() throws Exception {
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameUseCaseSyncMock,mUsersCacheMock,mEventBusPosterMock);
        success();
    }



    //userId & username are synced to the endpoint
    @Test
    public void updateUsernameSync_success_userNameUserIdPassedToTheEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUpdateUsernameUseCaseSyncMock,times(1)).updateUsername(ac.capture(),ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0),is(USERID));
        assertThat(captures.get(1),is(USERNAME));
    }

    //userId & username are  synced to the endpoint hence user cache returns userID obj
    @Test
    public void updateUserNameSync_success_userCachedReturnsUserId() throws Exception{
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mUsersCacheMock).getUser(ac.capture());
        assertThat(ac.getValue(),is(USERID));
    }


    // userId & username are  synced to the endpoint hence eventposted userdetailschangedevent
    @Test
    public void updateUserNameSync_success_userDetailsChangedEventUpdated() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USERID,USERNAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(),is(instanceOf(UserDetailsChangedEvent.class)));
    }


    //general error happens and User is not cached!
    @Test
    public void updateUserNameSync_generalError_userNotCached() throws Exception{
        generalError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    //auth error happens and User is not cached
    @Test
    public void updateUserNameSync_authError_userNotCached() throws Exception{
        authError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    //server error happnes and user is not cached
    @Test
    public void updateUserNameSync_serverError_userNotCached() throws Exception{
        serverError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    // general error and no Event bus posted
    @Test
    public void updateUserNameSync_generalError_eventBusNotUpdated() throws Exception{
        generalError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    // auth error and no Event bus posted
    @Test
    public void updateUserNameSync_authError_eventBusNotUpdated() throws Exception{
        authError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    // server error and no Event bus posted
    @Test
    public void updateUserNameSync_serverError_eventBusNotUpdated() throws Exception{
        serverError();
        SUT.updateUsernameSync(USERID,USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    //general error happens and returns failure
    @Test
    public void updateUserNameSync_generalError_failureEndPointResult() throws Exception{
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    //auth error and returns failure
    @Test
    public void updateUserNameSync_authError_failureEndPointResult() throws Exception{
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    //server error happens and failure returned
    @Test
    public void updateUserNameSync_serverError_failureEndPointResult() throws Exception{
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    //success of sync and SUccess returned!
    @Test
    public void updateUserNameSync_success_successEndPointResult() throws Exception{
        success();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    //network error happesn and returnes network exception
    @Test
    public void updateUserNameSync_networkError_networkExceptionReturned() throws Exception{
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USERID,USERNAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }



    private void networkError() throws Exception{
        doThrow(new NetworkErrorException()).when(mUpdateUsernameUseCaseSyncMock).updateUsername(any(String.class),any(String.class));
    }


    private void serverError() throws Exception{
        when(mUpdateUsernameUseCaseSyncMock.updateUsername(any(String.class),any(String.class))).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,"",""));
    }

    private void authError() throws Exception{
        when(mUpdateUsernameUseCaseSyncMock.updateUsername(any(String.class),any(String.class))).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,"",""));
    }

    private void generalError() throws Exception{
        when(mUpdateUsernameUseCaseSyncMock.updateUsername(any(String.class),any(String.class))).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,"",""));
    }

    private void success() throws Exception {
        when(mUpdateUsernameUseCaseSyncMock.updateUsername(any(String.class),any(String.class))).thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,USERID,USERNAME));
    }
}