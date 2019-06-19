# Automated Acceptance Testing

Code for the chapter is here: https://github.com/sheunis-devops/docker-jenkins-chapt5

For this chapter, you can just run everything locally to simplify it. First step involves building the Jenkins image containing Docker and Docker Compose as follows:

`docker build -t jenkins-with-docker jenkinswithdocker`

You can now run the Docker container with the following command:

`docker run --rm -u root --name jenkins -d -p 8080:8080 -v jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins-with-docker`

You can get the initial admin password with the following commands:

    docker exec -it jenkins /bin/bash
    cat /var/jenkins_home/secrets/initialAdminPassword

After logging in, you need to install the **HTML Publisher** plugin to be able to publish the test reports. You also need to add the credentials for Docker Hub to be able to push images.
