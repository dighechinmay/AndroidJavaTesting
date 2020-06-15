package com.techyourchance.testdrivendevelopment.exercise6;

import  com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImplementation implements FetchUserUseCaseSync {



    public enum Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UsersCache mUserCache;
    private final FetchUserHttpEndpointSync mFetchUserHttpEndPointSync;
    UseCaseResult mUseCaseResult;

    public FetchUserUseCaseSyncImplementation(FetchUserHttpEndpointSync mFetchUserHttpEndPointSync, UsersCache mUsercache) {
        this.mFetchUserHttpEndPointSync = mFetchUserHttpEndPointSync;
        this.mUserCache = mUsercache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId)  {
        FetchUserHttpEndpointSync.EndpointResult endpointResult = null;

        try {
            endpointResult = mFetchUserHttpEndPointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            e.printStackTrace();
            mUseCaseResult = new UseCaseResult(FetchUserUseCaseSync.Status.NETWORK_ERROR,null);
            return mUseCaseResult;
        }

        if(isSuccessFullEndPointResult(endpointResult)) {
            User userDetails = new User(endpointResult.getUserId(), endpointResult.getUsername());
            mUseCaseResult = new UseCaseResult(FetchUserUseCaseSync.Status.SUCCESS, userDetails);
            if(mUserCache.hashCode() == userDetails.hashCode() || mUserCache.equals(userDetails)){
                return mUseCaseResult;
            }
            else{
                mUserCache.cacheUser(userDetails);
                mUserCache.getUser(userId);
            }
        }
        else {
            mUseCaseResult = new UseCaseResult(FetchUserUseCaseSync.Status.FAILURE, null);
        }

        return mUseCaseResult;
    }

    private boolean isSuccessFullEndPointResult(FetchUserHttpEndpointSync.EndpointResult endpointResult) {
            return endpointResult.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS.SUCCESS;
    }
}
