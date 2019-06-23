# Simple AWS Jenkins Swarm Example

## Master Setup

Launch an Ubuntu EC2 instance with the following user data/startup script:

    #!/bin/bash
    wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | apt-key add -
    sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    apt-get update
    apt-get install default-jre -y
    apt-get install jenkins -y

Make sure to open up ports **8080** and **50000** to any IP.

Get the initial admin password by SSH'ing into the host and using the following:

`sudo cat /var/lib/jenkins/secrets/initialAdminPassword`

Install the **Self-Organizing Swarm Plug-in Modules** plugin. Under **Configure Global Security**, set the **TCP port for inbound agents** to **50000** and the protocol to **JNLP2** (bad), but it makes it easy to get a demo running.

## Slave Setup

Launch an Ubuntu EC2 instance with the following user data/startup script:

    #!/bin/bash
    apt-get update
    apt-get install default-jre -y
    wget https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/1.26/swarm-client-1.26-jar-with-dependencies.jar -P /home/ubuntu/

Make sure to open up port **50000** to any IP.

Run the following on the slave host:

`java -jar swarm-client-1.26-jar-with-dependencies.jar -master http://<master-public-ip>:8080 -username admin -password <admin-password> -name jenkins-swarm-slave-1 -disableSslVerification`

You should now see the slave successfully connect to the master.

## Testing

You can test this with the following basic pipeline:

    pipeline {
        agent any
        stages {
            stage("Hello") {
                steps {
                    sleep 20
                    echo 'Hello World'
                }
            }
        }
    }

Set the number of executors on the master to 0 and watch the job run only on the slave.

**Note:** I can probably do better wrt opening ports only to security groups, etc. Will have to experiment more to see if I can lock this setup down better. This is only for simple demo purposes though.
