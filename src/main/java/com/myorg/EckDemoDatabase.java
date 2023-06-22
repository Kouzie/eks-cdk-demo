package com.myorg;

import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudwatch.MetricOptions;
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

        String databaseUsername = "mywork";
        String databasePassword = "myworkpassword";

        // aurora serverless v2
        DatabaseCluster dbCluster = DatabaseCluster.Builder.create(this, "eks-work-db-postgre")
                .vpc(vpc)
                .writer(ClusterInstance.serverlessV2("eks-work-db-postgre-serverless",
                        ServerlessV2ClusterInstanceProps.builder().build())
                )
                .engine(DatabaseClusterEngine.auroraPostgres(AuroraPostgresClusterEngineProps.builder()
                        .version(AuroraPostgresEngineVersion.VER_14_7)
                        .build()))
                .credentials(Credentials.fromPassword(databaseUsername, SecretValue.plainText(databasePassword))) // id: admin, pw: secret
                .securityGroups(Collections.singletonList(sgPostgre))
                .serverlessV2MinCapacity(2)
                .serverlessV2MaxCapacity(4)
                .defaultDatabaseName("myworkdb")
                .build();
    }
}
