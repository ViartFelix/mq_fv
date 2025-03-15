# MQ_FV

## Introduction
**MQ_FV** is a project conscisting of writting a Minecraft Paper plugin to provide light RGP elements to minecraft:

- Custom Tab layout 
- Damage indicators whener entities are damaged by another entity
- Money system (*beta*)
- Database persistance of Player's infos (money, name, uuid) (*beta*)

## Details
This project's plugin is mainly written in Kotlin, which I wanted to learn for quite a bit but never found a fitting project to learn, until I had the idea of trying to code a minecraft plugin.

For the minecraft server, no need to install the .zip file, just do ``docker compose up --build -d`` to pull the minecraft server image from Docker and ta-da, you got your own local minecraft server !
Also, this project uses PostgreSQL to store data, which is handled too by the docker compose.
