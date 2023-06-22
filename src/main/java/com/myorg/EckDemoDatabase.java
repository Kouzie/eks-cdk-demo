package com.myorg;

import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.Collections;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class EckDemoDatabase extends Stack {

    public EckDemoDatabase(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);
        SecurityGroup sgPostgre = SecurityGroup.Builder.create(this, "eks-work-sg-postgre")
                .securityGroupName("eks-work-sg-postgre")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();
        sgPostgre.addIngressRule(Peer.anyIpv4(), Port.tcp(5432));

        String databaseUsername = "admin";
        String databasePassword = "password";
        ServerlessCluster databaseCluster = new ServerlessCluster(this, "-ueyes-serverless-database", ServerlessClusterProps.builder()
                .engine(DatabaseClusterEngine.auroraPostgres(AuroraPostgresClusterEngineProps.builder()
                        .version(AuroraPostgresEngineVersion.VER_15_2)
                        .build()))
                .credentials(Credentials.fromPassword(databaseUsername, SecretValue.plainText(databasePassword))) // id: admin, pw: secret
                .vpc(vpc)
                .securityGroups(Collections.singletonList(sgPostgre))
                .build());
    }
}
