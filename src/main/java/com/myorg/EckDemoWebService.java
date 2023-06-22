package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.constructs.Construct;

public class EckDemoWebService extends Stack {
    public Vpc vpc;

    public EckDemoWebService(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);
        Repository.Builder.create(this, "calculating-repo")
                .repositoryName("calculating-repo")
                .build();
        Repository.Builder.create(this, "greeting-repo")
                .repositoryName("greeting-repo")
                .build();
        Repository.Builder.create(this, "region-repo")
                .repositoryName("region-repo")
                .build();

    }
}
