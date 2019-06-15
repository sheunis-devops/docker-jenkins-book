FROM ubuntu:bionic

RUN apt-get update && \
    apt-get install -y \
    python3 \
    openssh-server && \
    echo "root:password" | chpasswd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

CMD service ssh start && tail -f /dev/null
