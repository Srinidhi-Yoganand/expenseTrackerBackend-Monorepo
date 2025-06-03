FROM kong:latest

WORKDIR /usr/local/kong

COPY custom-plugins /usr/local/kong/plugins

COPY config/kong.yml /etc/kong/kong.yml

ENV KONG_DATABASE=off
ENV KONG_PORTAL=off
ENV KONG_PLUGINS=bundled,custom-auth
ENV KONG_LOG_LEVEL=debug
ENV KONG_CONFIGURATION=/etc/kong/kong.yml

ENV KONG_PREFIX=/usr/local/kong

EXPOSE 8000 8443 8001

ENTRYPOINT ["/bin/sh", "-c", "kong reload -c /etc/kong/kong.yml && kong reload"]