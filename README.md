#### Practice with ECS
Using this article:
https://mydeveloperplanet.com/2021/09/07/how-to-deploy-a-spring-boot-app-on-aws-ecs-cluster/


#### Build project + Docker image
```bash
$ mvn clean verify
```
Verify the image has been created properly:
```bash
$ docker images
REPOSITORY                                          TAG                 IMAGE ID            CREATED             SIZE
mydeveloperplanet/myawsplanet                       0.0.1-SNAPSHOT      765984f7cfc2        24 seconds ago      666MB
```

#### Upload image to ECR
Now that you have created the Docker image, you need to upload it to ECR, the AWS Docker repository. 
Navigate in AWS to the ECS Service and select in the left menu the Repositories section. 
First thing to do, is to create a repository by clicking the Create repository button.

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecr-create-repo.png)

In order to see how you can push the Docker image to the repository, click the View push commands button which is available in the repository overview.

![](https://mydeveloperplanet.files.wordpress.com/2021/09/aws-ecr-push-commands.png)

Execute step 1, which will provide you temporary credentials in order to be able to gain access to the repository. The <account ID> will be replaced with your AWS account ID.
```bash
$ aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin <account ID>.dkr.ecr.eu-west-3.amazonaws.com
```
Skip step2, building the Docker image is already executed by means of the Maven build. In step 3, adjust the command in order to use version 0.0.1-SNAPSHOT instead of latest for identifying the local Docker image.

```bash
$ docker tag mydeveloperplanet/myawsplanet:0.0.1-SNAPSHOT <account ID>.dkr.ecr.eu-west-3.amazonaws.com/mydeveloperplanet/myawsplanet:latest
```
In step 4, you push the local Docker image to the remote AWS Docker repository.

```bash
$ docker push <account ID>.dkr.ecr.eu-west-3.amazonaws.com/mydeveloperplanet/myawsplanet:latest
```

After successful upload, the Docker image is added to the repository.
![Screenshot 2022-11-14 at 19 02 24](https://user-images.githubusercontent.com/27693622/201743948-b8eb784d-7533-4341-8240-3e097931eccb.png)

#### Create Task Definition
Now that the Docker image is available in ECR, next thing to do is to create a Task Definition in ECS by creating the 
Create new Task Definition button in the Task Definitions section (left menu).

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-task-definiteion-home.png)

In step 1, choose for an EC2 self managed task and click the Next step button.
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-taskdef-step1-ec2.png)

In step 2, give the task definiton the name myawsplanet.
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-taskdef-step2-name.png)

Select a Task execution role in the Task execution IAM role section. 
This role is necessary for pulling the Docker image. 
If the role does not exist yet, select Create new role in the dropdown list. 
The Task memory and Task CPU fields are optional, leave them empty for now. 
In the screenshot below, the container is already added. 
See below the screenshot how to accomplish this.

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-taskdef-step2-role-container.png)

Click the Add container button. Give the container the name myawsplanet. Fill in the Image to be pulled. This should be the Image URI of the Docker image from the ECR repository followed by the tag. Set the Memory Limits to 300 and add a Port mappings entry for host port 8080 to the container port 8080 (the Spring Boot application runs on port 8080 inside the container). Finally, click the Add button in order to add the container to the Task Definition.

![](https://mydeveloperplanet.files.wordpress.com/2021/09/aws-ecs-taskdef-create-container.png)

The only thing left to do is to finalize step 2 by clicking the Create button at the bottom of the page.

#### Create Cluster
Navigate in the left menu to the Clusters section and click the Create cluster button.

In step 1, choose EC2 Linux + Networking and click the Next step button.

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-create-cluster-step1.png)

In step 2, give the Cluster the name myawsplanet and choose the t2.micro as EC2 instance type. This will allow you to remain in the Free Tier. Leave the other options as default. This will launch 1 EC2 instance into your Cluster.

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-create-cluster-step2-part1.png)

In the Networking section, choose the default VPC and select all available subnets. Choose Create a new security group as Security group. You can also choose a Key pair if you want to be able to SSH to the EC2 instances. In the screenshot below, SSH access is not enabled.

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-create-cluster-step2-part2.png)

The new security group must allow all traffic for port 8080 otherwise the Rest endpoint will not be accessible. This is configured in the Security group inbound rules section. Next, choose a previously created Container instance IAM role or choose Create new role when this is the first time you are going to use this. Click the Create button.
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-create-cluster-step2-part3.png)

After a few minutes, the ECS Cluster is running. Navigate to the EC2 service and notice that the Auto Scaling group has been created and one EC2 instance is running.
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-created-cluster-asg.png)

The EC2 instance:
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-created-cluster-ec2.png)

### Create Service
You have created a Cluster and a Task Definition. Now it is time to deploy the Task Definition containing the configuration for the Docker container into the Cluster. Navigate to the Cluster and click the Deploy button.
![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-created-cluster-asg.png)

![](https://mydeveloperplanet.files.wordpress.com/2021/07/aws-ecs-created-cluster-ec2.png)

### Create Service
You have created a Cluster and a Task Definition. 
Now it is time to deploy the Task Definition containing the configuration for the Docker container into the Cluster. 
Navigate to the Cluster and click the Deploy button.

Navigate to the EC2 service and copy the public URL of the EC2 instance. Use this URL in order to verify whether the endpoint is accessible.

```bash
$ curl http://ec2-13-36-172-189.eu-west-3.compute.amazonaws.com:8080/hello
Hello AWS! From host: a035a951c3c8/172.17.0.2
```









