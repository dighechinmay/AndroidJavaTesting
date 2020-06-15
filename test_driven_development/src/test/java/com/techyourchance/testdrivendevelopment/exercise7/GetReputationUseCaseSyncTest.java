package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetReputationUseCaseSyncTest   {

    //region constants-------------------------------
    //endregion constants-----------------------------


    //region helper fields--------------------------
    //endregion helper fields-----------------------

    GetReputationUseCaseSync SUT;

    @Mock GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;

    @Before
    public void setUp() throws Exception {
        SUT = new GetReputationUseCaseSync(mGetReputationHttpEndpointSyncMock);
        success();
    }



    @Test
    public void getReputationSync_success_successReturned() {
        //Arrange
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        //Assert
        assertThat(result, is(GetReputationUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void getReputationSync_generalError_failureReturned() {
        //Arrange
        generalError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        assertThat(result,is(GetReputationUseCaseSync.UseCaseResult.FAILURE));
        //Assert
    }

    @Test
    public void getReputationSync_networkError_networkErrorReturned() {
        //Arrange
        networkError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        //Assert
        assertThat(result,is(GetReputationUseCaseSync.UseCaseResult.FAILURE));
    }




    //region helper methods----------------------------

    public void success() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(EndpointResult.SUCCESS);
    }


    public void generalError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(EndpointResult.GENERAL_ERROR);
    }

    public void networkError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(EndpointResult.NETWORK_ERROR);
    }

    //endregion helper methods-------------------------


    //region helper classess---------------------------
    //endregion helper classes-------------------------
}