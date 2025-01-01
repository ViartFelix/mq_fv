FROM itzg/minecraft-server

MAINTAINER "Félix Viart"
LABEL authors="Félix Viart"
LABEL email="viart.felix@gmail.com"

# As root
ENV UID=1000
ENV GID=1000

# Server configuration
ENV EULA=TRUE
ENV TYPE=PAPER
ENV VERSION=1.21.3

# Other server params
ENV MODE=creative

# Adding plugins to the server
RUN mkdir /_build
COPY ./build/libs/mq_fv.jar /_build
ENV PLUGINS=/_build/mq_fv.jar

EXPOSE 25565