FROM adoptopenjdk/openjdk11:debian-slim
USER root
RUN apt-get update && \
 apt-get -y install python
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY flag_y0u_found_1t_more_time /root
ENTRYPOINT ["java","-cp","app:app/lib/*","com.adioss.hack.Application"]