# Basic Docker Swarm Example

You can run the Tomcat demo at the start of the chapter with the following setup. First launch a worker node with the following user data/startup script:

    #!/bin/bash
    apt update
    apt install apt-transport-https ca-certificates curl software-properties-common -y
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
    apt update
    apt install docker-ce -y

Add it to a security group with port **8080** exposed to any IP. Now launch the manager node with the same user data as above. For the manager node, expose port **8080** to any IP and expose port **2377** to the security group that the worker node belongs to. The worker node needs access to this port to be able to join the swarm.

All of the commands should now work correctly. Also, for this small demo, **t2.micro** instances seem to work fine.
