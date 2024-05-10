FROM php:7.0-fpm

RUN docker-php-ext-install pdo pdo_mysql mbstring mysqli
RUN docker-php-ext-enable mysqli

#Installing and setting up DNAS
WORKDIR /tmp

COPY --chown=www-data:www-data ./docker/vars/php/www /var/www

COPY --chown=www-data:www-data ./bioserv1/www /var/www/dnas/00000002

USER www-data

WORKDIR /var/www

EXPOSE 9000

CMD [ "php-fpm" ]