#!/bin/bash
docker kill www-try-clojure-com
docker rm www-try-clojure-com
docker run -d --name www-try-clojure-com \
              --restart=always \
              -e VIRTUAL_HOST=www.try-clojure.com \
              -e LETSENCRYPT_HOST=www.try-clojure.com \
              -e LETSENCRYPT_EMAIL=abcdefg@gmail.com \
              --network=proxy \
              -v `pwd`/conf/nginx.conf:/etc/nginx/nginx.conf \
              -v `pwd`/logs:/var/log/nginx \
              -v `pwd`/html:/var/www/html \
              nginx

