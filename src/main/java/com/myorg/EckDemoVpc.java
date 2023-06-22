package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class EckDemoVpc extends Stack {
    public Vpc vpc;

    public EckDemoVpc(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        vpc = Vpc.Builder.create(this, "eks-work-VPC")
                .vpcName("eks-work-VPC")
                .maxAzs(3)  // Default is all AZs in region
                .cidr("10.0.0.0/16")
                .enableDnsSupport(true)
                .enableDnsHostnames(true)
                .build();
    }
}
