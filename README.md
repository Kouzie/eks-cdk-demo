# Welcome to your CDK Java project!

This is a blank project for CDK development with Java.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation
 * `cdk destroy`     remove Cloudformation tmplate         


Enjoy!

## install eks

> 참고: <https://catalog.us-east-1.prod.workshops.aws/workshops/c15012ac-d05d-46b1-8a4a-205e7c9d93c9/ko-KR/40-deploy-clusters/200-cluster/210-cluster>

```shell
cdk bootstrap
cdk synth
cdk deploy EckDemoVpc
cdk deploy EckDemoCluster
```

### cluster 생성 확인 및 .kube/config 업데이트

```shell
aws eks list-clusters

export ACCOUNT_ID=551...

aws eks --region ap-northeast-2 update-kubeconfig \
    --name eks-work-cluster \
    --role-arn arn:aws:iam::$ACCOUNT_ID\:role/eks-work-kubectl-role
# ~/.kube/config 에 context 확인
kubectl config get-contexts
```

### cluster 동작 테스트

```shell
kubectl get nodes

kubectl apply -f k8s/02_nginx_k8s.yaml

kubectl get pods                      
NAME        READY   STATUS    RESTARTS   AGE
nginx-pod   1/1     Running   0          64s

kubectl port-forward nginx-pod 8080:80
```

### aws console 사용자 추가

CDK 에서 생성한 eks-work-kubectl-role 을 기반으로 Cluster 를 구축했기 때문에  
AWS Console 로그인 사용자는 Cluster 접근 권한이 없음  

아래 명령어로 Cluster 에 접근 가능한 RBAC 권한 추가 

```shell
kubectl edit -n kube-system configmap/aws-auth
```

```yaml
apiVersion: v1
data:
    mapAccounts: '[]'
    mapUsers: '[{"userarn":"arn:aws:iam::...:user/kouzie","username":"kouzie","groups":["system:masters"]}]'
    ...
kind: ConfigMap
...
```

### RDS 추가  

```shell
cdk deploy EckDemoDatabase
```


### 모니터링

> <https://docs.aws.amazon.com/ko_kr/AmazonCloudWatch/latest/monitoring/Container-Insights-setup-metrics.html>


