FROM openjdk:11-jdk

EXPOSE 8090:8090

RUN mkdir /app
COPY ./build/install/admin-order-service/ /app/
WORKDIR /app/bin

CMD ["./admin-order-service"]