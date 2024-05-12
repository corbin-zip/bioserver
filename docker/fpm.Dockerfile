FROM php:7.0-fpm

ARG DNAS_SRC
ARG DNAS_DEST

RUN docker-php-ext-install pdo pdo_mysql mbstring mysqli
RUN docker-php-ext-enable mysqli

#Installing and setting up DNAS
WORKDIR /tmp

COPY --chown=www-data:www-data ./docker/vars/php/www /var/www

COPY --chown=www-data:www-data $DNAS_SRC $DNAS_DEST

USER www-data

WORKDIR /var/www

EXPOSE 9000

CMD [ "php-fpm" ]