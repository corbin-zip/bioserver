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
    && make install 

#Installing and setting up DNAS
WORKDIR /tmp

RUN apt-get install -y git

RUN git clone https://github.com/corbin-ch/DNASrep.git \
    && mv DNASrep/etc/dnas /etc/dnas \
    && chown -R 0:0 /etc/dnas \
    && mkdir -p /var/www/dnas \
    && mv DNASrep/www/dnas /var/www \
    && chown -R www-data:www-data /var/www/dnas

#Setting up DNS
#TODO: remove php to another container
# RUN apt-get install -y dnsmasq dnsutils php7.4-fpm
RUN apt-get install -y php7.4-fpm

# COPY ./docker/vars/apache/httpd.conf /opt/apache/conf/httpd.conf
# COPY ./docker/vars/apache/obcomsrv /etc/dnsmasq.d/obcomsrv
# COPY ./docker/vars/apache/dnsmasq.conf /etc/dnsmasq.conf
# COPY ./docker/vars/apache/start.sh /var/www/


#Start this shit
WORKDIR /var/www

CMD [ "sh", "-c", "/var/www/start.sh" ]

EXPOSE 2003