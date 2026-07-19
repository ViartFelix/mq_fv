FROM itzg/minecraft-server

MAINTAINER "Félix Viart"
LABEL authors="Félix Viart"
LABEL email="viart.felix@gmail.com"

# As root
ENV UID=1000
ENV GID=1000

# Server configuration
ENV EULA=TRUE
ENV TYPE=CUSTOM
ENV CUSTOM_SERVER=https://api.papermc.io/v2/projects/paper/versions/1.20.6/builds/151/downloads/paper-1.20.6-151.jar

# OPs
ENV OPS=JamesTheRefined,AWildSalami

# Other server params
ENV MODE=creative

# Adding main plugin
COPY --chmod=777 ./build/libs/mq_fv.jar /plugins/

# Adding dependencies of the main plugin
ADD --chmod=777 https://github.com/dmulloy2/ProtocolLib/releases/download/5.3.0/ProtocolLib.jar /plugins/ProtocolLib.jar
# ADD --chmod=777 https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar /plugins/

EXPOSE 25565