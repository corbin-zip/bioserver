FROM debian:bullseye

WORKDIR /var/www

RUN apt-get update && apt-get install -y build-essential wget

#Setting up weak ciphers
RUN wget https://www.openssl.org/source/openssl-1.0.2q.tar.gz \
    && tar xzvf openssl-1.0.2q.tar.gz \
    && cd openssl-1.0.2q \
    && ./config --prefix=/opt/openssl-1.0.2 \
        --openssldir=/etc/ssl \
        shared enable-weak-ssl-ciphers \
        enable-ssl3 enable-ssl3-method \
        enable-ssl2 \
        -Wl,-rpath=/opt/openssl-1.0.2/lib \
    && make \
    && make install

COPY ./docker/vars/apache/arm-linux-gnueabihf.conf /etc/ld.so.conf.d/arm-linux-gnueabihf.conf

RUN ldconfig

#Compiling Apache2
RUN apt-get install -y libpcre3 \
    libpcre3-dev \
    libexpat1 \
    libexpat1-dev \
    libxml2 \
    libxml2-dev \
    libxslt1-dev \
    libxslt1.1

RUN wget https://dlcdn.apache.org/httpd/httpd-2.4.59.tar.gz \
    && wget https://downloads.apache.org/apr/apr-1.6.5.tar.gz \
    && wget https://downloads.apache.org/apr/apr-util-1.6.3.tar.gz \
    && tar xzvf httpd-2.4.59.tar.gz \
    && cd httpd-2.4.59/srclib/ \
    && tar xzvf ../../apr-1.6.5.tar.gz \
    && tar xzvf ../../apr-util-1.6.3.tar.gz \
    && ln -s apr-1.6.5 apr \
    && ln -s apr-util-1.6.3 apr-util \
    && cd ../ \
    && ./configure --prefix=/opt/apache \
        --with-included-apr \
        --with-ssl=/opt/openssl-1.0.2 \
        --enable-ssl \
    && make \
    && make install \
    && apt-get install -y php libapache2-mod-php7.4 

COPY --chown=0:0 ./docker/vars/apache/etc /etc/dnas 
COPY --chown=www-data:www-data ./docker/vars/php/www /var/www

RUN apt-get install -y iputils-ping

#Start this shit
WORKDIR /var/www
RUN touch /var/www/index.html
COPY ./docker/vars/apache/httpd.conf /opt/apache/conf/httpd.conf
COPY ./docker/vars/apache/start.sh /var/www/
COPY --chown=www-data:www-data ./docker/vars/php/www /var/www
COPY --chown=www-data:www-data ./bioserv1/www /var/www/bhof1


CMD [ "sh", "-c", "/var/www/start.sh" ]