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

Now that you have created the Docker image, you need to upload it to ECR, the AWS Docker repository. 
Navigate in AWS to the ECS Service and select in the left menu the Repositories section. 
First thing to do, is to create a repository by clicking the Create repository button.