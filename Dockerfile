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
ENV VERSION=1.20.6

# Other server params
ENV MODE=creative

# Adding main plugin
COPY --chmod=755 ./build/libs/mq_fv.jar /plugins/

# Adding dependencies of the main plugin
ADD --chmod=755 https://github.com/dmulloy2/ProtocolLib/releases/download/5.3.0/ProtocolLib.jar /plugins/

EXPOSE 25565