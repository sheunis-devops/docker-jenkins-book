# Simple AWS Dynamic Docker Agent Example

## Master Setup

Launch an Ubuntu EC2 instance with the following user data/startup script:

    #!/bin/bash
    wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | apt-key add -
    sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    apt-get update
    apt-get install default-jre -y
    apt-get install jenkins -y

Make sure to open up port **8080** to any IP.

Get the initial admin password by SSH'ing into the host and using the following:

`sudo cat /var/lib/jenkins/secrets/initialAdminPassword`

Install the **Docker** plugin.

## Slave Setup

Launch an Ubuntu EC2 instance with the following user data/startup script:

    #!/bin/bash
    mkdir -p /etc/systemd/system/docker.service.d
    echo "[Service]" >> /etc/systemd/system/docker.service.d/docker.conf
    echo "ExecStart=" >> /etc/systemd/system/docker.service.d/docker.conf
    echo "ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock" >> /etc/systemd/system/docker.service.d/docker.conf
    apt update
    apt install apt-transport-https ca-certificates curl software-properties-common -y
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
    apt update
    apt install docker-ce -y

Make sure to open up port **2375** to any IP, as well as ports **32768-upper** (not sure what upper is here) to any IP. This is because the SSH ports of the Docker containers that spin up are mapped to a high port of the host. The Jenkins master needs access to these ports for authentication.

## Plugin Config

Follow the exact plugin config shown in the book, but use the public IP of the slave instance.

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

Set the number of executors on the master to 0. Launch more than one job. You should now see Docker containers spin up to execute these jobs.

**Note:** I can probably do better wrt opening ports only to security groups, etc. Will have to experiment more to see if I can lock this setup down better. This is only for simple demo purposes though.
