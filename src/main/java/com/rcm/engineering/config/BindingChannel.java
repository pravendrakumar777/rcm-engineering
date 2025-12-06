package com.rcm.engineering.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface BindingChannel {

    String PRE_ONBOARDING_REQUEST_OUTPUT = "employee-pre-onboarding-request-output";
    String PRE_ONBOARDING_REQUEST_INPUT  = "employee-pre-onboarding-request-input";

    String APPROVAL_STATUS_OUTPUT = "employee-approval-status-output";
    String APPROVAL_STATUS_INPUT  = "employee-approval-status-input";

    @Output(PRE_ONBOARDING_REQUEST_OUTPUT)
    MessageChannel preOnboardingRequestOutput();
    @Input(PRE_ONBOARDING_REQUEST_INPUT)
    SubscribableChannel preOnboardingRequestInput();

    @Output(APPROVAL_STATUS_OUTPUT)
    MessageChannel approvalStatusOutput();
    @Input(APPROVAL_STATUS_INPUT)
    SubscribableChannel approvalStatusInput();

}
