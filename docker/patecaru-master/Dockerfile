FROM openmpi
MAINTAINER Fabian Wrede, Vincent von Hof <vincent@vhof.de>
RUN apt-get update -qqy && apt-get install curl -qqy
# Install Docker Machine
RUN curl -L https://github.com/docker/machine/releases/download/v0.7.0/docker-machine-`uname -s`-`uname -m` > /usr/local/bin/docker-machine
RUN chmod +x /usr/local/bin/docker-machine

ADD aws/ /root/.aws/
ADD docker-compose.yml /docker/docker-compose.yml

ADD ./runExperiment.sh /docker

ADD .ssh/ /root/.ssh/
RUN chmod 0600 /root/.ssh/muesli_master
RUN chown -R root:root /root/.ssh
RUN reload ssh

# Install Python and Basic Python Tools
RUN apt-get install -y python python-dev python-distribute python-pip
ADD web /web
# Get pip to download and install requirements:
RUN pip install -r /web/requirements.txt

# Expose ports
EXPOSE 80

WORKDIR /web
CMD python webservice.py
