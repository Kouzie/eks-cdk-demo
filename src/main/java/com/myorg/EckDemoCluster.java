package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.eks.*;
import software.amazon.awscdk.services.iam.*;
import software.constructs.Construct;

import java.util.Collections;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class EckDemoCluster extends Stack {

    public EckDemoCluster(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);
        // master 가 사용할 iam 지정
        // https://docs.aws.amazon.com/eks/latest/userguide/service_IAM_role.html#create-service-role
        IManagedPolicy policy = ManagedPolicy.fromAwsManagedPolicyName("AmazonEKSClusterPolicy");
        Role eksClusterRole = Role.Builder.create(this, "eks-work-control-plane-role")
                .roleName("eks-work-control-plane-role")
                .assumedBy(ServicePrincipal.Builder.create("eks.amazonaws.com").build())
                .managedPolicies(Collections.singletonList(policy))
                .build();
        Role clusterAdmin = Role.Builder.create(this, "eks-work-kubectl-role")
                .roleName("eks-work-kubectl-role")
                .assumedBy(new AccountRootPrincipal())
                .build();
        //eks cluster 생성
        Cluster eksCluster = Cluster.Builder.create(this, "eks-work-cluster")
                .vpc(vpc)
                .clusterName("eks-work-cluster")
                .version(KubernetesVersion.V1_26)
                .defaultCapacity(2)
                .defaultCapacityInstance(InstanceType.of(InstanceClass.T2, InstanceSize.SMALL))
                .defaultCapacityType(DefaultCapacityType.NODEGROUP)
                .role(eksClusterRole) // control plane role
                .mastersRole(clusterAdmin) // kubectl access role
                .build();
        eksCluster.getDefaultNodegroup().getRole().addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("CloudWatchAgentServerPolicy"));
//        eksCluster.addAutoScalingGroupCapacity("eks-work-auto-scale-group", AutoScalingGroupCapacityOptions.builder()
//                .autoScalingGroupName("eks-work-auto-scale-group")
//                .instanceType(InstanceType.of(InstanceClass.T2, InstanceSize.SMALL))
//                .minCapacity(2)
//                .maxCapacity(5)
//                .build());
//        eksCluster.addNodegroupCapacity("eks-work-nodegroup", NodegroupOptions.builder()
//                .nodegroupName("eks-work-nodegroup")
//                .minSize(2)
//                .maxSize(5)
//                .instanceTypes(Collections.singletonList(InstanceType.of(InstanceClass.T2, InstanceSize.SMALL)))
//                .build());
        // The code that defines your stack goes here

        // example resource
        // final Queue queue = Queue.Builder.create(this, "EckDemoQueue")
        //         .visibilityTimeout(Duration.seconds(300))
        //         .build();
    }
}
