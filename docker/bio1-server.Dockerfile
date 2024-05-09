FROM debian:bullseye

RUN apt update && apt install -y wget php7.4-mysql openjdk-17-jre openjdk-17-jre 

COPY --chown=www-data:www-data ./bioserv1 /var/www
COPY --chown=www-data:www-data ./bioserv1/www /var/www/bhof1

RUN wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j_8.0.32-1debian11_all.deb \
    && dpkg --install mysql-connector-j_8.0.32-1debian11_all.deb

WORKDIR /var/www

USER www-data

RUN mkdir -p /var/www/dnas/00000002 && ln -s /var/www/bhof1 /var/www/dnas/00000002

RUN cd bioserver && javac -cp /usr/share/java/mysql-connector-j-8.0.32.jar:. *.java

RUN mkdir -p bin/bioserver \
    && mv bioserver/*.class bin/bioserver \
    && mv bioserver/config.properties . \
    && mkdir lib \
    && cp /usr/share/java/mysql-connector-j-8.0.32.jar lib/mysql-connector.jar \
    && sed 's/{{EXTERNAL_IP}}/${EXTERNAL_IP}/g' config.properties

CMD [ "sh", "-c", "/var/www/run_file1.sh" ]



