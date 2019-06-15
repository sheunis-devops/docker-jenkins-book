# Configuration Management with Ansible

Keeping the code here, since this chapter doesn't use Jenkins.

The first step involves building the two types of Ansible images as follows:

    docker build -t ansible-master ansiblemaster
    docker build -t ansible-slave ansibleslave

You now need to run the three containers as follows:

    docker run --rm --name ansible-master -d -v ~/scratchpad/docker-jenkins-book/chapt6:/calculator ansible-master
    docker run --rm --name redis-slave -d ansible-slave
    docker run --rm --name app-slave -p 8080:8080 -d ansible-slave

The order is important, since we need to know the ip addresses of the redis and app containters.

You now need to add ssh keys to the servers to be able to provision software to them. From **ansible-master**, run the following:

    ssh-keygen -t rsa
    ssh-copy-id 172.17.0.3
    ssh-copy-id 172.17.0.4

The passwords to use is just **password**. This is obviously not secure, but for the sake of getting everything working in a small demo, it is fine.

The last step involves building the project and using Ansible to provision everything. You can do this as follows:

    ./gradlew build
    ansible-playbook playbook.yml

You should now be able to access the app server as follows:

    http://localhost:8080/sum?a=1&b=2
