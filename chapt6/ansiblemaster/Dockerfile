FROM ubuntu:bionic

RUN apt-get update && \
    apt-get install software-properties-common -y && \
    add-apt-repository ppa:ansible/ansible && \
    apt-get update && \
    apt-get install -y \
    ansible \
    vim \
    default-jre

COPY hosts /etc/ansible/hosts

CMD tail -f /dev/null
