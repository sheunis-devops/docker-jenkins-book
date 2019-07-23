# Continuous Delivery Pipeline

Code for the chapter is here: https://github.com/sheunis-scratchpad/docker-jenkins-chapt7

For this chapter, you should run 3 EC2 instances with 4GB+ of RAM each. For the first instance, which will be the Jenkins instance, start it with the following user data:

    #!/bin/bash
    apt-get update
    apt-get install apt-transport-https ca-certificates curl software-properties-common -y
    add-apt-repository ppa:ansible/ansible
    wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | apt-key add -
    sh -c 'echo deb https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    apt-get update
    apt-get install default-jre -y
    apt-get install jenkins -y
    apt-get install docker-ce -y
    apt-get install ansible vim -y

Also make sure that this instances is added to a security group which has ports **22** and **8080** open to any IP.

For the other two instances, which will be Staging and Prod, start them with the following user data:

    #!/bin/bash
    apt-get update
    apt-get install python3 -y
    adduser --quiet --disabled-password --shell /bin/bash --home /home/jenkins --gecos "User" jenkins
    usermod -aG sudo jenkins
    echo "jenkins:password" | chpasswd
    sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config
    service sshd restart

It is not very secure to do this, but for the sake of getting a small demo running, it is fine. Add these instances to a security group that has port **8080** open to any IP, but port **22** only open to the security group that the Jenkins instance belongs to. We don't need to SSH to these instances from the outside world.

Next, you need to SSH to the Jenkins instance and run the following commands:

    sudo usermod -aG docker jenkins
    sudo setfacl -m user:jenkins:rw /var/run/docker.sock

This makes sure that Jenkins can access the Docker daemon correctly to build images. I tried to automate this using user data, but it takes quite a while for the Docker and Jenkins groups and users to be created and I think there was a timing issue. Can probably build an AMI with all of this baked in if needed.

You now need to generate an SSH key for the Jenkins user and you also need to copy the public key to the Staging and Prod instances to be able to run Ansible. Do this as follows:

    sudo passwd jenkins
    <set password>
    su - jenkins
    <enter password>
    ssh-keygen -t rsa
    ssh-copy-id <staging private ip>
    ssh-copy-id <prod private ip>

You should now be able to SSH to these two instances as the Jenkins user.

You also need to update four files with IPs before committing back to Github, namely **acceptance_test.sh** and **smoke_test.sh** (these need the public IPs of Staging and Prod respectively) and **inventory/staging** and **inventory/production** (these need the private IPs of Staging and prod respectively).

Next, you need to get the initial password from Jenkins as follows:

    sudo cat /var/lib/jenkins/secrets/initialAdminPassword

Lastly, you need to install the **HTML Publisher** plugin and you also need to set the correct credentials for DockerHub with the id shown in the Jenkinsfile.

If you now create a job with the chapt7 repo and run it, everything should pass. You should be able to access Prod or Staging as follows:

    http://<public ip>/sum?a=1&b=2

One last thing, it is not ideal to explicitly set the port to **8080** in the docker-compose file, since that will cause problems when using docker-compose to scale. The problem is that I don't know how to figure out which port it will be bound to on the Prod or Staging instances when not doing this.

This is quite cool, since it shows Github+Jenkins+Docker+Docker Compose+Ansible.
