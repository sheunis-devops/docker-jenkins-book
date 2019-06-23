# Basic Docker Stack Example

Launch instances with the same user data as in the previous example: https://github.com/sheunis-devops/docker-jenkins-book/tree/master/chapt8/p1

Also, the following `docker-compose.yml` will work for the example:

    version: "3"
    services:
        calculator:
            deploy:
                replicas: 3
            image: sheunis/calculator:latest
            ports:
            - "8080:8080"
        redis:
            deploy:
                replicas: 1
            image: redis:latest

Regarding ports, for the security group that the manager node belongs to, we need to open port **8080** to any IP. We also need to open ports **2377** and **6379** to the security group that the worker nodes belong to. Port **2377** is needed so that the workers can connect to the master and port **6379** is needed for access to the Redis replicas. For the security group that the workers belong to, we need to open port **8080** to any IP and port **6379** to the security group that the manager belongs to.

Regarding instances, **t2.micro** seems to be fine, but we need to launch enough workers to handle the 4 replicas. One manager and two workers should be enough.
