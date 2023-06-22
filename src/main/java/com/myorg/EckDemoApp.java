package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class EckDemoApp {
    public static void main(final String[] args) {
        App app = new App();
        String accountId = "...";
        StackProps props = StackProps.builder()
                // If you don't specify 'env', this stack will be environment-agnostic.
                // Account/Region-dependent features and context lookups will not work,
                // but a single synthesized template can be deployed anywhere.

                // Uncomment the next block to specialize this stack for the AWS Account
                // and Region that are implied by the current CLI configuration.
                .env(Environment.builder()
                        .account(accountId)
                        .region("ap-northeast-2")
                        .build())
                .build();
        EckDemoVpc eckDemoVpc = new EckDemoVpc(app, "EckDemoVpc", props);
        EckDemoCluster eckDemoCluster =  new EckDemoCluster(app, "EckDemoCluster", props, eckDemoVpc.vpc);
        EckDemoDatabase eckDemoDatabase = new EckDemoDatabase(app, "EckDemoDatabase", props, eckDemoVpc.vpc);
        EckDemoWebService eckDemoWebService = new EckDemoWebService(app, "EckDemoWebService", props, eckDemoVpc.vpc);
        app.synth();
    }
}

