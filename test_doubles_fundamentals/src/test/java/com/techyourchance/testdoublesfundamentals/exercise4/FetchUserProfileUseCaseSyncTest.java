package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.LoginUseCaseSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USERID = "userid";
    public static final String FULLNAME = "fullname";
    public static final String IMAGEURL = "imageurl";
    public static final User mUser = new User(USERID,FULLNAME,IMAGEURL);


    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncTd;
    UserCacheTd mUserCacheTd;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        mUserCacheTd = new UserCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd,mUserCacheTd);
    }

    // userId passed to the Endpoint

    @Test
    public void fetchUser_success_userIdPassedToEndpoint() {
            SUT.fetchUserProfileSync(USERID);
            assertThat(mUserProfileHttpEndpointSyncTd.mUserID,is(USERID));
    }

    // user is stored in cache when userID passed successfully

    @Test
    public void fetchUser_success_userFoundSuccessfully() {
        SUT.fetchUserProfileSync(USERID);
        User cachedUser = mUserCacheTd.getUser(USERID);
        assertThat(cachedUser.getUserId(),is(USERID));
        assertThat(cachedUser.getFullName(),is(FULLNAME));
        assertThat(cachedUser.getImageUrl(),is(IMAGEURL));
    }

    //user not stored in cache because of general error and returns null object

    @Test
    public void fetchUser_generalError_nullObjectReturned() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUserCacheTd.getUser(USERID),is(nullValue()));
    }


    //user not stored in cache because of auth_error and returns null object

    @Test
    public void fetchUser_authError_nullObjectReturned() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUserCacheTd.getUser(USERID),is(nullValue()));
    }

    //user not stored in cache because of server_error and returns null object

    @Test
    public void fetchUser_serverError_nullObjectReturned() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(mUserCacheTd.getUser(USERID),is(nullValue()));
    }

    //user  stored in cache because of success and returns user object

    @Test
    public void fetchUser_ONsuccess_successReturned() {
        SUT.fetchUserProfileSync(USERID);
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }



    // network error then NETWORK ERROR RETURNED! (network error)
    @Test
    public void fetchUser_networkError_nullObjReturned() {
        mUserProfileHttpEndpointSyncTd.isNetworkError = true;
        SUT.fetchUserProfileSync(USERID);
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result,is(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    @Test
    public void fetchUser_generalError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result,is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUser_authError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result,is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }


    @Test
    public void fetchUser_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result,is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    // (if user successfully logins) user is cached to the UsersCache (return user object!)
    // if error then user is not cached into UserCache & null is returned!
    // " " error -general,auth arror, server error
    //


    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync{
        public String mUserID = "userid";
        public String mFullname = "fullname";
        public String mImageUrl = "imageurl";

        public boolean isGeneralError;
        public boolean isAuthError;
        public boolean isServerError;
        public boolean isSuccess;
        public boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserID = userId;
            if(isGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"","","");
            }
            else if(isAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR,"","","");
            }
            else if(isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR,"","","");
            }
            else if(isNetworkError){
                throw new NetworkErrorException();
            }
            else
            {
                return new EndpointResult(EndpointResultStatus.SUCCESS,mUserID,mFullname,mImageUrl);
            }
        }
    }

    private static class UserCacheTd implements UsersCache {

        User mUser = null;




        @Override
        public void cacheUser(User user) {
            mUser = user;
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return mUser;
        }
    }


}