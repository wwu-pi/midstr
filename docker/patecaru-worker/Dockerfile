FROM patecaru-base
MAINTAINER Fabian Wrede, Vincent von Hof <vincent@vhof.de>
ADD .ssh/ /root/.ssh/
RUN chmod 0600 /root/.ssh/msl_docker
RUN chown -R root:root /root/.ssh
RUN reload ssh

RUN chmod 0600 /root/.ssh/authorized_keys
RUN mv /root/.ssh/config2 /root/.ssh/config

RUN apt-get install -y openssh-server
RUN mkdir /var/run/sshd
#RUN echo 'PermitUserEnvironment=yes' >> /etc/ssh/sshd_config
#EXPOSE 22

WORKDIR /
COPY ./entrypoint.sh /
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
