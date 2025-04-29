FROM kong:3.0

WORKDIR /usr/local/kong

COPY ./custom-auth-plugin /usr/local/kong/plugins/custom-auth

ENV KONG_PLUGINS=bundled,custom-auth

EXPOSE 8000 8443 8001 8444

COPY ./kong.yml /etc/kong/kong.yml

CMD ["kong", "reload", "-c", "/etc/kong/kong.conf.default"]