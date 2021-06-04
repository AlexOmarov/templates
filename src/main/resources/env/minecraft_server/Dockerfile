FROM ubuntu:latest

EXPOSE 25565
ENV TZ=Asia/Dubai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update && apt-get upgrade -y
RUN apt-get install screen -y
RUN apt-get install glances -y
RUN apt-get install sudo -y
RUN apt-get install cron -y
RUN apt-get install -y systemd
RUN apt-get install nano -y
RUN apt-get install software-properties-common -y
RUN apt-get install -my -y wget gnupg

RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add -
RUN add-apt-repository 'deb https://apt.corretto.aws stable main'
RUN apt-get install -y java-16-amazon-corretto-jdk


ARG PASSWORD
RUN useradd -G sudo -ms /bin/bash minecraftuser && echo "minecraftuser:$PASSWORD" | chpasswd

RUN mkdir /home/minecraftuser/minecraftdir
RUN mkdir /home/minecraftuser/backup

COPY home/minecraftuser/ /home/minecraftuser/

COPY home/minecraftuser/minecraftdir/tasks /etc/cron.d
RUN chmod 0644 /etc/cron.d/tasks && crontab /etc/cron.d/tasks

RUN  chmod u+x /home/minecraftuser/backup.sh
RUN  chmod u+x /home/minecraftuser/minecraftdir/start.sh

CMD ["cron", "-f"]