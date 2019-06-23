# Basic Jenkins Swarm Example

## Jenkins Setup

First, launch a **t2.micro** instance for the Jenkins instance and follow the instructions here: https://github.com/sheunis-devops/docker-jenkins-book/tree/master/chapt3/p1 to set the master up.

## Swarm Setup

For the user data, the same data from here: https://github.com/sheunis-devops/docker-jenkins-book/tree/master/chapt8/p1 can again be used to launch instances.

For security groups, the manager node needs to be added to a security group with port **50000** opened to any IP and port **2377** opened to the security group that the worker nodes belong to. For the worker nodes, the security group needs to have port **50000** opened to any IP.

You can now connect the worker nodes to the manager as shown in the previous examples. For this example, two workers were connected to one manager. Slightly larger instances with 2GB+ of were used. The following command will now start a Jenkins Swarm Slave service and connect it to the Jenkins instance:

`docker service create --replicas 4 --name jenkins-swarm-slave csanchez/jenkins-swarm-slave -master http://<master-public-ip>:8080 -disableSslVerification -username admin -password <admin-password> -name jenkins-swarm-slave`

You should now see the stack start up. You might see some intermittent failures, but they should resolve. If everything works correctly, you see four swarm workers connect to Jenkins. These workers are now able to run Jenkins jobs. I had some problems where you don't always see four workers connect, but in theory they should all connect successfully.
