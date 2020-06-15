package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class GetReputationUseCaseSync {


    GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;

    public GetReputationUseCaseSync(GetReputationHttpEndpointSync mGetReputationHttpEndpointSync) {
        this.mGetReputationHttpEndpointSync = mGetReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync()throws RuntimeException {

        GetReputationHttpEndpointSync.EndpointResult endpointResult = mGetReputationHttpEndpointSync.getReputationSync();

        switch (endpointResult) {
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return UseCaseResult.FAILURE;
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            default:
                throw new RuntimeException("Invalid result ="+ endpointResult);
        }


    }


    public enum UseCaseResult {
         FAILURE, SUCCESS
    }
}
